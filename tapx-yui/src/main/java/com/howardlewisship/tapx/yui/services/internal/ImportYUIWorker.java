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
import org.apache.tapestry5.ioc.annotations.Inject;
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

    private final String suffix;

    public ImportYUIWorker(AssetSource assetSource,

    JavascriptSupport javascriptSupport,

    @Inject
    @Symbol(YuiSymbols.BASE)
    String yuiBase,

    @Symbol(SymbolConstants.PRODUCTION_MODE)
    boolean productionMode)
    {
        this.assetSource = assetSource;
        this.javascriptSupport = javascriptSupport;

        this.yuiBase = yuiBase;

        this.suffix = productionMode ? "-min.js" : ".js";
    }

    public void transform(ClassTransformation transformation, MutableComponentModel model)
    {
        ImportYUI annotation = transformation.getAnnotation(ImportYUI.class);

        if (annotation == null)
            return;

        String[] paths = expand(annotation.value());

        addAdvicetoBeginRender(transformation, paths);

        model.addRenderPhase(BeginRender.class);
    }

    private void addAdvicetoBeginRender(ClassTransformation transformation, String[] paths)
    {
        TransformMethod method = transformation.getOrCreateMethod(TransformConstants.BEGIN_RENDER_SIGNATURE);

        method.addAdvice(createBeginRenderAdvice(paths));
    }

    private ComponentMethodAdvice createBeginRenderAdvice(final String[] paths)
    {
        return new ComponentMethodAdvice()
        {
            public void advise(ComponentMethodInvocation invocation)
            {
                importJavascriptLibrariesForPaths(paths);

                invocation.proceed();
            }
        };
    }

    private String[] expand(String[] names)
    {
        String[] result = new String[names.length];

        for (int i = 0; i < names.length; i++)
        {
            result[i] = expand(names[i]);
        }

        return result;
    }

    private String expand(String name)
    {
        String relativePath = name.contains("/") ? name : name + "/" + name;

        return yuiBase + "/build/" + relativePath + suffix;
    }

    private void importJavascriptLibrariesForPaths(final String[] paths)
    {
        for (String path : paths)
        {
            importJavascriptLibraryForPath(path);
        }
    }

    private void importJavascriptLibraryForPath(String path)
    {
        Asset pathAsset = assetSource.getAsset(null, path, null);

        javascriptSupport.importJavascriptLibrary(pathAsset);
    }

}
