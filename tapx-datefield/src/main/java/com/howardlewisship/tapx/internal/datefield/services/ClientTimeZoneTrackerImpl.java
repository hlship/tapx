package com.howardlewisship.tapx.internal.datefield.services;

import java.util.TimeZone;

import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.services.Cookies;

import com.howardlewisship.tapx.datefield.services.ClientTimeZoneTracker;

@Scope(ScopeConstants.PERTHREAD)
public class ClientTimeZoneTrackerImpl implements ClientTimeZoneTracker
{
    private static final String COOKIE_NAME = "tapx-timezone";

    private final Cookies cookies;

    private TimeZone timeZone;

    private boolean identified;

    public ClientTimeZoneTrackerImpl(Cookies cookies)
    {
        this.cookies = cookies;

        readTimeZoneFromCookie();
    }

    private void readTimeZoneFromCookie()
    {
        String id = cookies.readCookieValue(COOKIE_NAME);

        if (id == null)
        {
            timeZone = TimeZone.getDefault();
            return;
        }

        identified = true;

        timeZone = TimeZone.getTimeZone(id);
    }

    @Override
    public boolean isClientTimeZoneIdentified()
    {
        return identified;
    }

    @Override
    public TimeZone getClientTimeZone()
    {
        return timeZone;
    }

    @Override
    public void setClientTimeZone(TimeZone timeZone)
    {
        assert timeZone != null;

        this.timeZone = timeZone;
        identified = true;

        cookies.writeCookieValue(COOKIE_NAME, timeZone.getID());
    }
}
