package com.howardlewisship.tapx.internal.datefield.services;

import java.util.TimeZone;

import com.howardlewisship.tapx.datefield.services.ClientTimeZoneAnalyzer;

public class SystemTimeZoneAnalyzer implements ClientTimeZoneAnalyzer
{
    @Override
    public TimeZone extractTimeZone(String dateString, int offsetMinutes, long epochMillis)
    {
        return TimeZone.getDefault();
    }

}
