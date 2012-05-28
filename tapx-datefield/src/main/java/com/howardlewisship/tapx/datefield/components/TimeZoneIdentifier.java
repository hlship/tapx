// Copyright 2011, 2012 Howard M. Lewis Ship
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.howardlewisship.tapx.datefield.components;

import com.howardlewisship.tapx.datefield.services.ClientTimeZoneAnalyzer;
import com.howardlewisship.tapx.datefield.services.ClientTimeZoneData;
import com.howardlewisship.tapx.datefield.services.ClientTimeZoneTracker;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.util.TimeZone;

/**
 * Determines if {@linkplain ClientTimeZoneTracker#isClientTimeZoneIdentified() the client has
 * identified the time zone} and, if not, adds JavaScript to the page to send the time zone
 * information to the server.
 * <p/>
 * Typically, this component is placed into the application's <em>Layout</em> component (a common
 * component that defines the global layout of the application).
 * <p/>
 * After the time zone is identified, an event, "tapx:time-zone-identified" is triggered on the
 * document object. The memo of the event is a JSON object with key "timeZoneId".
 * <p/>
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
                offsetMinutes, epochMillis));

        tracker.setClientTimeZone(timeZone);

        JSONObject response = new JSONObject("timeZoneId", timeZone.getID());

        return response;
    }
}
