package com.howardlewisship.tapx.datefield.services;

import java.util.TimeZone;

import com.howardlewisship.tapx.datefield.components.TimeZoneIdentifier;

/**
 * Tracks the {@link TimeZone} identified for the client for the current request. This information
 * is obtained via the {@link TimeZoneIdentifier} component, and is ultimately stored as an HTTP
 * Cookie.
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

    /**
     * Invoked when the client time zone is identified; this will update the persistent storage
     * (by default, a cookie) used to track a particular client's time zone as necessary
     * 
     * @param timeZone
     *            identified time zone
     */
    void setClientTimeZone(TimeZone timeZone);
}
