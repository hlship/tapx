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

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.internal.services.AbstractAsset;
import org.apache.tapestry5.internal.services.ContextResource;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.AssetPathConverter;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Request;

/**
 * A replacement for Tapestry's built-in Context AssetFactory that understands about locations; each location is a
 * content delivery server.  The assumption is that any file under the local context is mirrored at some number of other
 * locations. Each named location is mapped to a URL and the path from the web root in the local context can be used
 * with the location's URL.
 * <p/>
 * This code is reliant upon the {@link com.howardlewisship.tapx.templating.internal.services.InternalConstants#LOCATION_URL_ATTRIBUTE}
 * request attribute being set (by {@link com.howardlewisship.tapx.templating.internal.services.TemplateRendererSourceImpl}).
 */
public class TemplateContextAssetFactory implements AssetFactory
{
    private final Request request;

    private final Resource rootResource;

    private final AssetPathConverter converter;

    public TemplateContextAssetFactory(Request request, Context context, AssetPathConverter converter)
    {
        this.request = request;
        this.converter = converter;

        rootResource = new ContextResource(context, "/");
    }

    public Asset createAsset(final Resource resource)
    {
        final String path = request.getContextPath() + "/" + resource.getPath();

        return new AbstractAsset(false)
        {
            public Resource getResource()
            {
                return resource;
            }

            public String toClientURL()
            {
                return converter.convertAssetPath(path);
            }
        };
    }

    /**
     * Returns the root {@link org.apache.tapestry5.internal.services.ContextResource}.
     */
    public Resource getRootResource()
    {
        return rootResource;
    }
}
