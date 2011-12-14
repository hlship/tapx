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
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Flow;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.func.Worker;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.TransformConstants;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

public class ImportYUIWorker implements ComponentClassTransformWorker2
{
    private final Resource yuiBaseFolder;

    private final AssetSource assetSource;

    private final JavaScriptSupport javaScriptSupport;

    private final boolean productionMode;

    public ImportYUIWorker(AssetSource assetSource,

                           JavaScriptSupport javaScriptSupport,

                           @Symbol(YuiSymbols.BASE)
                           String yuiBase,

                           @Symbol(SymbolConstants.PRODUCTION_MODE)
                           boolean productionMode)
    {
        this.assetSource = assetSource;
        this.javaScriptSupport = javaScriptSupport;

        yuiBaseFolder = assetSource.getExpandedAsset(yuiBase + "/build").getResource();

        this.productionMode = productionMode;
    }

    private final Mapper<String, String> expandSimpleName = new Mapper<String, String>()
    {
        public String map(String name)
        {
            return name.contains("/") ? name : name + "/" + name;
        }
    };

    private final Mapper<String, Resource> nameToResource = new Mapper<String, Resource>()
    {
        public Resource map(String name)
        {
            if (productionMode)
            {
                Resource minimized = yuiBaseFolder.forFile("build/" + name + "-min.js");

                if (minimized.exists())
                {
                    return minimized;
                }
            }

            return yuiBaseFolder.forFile("build/" + name + ".js");
        }
    };

    private final Mapper<Resource, Asset> resourceToAsset = new Mapper<Resource, Asset>()
    {
        public Asset map(Resource element)
        {
            // A bit roundabout, but AssetSource doesn't currently give us the necessary options.
            return assetSource.getUnlocalizedAsset(element.getPath());
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

        Flow<Asset> assetFlow = F.flow(annotation.value()).map(expandSimpleName).map(nameToResource).map(resourceToAsset);

        addAdviceToBeginRender(plasticClass, assetFlow);

        model.addRenderPhase(BeginRender.class);
    }

    private void addAdviceToBeginRender(PlasticClass transformation, Flow<Asset> assetFlow)
    {
        transformation.introduceMethod(TransformConstants.BEGIN_RENDER_DESCRIPTION).addAdvice(
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
