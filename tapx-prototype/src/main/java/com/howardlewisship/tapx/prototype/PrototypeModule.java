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

import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.corelib.components.Palette;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.internal.services.javascript.CoreJavaScriptStack;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ClientInfrastructure;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.javascript.JavaScriptStack;

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
     * This has the side effect of all pages importing the Tapestry {@link Palette}s JavaScript
     * (so that it can be patched) even if they don't use the Palette.
     */
    public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration,
            ClientInfrastructure clientInfrastructure,
            @Inject@Symbol(SymbolConstants.PRODUCTION_MODE) boolean productionMode,
            @Inject
            @Path("classpath:com/howardlewisship/tapx/prototype/tapestry-js-fixes.js")
            final Asset tapestryPatches,
            @Inject
            @Path("classpath:org/apache/tapestry5/corelib/components/palette.js")
            final Asset paletteLibrary )
    {
        configuration.override(InternalConstants.CORE_STACK_NAME, new CoreJavaScriptStack(clientInfrastructure, productionMode){
            @Override
            public List<Asset> getJavaScriptLibraries() {
                return F.flow(super.getJavaScriptLibraries()).append(paletteLibrary, tapestryPatches).toList();
            }
        });
    }
}