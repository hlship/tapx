// Copyright 2009 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.templating.base;

import com.howardlewisship.tapx.templating.RenderedStream;
import com.howardlewisship.tapx.templating.TemplateAPI;
import com.howardlewisship.tapx.templating.TemplateRenderer;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.ioc.test.TestBase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.*;
import java.util.List;

public class AbstractTemplatingIntegrationTest extends TestBase
{
    protected static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";

    public static final String DEFAULT_LOCATION = "default";

    private final String packageName;

    private final String contextDir;

    protected TemplateAPI templateAPI;

    private String tapestryVersion;

    protected AbstractTemplatingIntegrationTest(String packageName, String contextDir)
    {
        this.packageName = packageName;
        this.contextDir = contextDir;
    }

    @BeforeClass
    public void setup_template_API()
    {
        templateAPI = new TemplateAPI(packageName, new File(contextDir));

        Registry registry = templateAPI.getRegistry();

        SymbolSource source = registry.getService(SymbolSource.class);

        tapestryVersion = source.valueForSymbol(SymbolConstants.TAPESTRY_VERSION);
    }

    @AfterMethod
    public void cleanup_thread_after_method()
    {
        templateAPI.cleanupThread();
    }

    @AfterClass
    public void shutdown_api_after_class()
    {
        templateAPI.shutdown();
    }

    protected void compare(RenderedStream renderedStream,
                           String contentFile) throws IOException
    {
        compare(renderedStream, DEFAULT_CONTENT_TYPE, contentFile);
    }

    protected void compare(RenderedStream renderedStream, String expectedContentType,
                           String contentFile)
            throws IOException
    {
        assertEquals(renderedStream.getContentType(), expectedContentType);

        InputStream actualStream = renderedStream.getStream();

        try
        {
            compare(actualStream, getClass().getResourceAsStream(contentFile));
        }
        catch (AssertionError ex)
        {
            dumpRenderedStream(renderedStream);

            throw ex;
        }
    }

    protected void compare(InputStream actualStream, String contentFile)
            throws IOException
    {
        compare(actualStream, getClass().getResourceAsStream(contentFile));
    }

    protected void compare(InputStream actualStream, InputStream expectedStream)
            throws IOException
    {
        List<String> actual = read(actualStream);
        List<String> expected = read(expectedStream);

        assertListsEquals(actual, expected);
    }


    protected void dumpRenderedStream(RenderedStream renderedStream)
            throws IOException
    {
        System.err.println("Rendered stream content:");

        TapestryInternalUtils.copy(renderedStream.getStream(), System.err);

        System.err.println();

        System.err.flush();
    }

    /**
     * Reads a stream into a list of individual lines. Whitespace for each line is trimmed. Each element (open or close)
     * is moved to its own line, thus making any comparison agnostic about whitespace.
     *
     * @param stream
     * @return
     * @throws IOException
     */
    private List<String> read(InputStream stream) throws IOException
    {
        List<String> result = CollectionFactory.newList();

        LineNumberReader reader = new LineNumberReader(new InputStreamReader(new BufferedInputStream(stream)));

        while (true)
        {
            String line = reader.readLine();

            if (line == null) break;

            String filtered = line.trim().replace(tapestryVersion, "[TAPVERSION]");

            while (true)
            {
                int splitx = filtered.indexOf("><");

                if (splitx < 0) break;

                result.add(filtered.substring(0, splitx + 1));

                filtered = filtered.substring(splitx + 1);
            }

            result.add(filtered);
        }

        reader.close();

        return result;
    }

    protected void assertResponseContains(RenderedStream renderedStream, String string) throws IOException
    {
        String streamContent = extractStreamContent(renderedStream);

        if (streamContent.contains(string))
            return;

        dumpRenderedStream(renderedStream);

        throw new AssertionError(String.format("RenderedStream does not contain input string '%s'.",
                                               string));
    }

    private String extractStreamContent(RenderedStream renderedStream) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(renderedStream.getSize());

        TapestryInternalUtils.copy(renderedStream.getStream(), bos);

        return new String(bos.toByteArray());
    }

    /**
     * Convienience for creating a renderer for locale "en" and location "defaultLocation".
     *
     * @param templateName
     * @return
     */
    protected TemplateRenderer createRenderer(String templateName)
    {
        return templateAPI.createRenderer(templateName, "en", DEFAULT_LOCATION);
    }
}
