// Copyright 2010 The Apache Software Foundation
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

package com.howardlewisship.tapx.datefield.services;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

import com.howardlewisship.tapx.datefield.DateFieldSymbols;

public class DateFieldStack implements JavaScriptStack
{
    // There's support for some language variations as well, but didn't want to get into that.
    private static final Set<String> SUPPORTED_LANGUAGES = CollectionFactory
            .newSet(TapestryInternalUtils
                    .splitAtCommas("af,al,big,big5,br,ca,cs,da,de,du,el,en,es,eu,fi,fr,hr,hu,it,jp,ko,lt,lv,nl,no,pl,pt,ro,ru,si,sk,sp,sr,sv,tr,zh"));

    private final ThreadLocale threadLocale;

    private final AssetSource assetSource;

    private final List<StylesheetLink> stylesheets;

    public DateFieldStack(ThreadLocale threadLocale, AssetSource assetSource,

    @Symbol(DateFieldSymbols.SKIN)
    String skin,

    @Symbol(DateFieldSymbols.THEME)
    String theme)
    {
        this.threadLocale = threadLocale;
        this.assetSource = assetSource;

        Asset componentStylesheet = assetSource
                .getExpandedAsset("com/howardlewisship/tapx/datefield/tapx-datefield.css");

        stylesheets = CollectionFactory.newList(new StylesheetLink(skinAsset(skin, theme)),
                new StylesheetLink(componentStylesheet));
    }

    private Asset skinAsset(String skin, String theme)
    {
        String path = theme.equals("") ? "skins/" + skin + "/theme.css" : "calendar-" + theme
                + ".css";

        return assetSource.getExpandedAsset("${tapx.jscalendar.path}/" + path);
    }

    private List<Asset> toAssets(String... paths)
    {
        return F.flow(paths).map(new Mapper<String, Asset>()
        {
            public Asset map(String path)
            {
                return assetSource.getExpandedAsset(path);
            };
        }).toList();
    }

    public String getInitialization()
    {
        return null;
    }

    public List<Asset> getJavaScriptLibraries()
    {
        String language = threadLocale.getLocale().getLanguage();

        String supported = SUPPORTED_LANGUAGES.contains(language) ? language : "en";

        return toAssets("${tapx.jscalendar.path}/calendar.js",
                "${tapx.jscalendar.path}/calendar-setup.js",
                "${tapx.jscalendar.path}/lang/calendar-" + supported + ".js",
                "com/howardlewisship/tapx/datefield/tapx-datefield.js");
    }

    public List<StylesheetLink> getStylesheets()
    {
        return stylesheets;
    }

    public List<String> getStacks()
    {
        return Collections.emptyList();
    }

}
