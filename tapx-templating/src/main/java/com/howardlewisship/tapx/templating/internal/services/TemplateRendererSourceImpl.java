// Copyright 2009, 2010 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.templating.internal.services;

import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.LocalizationSetter;
import org.apache.tapestry5.services.RequestGlobals;

import com.howardlewisship.tapx.templating.TemplateRenderer;
import com.howardlewisship.tapx.templating.services.LocationManager;
import com.howardlewisship.tapx.templating.services.TemplateRendererSource;

public class TemplateRendererSourceImpl implements TemplateRendererSource
{
    private final ComponentSource source;

    private final LocalizationSetter localizationSetter;

    private final TemplateRendererFactory factory;

    private final RequestGlobals requestGlobals;

    private final LocationManager locationManager;

    private final TemplateRequestGlobals globals;

    public TemplateRendererSourceImpl(ComponentSource source, LocalizationSetter localizationSetter,
            TemplateRendererFactory factory, RequestGlobals requestGlobals, LocationManager locationManager,
            TemplateRequestGlobals globals)
    {
        this.source = source;
        this.localizationSetter = localizationSetter;
        this.factory = factory;
        this.requestGlobals = requestGlobals;
        this.locationManager = locationManager;
        this.globals = globals;
    }

    public TemplateRenderer createRenderer(String templateName, String localeName, String location)
    {
        assert InternalUtils.isNonBlank(templateName);
        assert InternalUtils.isNonBlank(localeName);
        assert InternalUtils.isNonBlank(location);

        TemplateRequest request = new TemplateRequest();

        globals.setLocationURL(locationManager.getLocationURL(location));

        // The response comes later.

        requestGlobals.storeRequestResponse(request, null);

        localizationSetter.setNonPeristentLocaleFromLocaleName(localeName);

        Component page = source.getPage(templateName);

        return factory.createRenderer(page);
    }
}
