// Copyright 2009, 2011 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.prototype;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.corelib.components.Palette;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class PrototypeModule
{
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.override("tapestry.scriptaculous.path", "com/howardlewisship/tapx/prototype");
    }

    public static void contributeComponentClassResolver(final Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("tapx", "com.howardlewisship.tapx.prototype"));
    }

    /**
     * Patches Tapestry to include a file of fixes to make Tapestry 5.2.5 work with Prototype 1.7.
     * This has two side effects: first <em>every</em> page will not have the base Tapestry stack on it.
     * That's ok ... most pages already will have this. Secondly, all pages will import the Tapestry {@link Palette}s
     * JavaScript (so that it can be patched) even if they don't use the Palette.
     */
    public static void contributeMarkupRenderer(final OrderedConfiguration<MarkupRendererFilter> configuration,
            final Environment environment,

            @Inject
            @Path("classpath:org/apache/tapestry5/corelib/components/palette.js")
            final Asset paletteLibrary,

            @Inject
            @Path("classpath:com/howardlewisship/tapx/prototype/tapestry-js-fixes.js")
            final Asset tapestryPatches)
    {

        MarkupRendererFilter tapestryFixesFilter = new MarkupRendererFilter()
        {
            public void renderMarkup(final MarkupWriter writer, final MarkupRenderer renderer)
            {
                JavaScriptSupport javaScriptSupport = environment.peekRequired(JavaScriptSupport.class);

                javaScriptSupport.importJavaScriptLibrary(paletteLibrary);
                javaScriptSupport.importJavaScriptLibrary(tapestryPatches);

                renderer.renderMarkup(writer);
            }
        };

        configuration.add("TapestryFixes", tapestryFixesFilter, "after:RenderSupport");
    }
}
