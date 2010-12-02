package com.howardlewisship.tapx.datefield.services;

import java.util.TimeZone;

/**
 * Given a latitude and longitude (as reported by the client-side geolocation API), resolve that
 * location to a particular time zone. The default implementation uses a web service provided by
 * http://www.geonames.org.
 */
public interface LatLongToTimeZoneResolver
{
    /**
     * Identifies the TimeZone via its location.
     * 
     * @param latitude
     * @param longitude
     * @return TimeZone if identified, or null
     */
    TimeZone resolveTimeZone(double latitude, double longitude);
}
