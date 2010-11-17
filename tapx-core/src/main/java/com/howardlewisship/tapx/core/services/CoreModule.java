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

package com.howardlewisship.tapx.core.services;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.UpdateListenerHub;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;

import com.howardlewisship.tapx.core.CoreSymbols;
import com.howardlewisship.tapx.core.dynamic.DynamicTemplate;
import com.howardlewisship.tapx.core.dynamic.DynamicTemplateParser;
import com.howardlewisship.tapx.core.internal.dynamic.DynamicTemplateParserImpl;
import com.howardlewisship.tapx.core.internal.services.KaptchaProducerImpl;
import com.howardlewisship.tapx.core.internal.services.TapxCoreStack;

public class CoreModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(KaptchaProducer.class, KaptchaProducerImpl.class);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(CoreSymbols.TEST_MODE, "false");
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("tapx", "com.howardlewisship.tapx.core"));
    }

    public static void contributeComponentMessagesSource(OrderedConfiguration<Resource> configuration,
            @Value("classpath:com/howardlewisship/tapx/core/tapx-core.properties")
            Resource coreCatalog)
    {
        configuration.add("TapxCore", coreCatalog, "before:AppCatalog");
    }

    @Contribute(JavaScriptStackSource.class)
    public static void provideTapxCoreStack(MappedConfiguration<String, JavaScriptStack> configuration)
    {
        configuration.addInstance("tapx-core", TapxCoreStack.class);
    }

    /**
     * Contributes:
     * <ul>
     * <li>{@link Resource} to {@link DynamicTemplate}</li>
     * <li>{@link Asset} to {@link Resource}</li>
     * </ul>
     */
    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration,

    @Local
    final DynamicTemplateParser parser)
    {
        configuration.add(CoercionTuple.create(Resource.class, DynamicTemplate.class,
                new Coercion<Resource, DynamicTemplate>()
                {
                    public DynamicTemplate coerce(Resource input)
                    {
                        return parser.parseTemplate(input);
                    }
                }));

        configuration.add(CoercionTuple.create(Asset.class, Resource.class, new Coercion<Asset, Resource>()
        {
            public Resource coerce(Asset input)
            {
                return input.getResource();
            }
        }));
    }

    public static DynamicTemplateParser buildDynamicTemplateParser(@Autobuild
    DynamicTemplateParserImpl service, UpdateListenerHub updateListenerHub)
    {
        updateListenerHub.addUpdateListener(service);

        return service;
    }
}
