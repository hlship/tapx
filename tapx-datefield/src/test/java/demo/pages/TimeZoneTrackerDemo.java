package demo.pages;

import java.util.TimeZone;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.howardlewisship.tapx.datefield.services.ClientTimeZoneTracker;

public class TimeZoneTrackerDemo
{
    @Inject
    private ClientTimeZoneTracker tracker;

    public boolean isIdentified()
    {
        return tracker.isClientTimeZoneIdentified();
    }

    public TimeZone getTimeZone()
    {
        return tracker.getClientTimeZone();
    }
}
