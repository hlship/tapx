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

package com.howardlewisship.tapx.yui.services.internal;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Flow;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.func.Worker;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentMethodAdvice;
import org.apache.tapestry5.services.ComponentMethodInvocation;
import org.apache.tapestry5.services.TransformConstants;
import org.apache.tapestry5.services.TransformMethod;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

import com.howardlewisship.tapx.yui.ImportYUI;
import com.howardlewisship.tapx.yui.YuiSymbols;

public class ImportYUIWorker implements ComponentClassTransformWorker
{
    private final AssetSource assetSource;

    private final JavascriptSupport javascriptSupport;

    private final String yuiBase;

    private final boolean productionMode;

    public ImportYUIWorker(AssetSource assetSource,

    JavascriptSupport javascriptSupport,

    @Symbol(YuiSymbols.BASE)
    String yuiBase,

    @Symbol(SymbolConstants.PRODUCTION_MODE)
    boolean productionMode)
    {
        this.assetSource = assetSource;
        this.javascriptSupport = javascriptSupport;

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
            javascriptSupport.importJavascriptLibrary(value);
        }
    };

    public void transform(ClassTransformation transformation, MutableComponentModel model)
    {
        ImportYUI annotation = transformation.getAnnotation(ImportYUI.class);

        if (annotation == null)
            return;

        Flow<Asset> assetFlow = F.flow(annotation.value()).map(expandSimpleName).map(pathToAsset);

        addAdvicetoBeginRender(transformation, assetFlow);

        model.addRenderPhase(BeginRender.class);
    }

    private void addAdvicetoBeginRender(ClassTransformation transformation, Flow<Asset> assetFlow)
    {
        TransformMethod method = transformation.getOrCreateMethod(TransformConstants.BEGIN_RENDER_SIGNATURE);

        method.addAdvice(createBeginRenderAdvice(assetFlow));
    }

    private ComponentMethodAdvice createBeginRenderAdvice(final Flow<Asset> assetFlow)
    {

        return new ComponentMethodAdvice()
        {
            public void advise(ComponentMethodInvocation invocation)
            {
                assetFlow.each(importLibrary);

                invocation.proceed();
            }
        };
    }
}
