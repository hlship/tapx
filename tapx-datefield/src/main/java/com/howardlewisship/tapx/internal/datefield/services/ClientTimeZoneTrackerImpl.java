// Copyright 2011 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.internal.datefield.services;

import java.util.TimeZone;

import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

import com.howardlewisship.tapx.datefield.services.ClientTimeZoneTracker;

@Scope(ScopeConstants.PERTHREAD)
public class ClientTimeZoneTrackerImpl implements ClientTimeZoneTracker
{
    private static final String COOKIE_NAME = "tapx-timezone";

    private static final String ATTRIBUTE_NAME = "tapx-datefield.timezone-id";

    private final Cookies cookies;

    private final Request request;

    private TimeZone timeZone;

    private boolean identified;

    public ClientTimeZoneTrackerImpl(Cookies cookies, Request request)
    {
        this.cookies = cookies;
        this.request = request;

        setupTimeZone();
    }

    private void setupTimeZone()
    {
        timeZone = readTimeZoneFromSession();

        if (timeZone == null)
            timeZone = readTimeZoneFromCookie();

        if (timeZone == null)
            timeZone = TimeZone.getDefault();
        else
            identified = true;
    }

    private TimeZone readTimeZoneFromSession()
    {
        Session session = request.getSession(false);

        if (session != null)
        {
            String id = (String) session.getAttribute(ATTRIBUTE_NAME);

            if (id != null)
                return TimeZone.getTimeZone(id);
        }

        return null;
    }

    private TimeZone readTimeZoneFromCookie()
    {
        String id = cookies.readCookieValue(COOKIE_NAME);

        return id == null ? null : TimeZone.getTimeZone(id);
    }

    public boolean isClientTimeZoneIdentified()
    {
        return identified;
    }

    public TimeZone getClientTimeZone()
    {
        return timeZone;
    }

    public void setClientTimeZone(TimeZone timeZone)
    {
        assert timeZone != null;

        identified = true;

        if (timeZone == this.timeZone)
            return;

        this.timeZone = timeZone;

        cookies.writeCookieValue(COOKIE_NAME, timeZone.getID());

        // Write to the Session, if it exists, in case the client doesn't support cookies.

        Session session = request.getSession(false);

        if (session != null)
            session.setAttribute(ATTRIBUTE_NAME, timeZone.getID());

        // Worst case: no session yet AND client doesn't support cookies. That means we'll likely
        // keep tracking the time zone (on the client) and updating (here on the server) until
        // a session gets created.
    }
}
