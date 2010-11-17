// Copyright 2010 [ORG]
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

package com.howardlewisship.tapx.core.internal.services;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

public class TapxCoreStack implements JavaScriptStack
{
    private final List<Asset> libraries;

    public TapxCoreStack(AssetSource assetSource)
    {
        Asset tapx = assetSource.getClasspathAsset("com/howardlewisship/tapx/core/tapx.js");

        libraries = Collections.singletonList(tapx);
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
        return Collections.emptyList();
    }

    public String getInitialization()
    {
        return null;
    }

}
