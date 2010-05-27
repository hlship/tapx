// Copyright 2010 Howard M. Lewis Ship
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.howardlewisship.tapx.core.internal.dynamic;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.internal.parser.AttributeToken;
import org.apache.tapestry5.internal.services.XMLTokenStream;
import org.apache.tapestry5.internal.services.XMLTokenType;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.ioc.util.func.AbstractMapper;
import org.apache.tapestry5.ioc.util.func.AbstractWorker;
import org.apache.tapestry5.ioc.util.func.F;
import org.apache.tapestry5.ioc.util.func.Flow;
import org.apache.tapestry5.ioc.util.func.Mapper;
import org.apache.tapestry5.ioc.util.func.Worker;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.runtime.RenderQueue;

import com.howardlewisship.tapx.core.dynamic.BlockSource;
import com.howardlewisship.tapx.core.dynamic.DynamicTemplate;

/** Does the heavy lifting for {@link DynamicTemplateParserImpl}. */
class DynamicTemplateSaxParser
{
    private final Resource resource;

    private final XMLTokenStream tokenStream;

    private final Map<String, URL> publicIdToURL = Collections.emptyMap();

    private static final Pattern PARAM_ID_PATTERN = Pattern.compile("^param:(\\p{Alpha}\\w*)$",
            Pattern.CASE_INSENSITIVE);

    private static final DynamicTemplateElement END = new DynamicTemplateElement()
    {
        public void render(MarkupWriter writer, RenderQueue queue, BlockSource blockSource)
        {
            // End the previously started element
            writer.end();
        }
    };

    DynamicTemplateSaxParser(Resource resource)
    {
        this.resource = resource;
        this.tokenStream = new XMLTokenStream(resource, publicIdToURL);
    }

    DynamicTemplate parse()
    {
        try
        {
            tokenStream.parse();

            return toDynamicTemplate(root());
        }
        catch (Exception ex)
        {
            throw new TapestryException(String.format("Failure parsing dynamic template %s: %s", resource,
                    InternalUtils.toMessage(ex)), tokenStream.getLocation(), ex);
        }
    }

    // Note the use of static methods; otherwise the compiler sets this$0 to point to the DynamicTemplateSaxParser,
    // creating an unwanted reference that keeps the parser from being GCed.

    private static DynamicTemplate toDynamicTemplate(List<DynamicTemplateElement> elements)
    {
        final Flow<DynamicTemplateElement> flow = F.flow(elements).reverse();

        return new DynamicTemplate()
        {
            public RenderCommand createRenderCommand(final BlockSource blockSource)
            {
                final Mapper<DynamicTemplateElement, RenderCommand> toRenderCommand = createToRenderCommandMapper(blockSource);

                return new RenderCommand()
                {
                    public void render(MarkupWriter writer, RenderQueue queue)
                    {
                        Worker<RenderCommand> pushOnQueue = createQueueRenderCommand(queue);

                        flow.map(toRenderCommand).each(pushOnQueue);
                    }
                };
            }
        };
    }

    private List<DynamicTemplateElement> root()
    {
        List<DynamicTemplateElement> result = CollectionFactory.newList();

        while (tokenStream.hasNext())
        {
            switch (tokenStream.next())
            {
                case START_ELEMENT:
                    result.add(element());
                    break;

                case END_DOCUMENT:
                    // Ignore it.
                    break;

                default:
                    result.add(textContent());
            }
        }

        return result;
    }

    private DynamicTemplateElement element()
    {
        String elementURI = tokenStream.getNamespaceURI();
        String elementName = tokenStream.getLocalName();

        String blockId = null;

        int count = tokenStream.getAttributeCount();

        List<AttributeToken> attributes = CollectionFactory.newList();

        Location location = getLocation();

        for (int i = 0; i < count; i++)
        {
            QName qname = tokenStream.getAttributeName(i);

            // The name will be blank for an xmlns: attribute

            String localName = qname.getLocalPart();

            if (InternalUtils.isBlank(localName))
                continue;

            String uri = qname.getNamespaceURI();

            String value = tokenStream.getAttributeValue(i);

            if (localName.equals("id"))
            {
                Matcher matcher = PARAM_ID_PATTERN.matcher(value);

                if (matcher.matches())
                {
                    blockId = matcher.group(1);
                    continue;
                }
            }

            attributes.add(new AttributeToken(uri, localName, value, location));
        }

        if (blockId != null)
            return block(blockId);

        List<DynamicTemplateElement> body = CollectionFactory.newList();

        boolean atEnd = false;
        while (!atEnd)
        {
            switch (tokenStream.next())
            {
                case START_ELEMENT:

                    // Recurse into this new element
                    body.add(element());

                    break;

                case END_ELEMENT:
                    body.add(END);
                    atEnd = true;

                    break;

                default:

                    body.add(textContent());
            }
        }

        return createElementWriterElement(elementURI, elementName, attributes, body);
    }

