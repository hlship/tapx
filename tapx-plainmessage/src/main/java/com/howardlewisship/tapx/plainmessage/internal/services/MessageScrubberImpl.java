// Copyright 2009, 2010 Howard M. Lewis Ship
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.howardlewisship.tapx.plainmessage.internal.services;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.ioc.MessageFormatter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.howardlewisship.tapx.plainmessage.services.MessageScrubber;

public class MessageScrubberImpl implements MessageScrubber
{
    private final Pattern element = Pattern.compile("<.+?>");

    private final Pattern entity = Pattern.compile("&(.+?);");

    private final Map<String, String> entities = CollectionFactory.newMap();

    {
        entities.put("lt", "<");
        entities.put("gt", ">");
        entities.put("amp", "&");
        entities.put("quot", "\"");
        entities.put("apos", "'");
        entities.put("nbsp", "\u00a0");
    }

    public String scrub(String input)
    {
        assert input != null;

        String elementsStripped = element.matcher(input).replaceAll("");

        String result = convertEntities(elementsStripped);

        // A little bit of interning to keep memory usage down.

        return input.equals(result) ? input : result;
    }

    private String convertEntities(String input)
    {
        StringBuffer buffer = new StringBuffer();

        Matcher matcher = entity.matcher(input);

        while (matcher.find())
        {
            String entity = matcher.group(1);

            String replacement = entityReplacement(entity);

            matcher.appendReplacement(buffer, replacement);
        }

        matcher.appendTail(buffer);

        return buffer.toString();
    }

    private String entityReplacement(String entity)
    {
        String result = entities.get(entity);

        if (result != null) return result;

        if (entity.startsWith("#"))
        {
            try
            {
                int code = Integer.parseInt(entity.substring(1));

                return Character.toString((char) code);
            }
            catch (NumberFormatException ex)
            {
                // Ignore and fall down to later exception reporting.
            }
        }

        throw new RuntimeException(String.format("Unknown XML entity: '&%s;'.", entity));
    }


    public Messages scrub(final Messages messages)
    {
        assert messages != null;

        return new Messages()
        {
            public boolean contains(String key)
            {
                return messages.contains(key);
            }

            public String get(String key)
            {
                return scrub(messages.get(key));
            }

            public MessageFormatter getFormatter(String key)
            {
                final MessageFormatter formatter = messages.getFormatter(key);

                return new MessageFormatter()
                {
                    public String format(Object... args)
                    {
                        return scrub(formatter.format(args));
                    }
                };
            }

            public String format(String key, Object... args)
            {
                return scrub(messages.format(key, args));
            }
        };
    }
}
