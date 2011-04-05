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

package com.howardlewisship.tapx.internal.datefield.services;

import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.howardlewisship.tapx.datefield.services.ClientTimeZoneAnalyzer;
import com.howardlewisship.tapx.datefield.services.ClientTimeZoneData;

/**
 * Used in the absence of latitude/longitude information to perform a best guess based purely on
 * the remaining data from the client, possibly including a time zone id extracted from the Date
 * string.
 */
public class BestGuessTimeZoneAnalyzer implements ClientTimeZoneAnalyzer
{
    private static final int ONE_MINUTE = 60 * 1000;

    private final Pattern parser = Pattern.compile("^.*\\((\\w+)\\)$");

    public TimeZone extractTimeZone(ClientTimeZoneData data)
    {
        // JS and Java are opposite on whether its the offset to or from GMT

        String[] ids = TimeZone.getAvailableIDs(-data.offsetMinutes * ONE_MINUTE);

        // Assuming the client isn't reporting garbage, this should not happen.

        if (ids.length == 0)
            return null;

        // Ok, see if we can extract something that looks like the time zone id from
        // the Date's description.

        Matcher matcher = parser.matcher(data.date);

        if (matcher.matches())
        {
            String clientId = matcher.group(1);

            for (String id : ids)
            {
                if (id.equalsIgnoreCase(clientId)) { return TimeZone.getTimeZone(id); }
            }
        }

        return TimeZone.getTimeZone(ids[0]);
    }
}
