package com.howardlewisship.tapx.internal.datefield.services;

import java.util.TimeZone;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.howardlewisship.tapx.datefield.services.ClientTimeZoneAnalyzer;
import com.howardlewisship.tapx.datefield.services.ClientTimeZoneData;
import com.howardlewisship.tapx.datefield.services.LatLongToTimeZoneResolver;

public class LatLongTimeZoneAnalyzer implements ClientTimeZoneAnalyzer
{
    @Inject
    private LatLongToTimeZoneResolver resolver;

    @Override
    public TimeZone extractTimeZone(ClientTimeZoneData data)
    {
        if (data.latitude != null && data.longitude != null)
            return resolver.resolveTimeZone(data.latitude, data.longitude);

        return null;
    }

}
