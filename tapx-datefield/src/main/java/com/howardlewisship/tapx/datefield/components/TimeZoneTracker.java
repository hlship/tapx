package com.howardlewisship.tapx.datefield.components;

import java.util.TimeZone;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.howardlewisship.tapx.datefield.services.ClientTimeZoneAnalyzer;
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
    @Primary
    private ClientTimeZoneAnalyzer timeZoneAnalyzer;

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
        Link link = resources.createEventLink("identifyTimeZone");

        jsSupport.addInitializerCall("identifyClientTimeZone", link.toURI());
    }

    @Log
    boolean onIdentifyTimeZone(@RequestParameter("offsetMinutes")
    int offsetMinutes, @RequestParameter("dateString")
    String dateString, @RequestParameter("epochMillis")
    long epochMillis)
    {
        TimeZone timeZone = timeZoneAnalyzer
                .extractTimeZone(dateString, offsetMinutes, epochMillis);

        tracker.setClientTimeZone(timeZone);

        return true; // stop the event
    }
}
