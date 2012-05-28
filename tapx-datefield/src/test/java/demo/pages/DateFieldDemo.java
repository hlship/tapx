package demo.pages;

import com.howardlewisship.tapx.datefield.TimeZoneVisibility;
import com.howardlewisship.tapx.datefield.services.ClientTimeZoneTracker;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.PersistentLocale;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFieldDemo
{
    @Property
    @Persist
    private boolean time;

    @Property
    @Persist
    private Date date;

    @Inject
    private Locale locale;

    @Inject
    private PersistentLocale persistentLocale;

    @Property
    @Inject
    @Symbol(SymbolConstants.SUPPORTED_LOCALES)
    private String supportedLocales;

    @Property
    @Inject
    private ClientTimeZoneTracker tracker;

    @Persist
    private TimeZoneVisibility timeZoneVisibility;

    public TimeZoneVisibility getTimeZoneVisibility()
    {
        return timeZoneVisibility == null ? TimeZoneVisibility.NONE : timeZoneVisibility;
    }

    public void setTimeZoneVisibility(TimeZoneVisibility timeZoneVisibility)
    {
        this.timeZoneVisibility = timeZoneVisibility;
    }

    public DateFormat getDateFormat()
    {
        return time

                ? DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, locale)

                : DateFormat.getDateInstance(DateFormat.LONG, locale);
    }

    public String getLocaleName()
    {
        return locale.toString();
    }

    @Validate("required")
    public void setLocaleName(String localeName)
    {
        persistentLocale.set(new Locale(localeName));
    }

    @SuppressWarnings(
            {"deprecation"})
    void onActionFromSetup()
    {
        setLocaleName("en");
        date = new Date(109, Calendar.MARCH, 16);
        time = false;
    }

    public Date getMinDate()
    {
        return nowOffset(Calendar.DAY_OF_YEAR, -30);
    }

    public Date getMaxDate()
    {
        return nowOffset(Calendar.DAY_OF_YEAR, 30);
    }

    private Date nowOffset(int units, int offset)
    {
        Calendar calendar = Calendar.getInstance();

        calendar.add(units, offset);

        return calendar.getTime();
    }
}
