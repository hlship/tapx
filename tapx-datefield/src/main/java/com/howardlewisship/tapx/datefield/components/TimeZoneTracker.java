package com.howardlewisship.tapx.datefield.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.howardlewisship.tapx.datefield.services.ClientTimeZoneTracker;

/**
 * Determines if {@linkplain ClientTimeZoneTracker#isClientTimeZoneIdentified() the client has
 * identified the time zone} and, if not, adds JavaScript to the page to sent the time zone
 * information to the server.
 * 
 * @see ClientTimeZoneTracker
 */
public class TimeZoneTracker
{
    @Inject
    private ClientTimeZoneTracker tracker;

    @Inject
    private JavaScriptSupport jsSupport;

    @Inject
    private ComponentResources resources;

    boolean setupRender()
    {
        return !tracker.isClientTimeZoneIdentified();
    }

    @Import(library = "time-zone-tracker.js")
    void beginRender()
    {
        Link link = resources.createEventLink("dataReceived");

        jsSupport.addInitializerCall("identifyClientTimeZone", link.toURI());
    }

    @Log
    void onDataReceived(@RequestParameter("offsetMinutes")
    int offsetMinutes, @RequestParameter("dateString")
    String dateString, @RequestParameter("epochMillis")
    long epochMillis)
    {

    }
}
