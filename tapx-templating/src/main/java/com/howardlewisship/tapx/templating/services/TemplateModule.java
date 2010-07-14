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

package com.howardlewisship.tapx.templating.services;

import com.howardlewisship.tapx.templating.internal.services.*;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.InternalSymbols;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.AssetPathConverter;
import org.apache.tapestry5.services.MarkupRendererFilter;

@SuppressWarnings("all")
public class TemplateModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(TemplateRendererSource.class, TemplateRendererSourceImpl.class);
        binder.bind(TemplateRendererFactory.class);
        binder.bind(RenderedStreamSource.class);
        binder.bind(LocationManager.class, LocationManagerImpl.class);
        binder.bind(MultipartResourceTracker.class);
        binder.bind(MailMessagePreparer.class, MailMessagePreparerImpl.class);
        binder.bind(TemplateRequestGlobals.class);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.override(SymbolConstants.APPLICATION_CATALOG,
                               String.format("classpath:${%s}/global.properties",
                                             InternalSymbols.APP_PACKAGE_PATH));
    }

    /**
     * Provides an override that prevents the default stylesheet from rendering.
     */
    public static void contributeMarkupRenderer(OrderedConfiguration<MarkupRendererFilter> configuration)
    {
        configuration.override("InjectDefaultStyleheet", null);
    }

    /**
     * Override Tapestry's default {@link org.apache.tapestry5.services.AssetPathConverter} with {@link
     * com.howardlewisship.tapx.templating.internal.services.CDNAssetPathConverter}.
     */
    public static void contributeServiceOverride(MappedConfiguration<Class, Object> configuration)
    {
        configuration.addInstance(AssetPathConverter.class, CDNAssetPathConverter.class);
    }

    public static AssetFactory decorateContextAssetFactory(@Autobuild TemplateContextAssetFactory replacement)
    {
        return replacement;
    }

    /**
     * Use the decorate functionality to entirely replace the ClasspathAssetFactory.
     */
    public static AssetFactory decorateClasspathAssetFactory(@Autobuild TemplateClasspathAssetFactory replacement)
    {
        return replacement;
    }

    public static void adviseTemplateRendererSource(MethodAdviceReceiver receiver,
                                                    @Autobuild CheckForUpdatesAdvice advice)
    {
        receiver.adviseAllMethods(advice);
    }

}
