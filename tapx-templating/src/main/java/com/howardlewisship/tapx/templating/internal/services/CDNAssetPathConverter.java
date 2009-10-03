// Copyright 2009 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.templating.internal.services;

import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.services.AssetPathConverter;

public class CDNAssetPathConverter implements AssetPathConverter
{
    private final TemplateRequestGlobals globals;


    public CDNAssetPathConverter(@Local TemplateRequestGlobals globals)
    {
        this.globals = globals;
    }

    /**
     * Returns false; these paths will vary from request to request based on the location value supplied to {@link
     * com.howardlewisship.tapx.templating.services.TemplateRendererSource#createRenderer(String, String, String)}.
     *
     * @return false
     */
    public boolean isInvariant()
    {
        return false;
    }

    public String convertAssetPath(String assetPath)
    {
        // Strip the leading slash.
        return globals.getLocationURL() + assetPath.substring(1);
    }
}
