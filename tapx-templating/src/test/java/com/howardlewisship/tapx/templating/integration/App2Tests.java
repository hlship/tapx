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

package com.howardlewisship.tapx.templating.integration;

import com.howardlewisship.tapx.templating.*;
import com.howardlewisship.tapx.templating.base.AbstractTemplatingIntegrationTest;

import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App2Tests extends AbstractTemplatingIntegrationTest
{
    public App2Tests()
    {
        super("app2", "src/test/context.2");
    }

    @Test
    public void simple() throws IOException
    {
        TemplateRenderer renderer = createRenderer("simple");

        RenderedStream renderedStream = renderer.render();

        compare(renderedStream, "simple.txt");

        assertEquals(renderedStream.toString(), "RenderedStream[Simple]");
    }

    @Test
    public void init_before_render() throws IOException
    {
        TemplateRenderer renderer = createRenderer("NumberOutput");

        assertSame(renderer.init("number", 42), renderer);

        RenderedStream renderedStream = renderer.render();

        compare(renderedStream, "init_before_render.txt");
    }

    @Test
    public void init_after_render_not_allowed() throws Exception
    {
        TemplateRenderer renderer = createRenderer("NumberOutput");

        assertSame(renderer.init("number", 42), renderer);

        renderer.render();

        try
        {
            renderer.init("number", 42);
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            // Expected.
        }
    }

    @Test
    public void render_only_allowed_once() throws Exception
    {
        TemplateRenderer renderer = createRenderer("simple");

        RenderedStream renderedStream = renderer.render();

        assertEquals(renderedStream.toString(), "RenderedStream[Simple]");

        try
        {
            renderer.render();
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            // Expected.
        }
    }

    @Test
    public void init_after_thread_cleanup_not_allowed() throws Exception
    {
        TemplateRenderer renderer = createRenderer("NumberOutput");

        templateAPI.cleanupThread();

        try
        {
            renderer.init("doesNotMatter", 99);
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            assertEquals(ex.getMessage(), "TemplateRenderer may not be used once the thread is cleaned up.");
        }
    }

    @Test
    public void render_after_thread_cleanup_not_allowed() throws Exception
    {
        TemplateRenderer renderer = createRenderer("NumberOutput");

        templateAPI.cleanupThread();

        try
        {
            renderer.render();
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            assertEquals(ex.getMessage(), "TemplateRenderer may not be used once the thread is cleaned up.");
        }
    }

    @DataProvider
    public Object[][] localization_support_data()
    {
        return new Object[][]
                {
                        { "en", "en" },
                        { "fr", "fr" },
                        { "ru", "en" },
                        { "fr_BE", "fr" }
                };
    }

    @Test(dataProvider = "localization_support_data")
    public void localization_support(String desiredLocaleName, String expectedLocaleName) throws IOException
    {
        TemplateRenderer renderer = templateAPI.createRenderer("EchoLocale", desiredLocaleName, DEFAULT_LOCATION);

        assertResponseContains(renderer.render(), "Page locale: " + expectedLocaleName);

    }

    @Test
    public void perform_template_operation() throws IOException
    {
        final AtomicReference<TemplateRenderer> rendererRef = new AtomicReference<TemplateRenderer>();

        TemplateRendererCallback callback = new TemplateRendererCallback()
        {
            public void performOperation(TemplateRenderer renderer)
                    throws IOException
            {
                rendererRef.set(renderer);

                assertResponseContains(renderer.render(),
                                       "Simple Template Test");
            }
        };

        templateAPI.performTemplateRendererOperation("Simple", DEFAULT_CONTENT_TYPE, DEFAULT_LOCATION,
                                                     callback);


        // This proves that the callback was invoked.

        assertNotNull(rendererRef.get());

        // Now prove that the thread has been cleaned up.

        try
        {
            rendererRef.get().render();
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            // Expected.
        }
    }

    @Test
    public void unknown_location_name()
    {
        try
        {
            templateAPI.createRenderer("Simple", "en", "unknown-location-name");
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals(ex.getMessage(),
                         "Location 'unknown-location-name' is not defined. Defined location names: default, europe.");
        }
    }

    @Test
    public void context_asset_from_default_location() throws IOException
    {
        TemplateRenderer renderer = createRenderer("contextasset");

        RenderedStream renderedStream = renderer.render();

        compare(renderedStream, "context_asset_from_default_location.txt");
    }

    @Test
    public void context_asset_from_alternate_location() throws IOException
    {
        TemplateRenderer renderer = templateAPI.createRenderer("contextasset", "fr", "europe");

        compare(renderer.render(), "context_asset_from_alternate_location.txt");
    }

    @Test
    public void page_render_exception() throws IOException
    {
        TemplateRenderer renderer = createRenderer("failedRender");

        try
        {
            renderer.render();

            unreachable();
        }
        catch (RuntimeException ex)
        {
            assertEquals(ex.getMessage(),
                         "Render queue error in AfterRender[FailedRender]: FailedRender: forced an error during page rendering.");
        }
    }

    @Test
    public void classpath_assets() throws Exception
    {
        TemplateRenderer renderer = createRenderer("classpathAssets");

        RenderedStream renderedStream = renderer.render();

        String body = extractString(renderedStream);

        assertTrue(body.startsWith("<html"), "Body starts with <html");

        Pattern p = Pattern.compile("<img src=\"cid:(.*?)\"");

        Matcher matcher = p.matcher(body);

        List<String> cids = CollectionFactory.newList();

        while (matcher.find())
            cids.add(matcher.group(1));

        assertEquals(cids.size(), 3);

        // The first and third should match.

        assertEquals(cids.get(0), cids.get(2));

        assertEquals(renderedStream.getEnclosures().size(), 2);

        for (RenderedStreamEnclosure ec : renderedStream.getEnclosures())
        {
            assertTrue(cids.contains(ec.getContentID()));
            assertEquals(ec.getContentType(), "image/png");
        }

    }

    private String extractString(ContentStream contentStream) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        TapestryInternalUtils.copy(contentStream.getStream(), bos);

        return bos.toString();
    }

    @Test
    public void global_message_catalog() throws IOException
    {
        TemplateRenderer renderer = createRenderer("GlobalMessages");

        RenderedStream renderedStream = renderer.render();

        compare(renderedStream, "global_message_catalog.txt");
    }
}
