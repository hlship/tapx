// Copyright 2010, 2011 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.core.services;

import com.howardlewisship.tapx.core.CoreSymbols;
import com.howardlewisship.tapx.core.TapxCore;
import com.howardlewisship.tapx.core.internal.services.CondBindingFactory;
import com.howardlewisship.tapx.core.internal.services.ConditionSourceImpl;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.javascript.*;

public class CoreModule
{
    private static final String PATH = "classpath:com/howardlewisship/tapx/core";

    @SuppressWarnings("unchecked")
    public static void bind(ServiceBinder binder)
    {
        binder.bind(ConditionSource.class, ConditionSourceImpl.class);
        binder.bind(BindingFactory.class, CondBindingFactory.class).withId("CondBindingFactory");
        binder.bind(JavaScriptStack.class, ExtensibleJavaScriptStack.class).withId("TapxCoreJavaScriptStack")
                .withMarker(TapxCore.class);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(CoreSymbols.TEST_MODE, "false");
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("tapx", "com.howardlewisship.tapx.core"));

        // What's this about? Because other modules in the TapX project ALSO contribute "tapx"
        // mapped to other sub-packages of "com.howardlewisship.tapx", there would otherwise
        // be a difference in how asset URL paths are created dependening on whether it was just
        // tapx-core or tapx-core plus (for example) tapx-datefield. Since the tapx-core.css file relies
        // on a particular layout so that it can reference images that are part of tapestry-core.jar,
        // we need at least two mappings for the tapx virtual folder.

        configuration.add(new LibraryMapping("tapx", "com.howardlewisship.tapx.placeholder"));
    }

    public static void contributeComponentMessagesSource(OrderedConfiguration<Resource> configuration, @Value(PATH
            + "/tapx-core.properties")
    Resource coreCatalog)
    {
        configuration.add("TapxCore", coreCatalog, "before:AppCatalog");
    }

    @Contribute(JavaScriptStackSource.class)
    public static void provideTapxCoreStack(MappedConfiguration<String, JavaScriptStack> configuration, @TapxCore
    JavaScriptStack tapxCoreStack)
    {
        configuration.add("tapx-core", tapxCoreStack);
    }


    @Contribute(BindingSource.class)
    public static void setupCondBindingPrefix(MappedConfiguration<String, BindingFactory> configuration, @Local
    BindingFactory condBindingFactory)
    {
        configuration.add("cond", condBindingFactory);
    }

    /**
     * Adds two default conditions to be used with {@link ConditionSource} (and the "cond:" binding prefix):
     * <dl>
     * <dt>production-mode</dt>
     * <dd>Driven by symbol {@link SymbolConstants#PRODUCTION_MODE}</dd>
     * <dt>test-mode</dt>
     * <dd>Driven by symbol {@link CoreSymbols#TEST_MODE}</dd>
     * </dl>
     */
    @Contribute(ConditionSource.class)
    public static void basicConditions(MappedConfiguration<String, Condition> configuration,
                                       @Symbol(SymbolConstants.PRODUCTION_MODE)
                                       boolean productionMode, @Symbol(CoreSymbols.TEST_MODE)
    boolean testMode)
    {
        configuration.add("production-mode", new FixedCondition(productionMode));
        configuration.add("test-mode", new FixedCondition(testMode));
    }

    /**
     * Makes contributions:
     * <dl>
     * <dt>CoreJS</dt>
     * <dd>Core JavaScript library</dd>
     * <dt>CoreJS-TestMode</dt>
     * <dd>Only in {@link CoreSymbols#TEST_MODE}</dd>
     * <dt>ScriptaculousBuilder</dt2>
     * <dd>builder.js (needed by Modalbox)</dd>
     * <dt>CoreJS-MultiSelect</dd>
     * <dd>Support for the MultiSelect component</dd>
     * <dt>CoreJS-Modalbox</dd>
     * <dd>JavaScript for the Modalbox component (used by the Confirm mixin, and elsewhere)</dd>
     * <dt>CoreCSS</dt>
     * <dd>Core Stylesheet</dd>
     * <dt>CoreCSS-Modalbox</dt>
     * <dd>Stylesheet used by Modalbox</dd>
     * </dl>
     * <p/>
     * If contributing additional values, you will typically want them expressly ordered <em>after</em> these
     * contributions.
     *
     * @param configuration
     */
    @Contribute(JavaScriptStack.class)
    @TapxCore
    public static void basicCoreStackElements(OrderedConfiguration<StackExtension> configuration)
    {
        configuration.add("CoreJS", new StackExtension(StackExtensionType.LIBRARY, PATH + "/tapx.js"));
        configuration.add("CoreJS-MultiSelect", new StackExtension(StackExtensionType.LIBRARY, PATH
                + "/tapx-multiselect.js"), "after:CoreJS");
        configuration.add("CoreJS-Modalbox", new StackExtension(StackExtensionType.LIBRARY, PATH + "/modalbox.js"),
                "after:CoreJS");
        configuration.add("CoreCSS", new StackExtension(StackExtensionType.STYLESHEET, PATH + "/tapx-core.css"));
        configuration
                .add("CoreCSS-Modalbox", new StackExtension(StackExtensionType.STYLESHEET, PATH + "/modalbox.css"));
        configuration.add("ScriptaculousBuilder", new StackExtension(StackExtensionType.LIBRARY,
                "${tapestry.scriptaculous}/builder.js"));
    }
}
