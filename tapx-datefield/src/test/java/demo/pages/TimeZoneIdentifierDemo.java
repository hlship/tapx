package demo.pages;

import java.util.Date;
import java.util.TimeZone;

import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Flow;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.howardlewisship.tapx.datefield.services.ClientTimeZoneTracker;

public class TimeZoneIdentifierDemo
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

    public Date getCurrentTime()
    {
        return new Date();
    }

    public Flow<TimeZone> getTimeZones()
    {
        return F.flow(TimeZone.getAvailableIDs()).map(new Mapper<String, TimeZone>()
        {
            public TimeZone map(String id)
            {
                return TimeZone.getTimeZone(id);
            }
        });
    }
}
