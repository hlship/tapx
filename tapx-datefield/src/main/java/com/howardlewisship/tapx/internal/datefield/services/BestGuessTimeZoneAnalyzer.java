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

    @Override
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