    private static DynamicTemplateElement createElementWriterElement(final String elementURI, final String elementName,
            final List<AttributeToken> attributes, List<DynamicTemplateElement> body)
    {
        final Flow<DynamicTemplateElement> bodyFlow = F.flow(body).reverse();

        return new DynamicTemplateElement()
        {
            public void render(MarkupWriter writer, RenderQueue queue, BlockSource blockSource)
            {
                // Write the element ...

                writer.elementNS(elementURI, elementName);

                // ... and the attributes

                for (AttributeToken attribute : attributes)
                {
                    writer.attributeNS(attribute.getNamespaceURI(), attribute.getName(), attribute.getValue());
                }

                // And convert the DTEs for the direct children of this element into RenderCommands and push them onto
                // the queue. This includes the child that will end the started element.

                Mapper<DynamicTemplateElement, RenderCommand> toRenderCommand = createToRenderCommandMapper(blockSource);
                Worker<RenderCommand> pushOnQueue = createQueueRenderCommand(queue);

                bodyFlow.map(toRenderCommand).each(pushOnQueue);
            }
        };
    }

    private DynamicTemplateElement block(final String blockId)
    {
        Location location = getLocation();

        removeContent();

        return createBlockElement(blockId, location);
    }

    private static DynamicTemplateElement createBlockElement(final String blockId, final Location location)
    {
        return new DynamicTemplateElement()
        {
            public void render(MarkupWriter writer, RenderQueue queue, BlockSource blockSource)
            {
                try
                {
                    Block block = blockSource.getBlock(blockId);

                    queue.push((RenderCommand) block);
                }
                catch (Exception ex)
                {
                    throw new TapestryException(String.format(
                            "Exception rendering block '%s' as part of dynamic template: %s", blockId, InternalUtils
                                    .toMessage(ex)), location, ex);
                }
            }
        };
    }

    private Location getLocation()
    {
        return tokenStream.getLocation();
    }

    private void removeContent()
    {
        int depth = 1;

        while (true)
        {
            switch (tokenStream.next())
            {
                case START_ELEMENT:
                    depth++;
                    break;

                // The matching end element.

                case END_ELEMENT:
                    depth--;

                    if (depth == 0)
                        return;

                    break;

                default:
                    // Ignore anything else (text, comments, etc.)
            }
        }
    }

    private DynamicTemplateElement textContent()
    {
        switch (tokenStream.getEventType())
        {
            case COMMENT:
                return comment();

            case CHARACTERS:
            case SPACE:
                return characters();

            default:
                return unexpectedEventType();
        }
    }

    private DynamicTemplateElement comment()
    {
        return createCommentElement(tokenStream.getText());
    }

    private static DynamicTemplateElement createCommentElement(final String content)
    {
        return new DynamicTemplateElement()
        {
            public void render(MarkupWriter writer, RenderQueue queue, BlockSource blockSource)
            {
                writer.comment(content);
            }
        };
    }

    private DynamicTemplateElement characters()
    {
        return createTextWriterElement(tokenStream.getText());
    }

    private static DynamicTemplateElement createTextWriterElement(final String content)
    {
        return new DynamicTemplateElement()
        {
            public void render(MarkupWriter writer, RenderQueue queue, BlockSource blockSource)
            {
                writer.write(content);
            }
        };
    }

    private <T> T unexpectedEventType()
    {
        XMLTokenType eventType = tokenStream.getEventType();

        throw new IllegalStateException(String.format("Unexpected XML parse event %s.", eventType.name()));
    }

    private static Worker<RenderCommand> createQueueRenderCommand(final RenderQueue queue)
    {
        return new AbstractWorker<RenderCommand>()
        {
            public void work(RenderCommand value)
            {
                queue.push(value);
            }
        };
    }

    private static RenderCommand toRenderCommand(final DynamicTemplateElement value, final BlockSource blockSource)
    {
        return new RenderCommand()
        {
            public void render(MarkupWriter writer, RenderQueue queue)
            {
                value.render(writer, queue, blockSource);
            }
        };
    }

    private static Mapper<DynamicTemplateElement, RenderCommand> createToRenderCommandMapper(
            final BlockSource blockSource)
    {
        return new AbstractMapper<DynamicTemplateElement, RenderCommand>()
        {
            public RenderCommand map(final DynamicTemplateElement value)
            {
                return toRenderCommand(value, blockSource);
            }
        };
    }
}
