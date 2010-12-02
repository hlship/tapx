package com.howardlewisship.tapx.datefield.services;

/**
 * Contains data obtained, via JavaScript, from the client.
 */
public class ClientTimeZoneData
{
    public final String date;

    public final int offsetMinutes;

    public final long epochMillis;

    public final Double latitude, longitude;

    /**
     * @param date
     *            the client-side date as a string, formatted by the client using toString()
     * @param offsetMinutes
     *            offset, in minutes, from GMT
     * @param epochMillis
     *            client reported time in milliseconds sinch the epoch (Jan 1 1970)
     * @param latitude
     *            reported latitude value from client (via geolocation), may be null
     * @param longitude
     *            reported longitude value form client (via geolocation), may be null
     */
    public ClientTimeZoneData(String date, int offsetMinutes, long epochMillis, Double latitude,
            Double longitude)
    {
        this.date = date;
        this.offsetMinutes = offsetMinutes;
        this.epochMillis = epochMillis;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
