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

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Flow;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

import com.howardlewisship.tapx.core.StackExtension;
import com.howardlewisship.tapx.core.StackExtensionType;

public class ExtensibleJavaScriptStack implements JavaScriptStack
{
    private final AssetSource assetSource;

    private final List<Asset> libraries;

    private final List<StylesheetLink> stylesheets;

    private final String initialization;

    private final Predicate<StackExtension> by(final StackExtensionType type)
    {
        return new Predicate<StackExtension>()
        {
            @Override
            public boolean accept(StackExtension element)
            {
                return element.type == type;
            }
        };
    }

    private final Mapper<StackExtension, String> extractValue = new Mapper<StackExtension, String>()
    {
        public String map(StackExtension element)
        {
            return element.value;
        };
    };

    private final Mapper<String, Asset> stringToAsset = new Mapper<String, Asset>()
    {
        public Asset map(String value)
        {
            return assetSource.getExpandedAsset(value);
        };
    };

    private final Mapper<Asset, StylesheetLink> assetToStylesheetLink = new Mapper<Asset, StylesheetLink>()
    {
        public StylesheetLink map(Asset asset)
        {
            return new StylesheetLink(asset);
        };
    };

    public ExtensibleJavaScriptStack(AssetSource assetSource, List<StackExtension> configuration)
    {
        this.assetSource = assetSource;

        Flow<StackExtension> extensions = F.flow(configuration);

        libraries = extensions.filter(by(StackExtensionType.LIBRARY)).map(extractValue).map(stringToAsset).toList();

        stylesheets = extensions.filter(by(StackExtensionType.STYLESHEET)).map(extractValue).map(stringToAsset)
                .map(assetToStylesheetLink).toList();

        List<String> initializations = extensions.filter(by(StackExtensionType.INITIALIZATION)).map(extractValue)
                .toList();

        initialization = initializations.isEmpty() ? null : InternalUtils.join(initializations, "\n");
    }

    public List<String> getStacks()
    {
        return Collections.emptyList();
    }

    public List<Asset> getJavaScriptLibraries()
    {
        return libraries;
    }

    public List<StylesheetLink> getStylesheets()
    {
        return stylesheets;
    }

    public String getInitialization()
    {
        return initialization;
    }

}
