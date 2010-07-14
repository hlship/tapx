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

package com.howardlewisship.tapx.templating;

import java.io.File;
import java.io.IOException;

import org.apache.tapestry5.internal.TapestryAppInitializer;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.ApplicationInitializer;
import org.apache.tapestry5.services.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.howardlewisship.tapx.templating.internal.TemplateContext;
import com.howardlewisship.tapx.templating.services.TemplateModule;
import com.howardlewisship.tapx.templating.services.TemplateRendererSource;

/**
 * Responsible for starting up the Tapestry Template Library, analagous to how TapestryFilter starts up a Tapestry
 * application.
 */
public class TemplateAPI implements TemplateRendererSource
{
    private final Logger logger = LoggerFactory.getLogger(TemplateAPI.class);

    private final TemplateRendererSource templateRendererSource;

    private final Registry registry;

    /**
     * Starts the {@link org.apache.tapestry5.ioc.Registry} and initializes the Tapestry framework and application. For
     * consistency with Tapestry Servlet, we look for an AppModule class in the services package.
     * <p/>
     * <p>Page template files may be stored on classpath, or in the provided context directory. The context directory
     * also contains static assets that may be referenced from the templates. It is assumed that the context directory
     * is a valid copy (or perhaps even a direct reference to) a deployed web context.
     *
     * @param applicationPackage root package name to search for pages, components and services
     * @param contextDirectory   directory containing static resources and template files
     */
    public TemplateAPI(String applicationPackage, File contextDirectory)
    {
        assert InternalUtils.isNonBlank(applicationPackage);
        assert contextDirectory != null;

        Context context = new TemplateContext(contextDirectory);

        TapestryAppInitializer initializer = new TapestryAppInitializer(logger, applicationPackage, "app",
                                                                        TemplateConstants.TEMPLATE_MODE);

        initializer.addModules(TemplateModule.class);

        registry = initializer.createRegistry();

        ApplicationInitializer ai = registry.getService("ApplicationInitializer", ApplicationInitializer.class);

        // We bypass the servlet initializer since we don't have the Servlet API artifacts and jump right into the
        // Tapestry generic initialization layer.

        ai.initializeApplication(context);

        registry.performRegistryStartup();

        templateRendererSource = registry.getService(TemplateRendererSource.class);

        initializer.announceStartup();
    }

    /**
     * This method must be called after rendering content; it ensures that any resources used during rendering are
     * cleaned up (this includes returning page instances to the page pool for later reuse).
     */
    public void cleanupThread()
    {
        registry.cleanupThread();
    }

    /**
     * Creates an returns a renderer.  You should invoke {@link #cleanupThread()} when done with the renderer.
     *
     * @param templateName name of template; a page name
     * @param localeName   identifies the locale in which the response should be generated
     * @param location     identifies the location of the recipient, which can be used to adjust aspects of the template
     *                     (it is intended to support location-specific content delivery servers)
     * @return the renderer
     */
    public TemplateRenderer createRenderer(String templateName, String localeName, String location)
    {
        return templateRendererSource.createRenderer(templateName, localeName, location);
    }

    /**
     * A convienience for performing an operation with a {@link com.howardlewisship.tapx.templating.TemplateRenderer}, then
     * {@linkplain #cleanupThread() cleaning up the thread}. Note that a TemplateRenderer is invalidated by thread
     * cleanup.
     *
     * @param templateName name of template; a page name
     * @param localeName   identifies the locale in which the response should be generated
     * @param location     identifies the location of the recipient, which can be used to adjust aspects of the template
     *                     (it is intended to support location-specific content delivery servers)
     * @throws IOException
     */
    public void performTemplateRendererOperation(String templateName, String localeName, String location,
                                                 TemplateRendererCallback callback)
            throws IOException
    {
        try
        {
            TemplateRenderer renderer = createRenderer(templateName, localeName, location);

            callback.performOperation(renderer);
        }
        finally
        {
            cleanupThread();
        }
    }

    /**
     * Returns the registry.  You should be careful to invoke {@link org.apache.tapestry5.ioc.Registry#cleanupThread()}
     * when done with the Registry, to ensure that objects created by services in the registry are properly returned to
     * the garbage collector.
     *
     * @return the services registry
     */
    public Registry getRegistry()
    {
        return registry;
    }

    /**
     * Shuts down the Registry, which immediately invalidates all services.
     */
    public void shutdown()
    {
        registry.shutdown();
    }
}
