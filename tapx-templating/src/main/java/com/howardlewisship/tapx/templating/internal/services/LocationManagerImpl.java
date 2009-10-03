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

import com.howardlewisship.tapx.templating.services.LocationManager;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.SymbolSource;

import java.util.Map;

public class LocationManagerImpl implements LocationManager
{
    private final Map<String, String> locationMap = CollectionFactory.newCaseInsensitiveMap();

    public LocationManagerImpl(Map<String, String> configuration, SymbolSource symbolSource)
    {
        for (Map.Entry<String, String> e : configuration.entrySet())
        {
            String URL = symbolSource.expandSymbols(e.getValue());

            if (!URL.endsWith("/"))
                URL = URL + "/";

            locationMap.put(e.getKey(), URL);
        }
    }

    public String getLocationURL(String locationName)
    {
        String result = locationMap.get(locationName);

        if (result == null)
            throw new IllegalArgumentException(
                    String.format("Location '%s' is not defined. Defined location names: %s.",
                                  locationName,
                                  InternalUtils.joinSorted(locationMap.keySet())));
        return result;
    }

}
