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

package com.howardlewisship.tapx.yui.services.internal;

import com.howardlewisship.tapx.yui.ImportYUI;
import com.howardlewisship.tapx.yui.YuiSymbols;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Flow;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.func.Worker;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.*;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.runtime.Event;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

public class ImportYUIWorker implements ComponentClassTransformWorker2
{
    private final AssetSource assetSource;

    private final JavaScriptSupport javaScriptSupport;

    private final String yuiBase;

    private final boolean productionMode;

    public static final MethodDescription BEGIN_RENDER_DESCRIPTION = PlasticUtils.getMethodDescription(Component.class,
            "beginRender", MarkupWriter.class, Event.class);

    public ImportYUIWorker(AssetSource assetSource,

                           JavaScriptSupport javaScriptSupport,

                           @Symbol(YuiSymbols.BASE)
                           String yuiBase,

                           @Symbol(SymbolConstants.PRODUCTION_MODE)
                           boolean productionMode)
    {
        this.assetSource = assetSource;
        this.javaScriptSupport = javaScriptSupport;

        this.yuiBase = yuiBase;
        this.productionMode = productionMode;
    }

    private final Mapper<String, String> expandSimpleName = new Mapper<String, String>()
    {
        public String map(String name)
        {
            String relativePath = name.contains("/") ? name : name + "/" + name;

            return yuiBase + "/build/" + relativePath;
        }
    };

    private final Mapper<String, Asset> pathToAsset = new Mapper<String, Asset>()
    {
        public Asset map(String path)
        {

            if (!productionMode)
            {
                String minPath = path + "-min.js";

                Asset asset = assetSource.getExpandedAsset(minPath);

                if (asset.getResource().exists())
                    return asset;
            }

            return assetSource.getExpandedAsset(path + ".js");
        }
    };

    private final Worker<Asset> importLibrary = new Worker<Asset>()
    {
        public void work(Asset value)
        {
            javaScriptSupport.importJavaScriptLibrary(value);
        }
    };

    public void transform(PlasticClass plasticClass, TransformationSupport support, MutableComponentModel model)
    {
        ImportYUI annotation = plasticClass.getAnnotation(ImportYUI.class);

        if (annotation == null)
        {
            return;
        }

        Flow<Asset> assetFlow = F.flow(annotation.value()).map(expandSimpleName).map(pathToAsset);

        addAdvicetoBeginRender(plasticClass, assetFlow);

        model.addRenderPhase(BeginRender.class);
    }

    private void addAdvicetoBeginRender(PlasticClass transformation, Flow<Asset> assetFlow)
    {
        transformation.introduceMethod(BEGIN_RENDER_DESCRIPTION).addAdvice(
                createBeginRenderAdvice(assetFlow));
    }

    private MethodAdvice createBeginRenderAdvice(final Flow<Asset> assetFlow)
    {

        return new MethodAdvice()
        {
            public void advise(MethodInvocation invocation)
            {
                assetFlow.each(importLibrary);

                invocation.proceed();
            }
        };
    }
}
