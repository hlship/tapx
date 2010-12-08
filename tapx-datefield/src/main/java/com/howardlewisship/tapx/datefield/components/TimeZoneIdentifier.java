package com.howardlewisship.tapx.datefield.components;

import java.util.TimeZone;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.howardlewisship.tapx.datefield.services.ClientTimeZoneAnalyzer;
import com.howardlewisship.tapx.datefield.services.ClientTimeZoneData;
import com.howardlewisship.tapx.datefield.services.ClientTimeZoneTracker;

/**
 * Determines if {@linkplain ClientTimeZoneTracker#isClientTimeZoneIdentified() the client has
 * identified the time zone} and, if not, adds JavaScript to the page to send the time zone
 * information to the server. The JavaScript will ask for access to geolocation data available on
 * the
 * client (this works in Firefox and Chrome) and will report the client's latitude and longitude.
 * If geolocation data is not available, other date information is used to determine the best
 * matching TimeZone.
 * <p>
 * Typically, this component is placed into the application's <em>Layout</em> component (a common
 * component that defines the global layout of the application).
 * <p>
 * After the time zone is identified, an event, "tapx:time-zone-identified" is triggered on the
 * document object. The memo of the event is a JSON object with key "timeZoneId".
 * <p>
 * TODO: Seems like collecting this information is just part of a larger cycle of determining
 * exactly what's running on the client ... imagine if we knew exactly what browser was out there,
 * and what features it supported, and could customize to that!
 * 
 * @see ClientTimeZoneTracker
 * @see ClientTimeZoneAnalyzer
 */
public class TimeZoneIdentifier
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

    @Inject
    private Request request;

    boolean setupRender()
    {
        return !tracker.isClientTimeZoneIdentified();
    }

    /**
     * TODO: Perhaps we should also render a floating <div> that will explain to the user that
     * geolocation data is being requested to determine the time zone.
     */
    @Import(library = "time-zone-identifier.js")
    void beginRender()
    {
        Link link = resources.createEventLink("identifyTimeZone");

        jsSupport.addInitializerCall("identifyClientTimeZone", link.toURI());
    }

    Object onIdentifyTimeZone(@RequestParameter("offsetMinutes")
    int offsetMinutes, @RequestParameter("dateString")
    String dateString, @RequestParameter("epochMillis")
    long epochMillis)
    {
        TimeZone timeZone = timeZoneAnalyzer.extractTimeZone(new ClientTimeZoneData(dateString,
                offsetMinutes, epochMillis, getDouble("latitude"), getDouble("longitude")));

        tracker.setClientTimeZone(timeZone);

        JSONObject response = new JSONObject("timeZoneId", timeZone.getID());

        return response;
    }

    private Double getDouble(String name)
    {
        String value = request.getParameter(name);

        return InternalUtils.isBlank(value) ? null : new Double(value);
    }
}
