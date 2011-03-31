// Copyright 2009 Howard M. Lewis Ship
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
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.PartialMarkupRenderer;
import org.apache.tapestry5.services.PartialMarkupRendererFilter;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class PrototypeModule
{
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.override("tapestry.scriptaculous.path", "com/howardlewisship/tapx/prototype");
    }

    public static void contributeMarkupRenderer(
            final OrderedConfiguration<MarkupRendererFilter> configuration,
            final Environment environment,
            @Inject @Path("classpath:com/howardlewisship/tapx/prototype/tapestry-js-fixes.js") final Asset tapestryPatches) {
        MarkupRendererFilter tapestryFixesFilter = new MarkupRendererFilter() {
            public void renderMarkup(final MarkupWriter writer, final MarkupRenderer renderer) {
                JavaScriptSupport javaScriptSupport = environment.peekRequired(JavaScriptSupport.class);
                javaScriptSupport.importJavaScriptLibrary(tapestryPatches);
                renderer.renderMarkup(writer);
            }
        };

        configuration.add("TapestryFixes", tapestryFixesFilter, "after:RenderSupport");
    }

    public static void contributePartialMarkupRenderer(
            final OrderedConfiguration<PartialMarkupRendererFilter> configuration,
            final Environment environment,
            @Inject @Path("classpath:com/howardlewisship/tapx/prototype/tapestry-js-fixes.js") final Asset tapestryPatches) {

        PartialMarkupRendererFilter tapestryFixes = new PartialMarkupRendererFilter() {
            public void renderMarkup(final MarkupWriter writer, final JSONObject reply,
                    final PartialMarkupRenderer renderer) {
                JavaScriptSupport javaScriptSupport = environment.peekRequired(JavaScriptSupport.class);
                javaScriptSupport.importJavaScriptLibrary(tapestryPatches);
                renderer.renderMarkup(writer, reply);
            }
        };

        configuration.add("TapestryFixes", tapestryFixes, "after:RenderSupport");

    }
}
