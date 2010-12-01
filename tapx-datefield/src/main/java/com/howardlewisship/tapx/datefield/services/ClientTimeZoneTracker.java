package com.howardlewisship.tapx.datefield.services;

import java.util.TimeZone;

/**
 * Tracks the {@link TimeZone} identified for the client for the current request.
 */
public interface ClientTimeZoneTracker
{
    /**
     * Has the client time zone been correctly identified in this, or a previous, request?
     * 
     * @return true if specific client time zone has been identified
     */

    boolean isClientTimeZoneIdentified();

    /**
     * Returns the time zone in effect for the client; preferably the time zone identified by the
     * client, but alternately, the system default time zone.
     * 
     * @return effective TimeZone
     */
    TimeZone getClientTimeZone();
}
