package com.howardlewisship.tapx.datefield.services;

import java.util.TimeZone;

import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.annotations.UsesOrderedConfiguration;

/**
 * An interface used to identify the client's time zone based on information obtained from the
 * client and provided to the server in an Ajax request. This is collected as a chain-of-command
 * service identified by the {@link Primary} marker annotation.
 */
@UsesOrderedConfiguration(ClientTimeZoneAnalyzer.class)
public interface ClientTimeZoneAnalyzer
{
    /**
     * Return the identified TimeZone, or null if not identifiable from provided data.
     * 
     * @param data
     *            data extracted from the client, including optional geolocation data
     */
    TimeZone extractTimeZone(ClientTimeZoneData data);
}
