package com.howardlewisship.tapx.internal.datefield.services;

import java.util.TimeZone;

import com.howardlewisship.tapx.datefield.services.ClientTimeZoneAnalyzer;
import com.howardlewisship.tapx.datefield.services.ClientTimeZoneData;

public class SystemTimeZoneAnalyzer implements ClientTimeZoneAnalyzer
{
    @Override
    public TimeZone extractTimeZone(ClientTimeZoneData data)
    {
        return TimeZone.getDefault();
    }

}
