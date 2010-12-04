package com.howardlewisship.tapx.datefield;

import com.howardlewisship.tapx.datefield.components.DateField;
import com.howardlewisship.tapx.datefield.components.TimeZoneIdentifier;
import com.howardlewisship.tapx.datefield.services.ClientTimeZoneTracker;

/**
 * Used with the {@link DateField} component to control if a TimeZone drop down list is included
 * and, if so, what its contents are. The default for the TimeZone selection is the current client
 * time zone. The client time zone may be updated if the user selects a different value using the
 * drop down list.
 * 
 * @see ClientTimeZoneTracker
 * @see TimeZoneIdentifier
 */
public enum TimeZoneVisibility
{
    /**
     * Do not display the time zone drop down list. This is the default behavior.
     */
    NONE,

    /** Display the name of the client's time zone, without allowing it to be edited. */
    DISPLAY,

    /**
     * Display a drop down list of all time zones known to the server. Caution: this can be 600+
     * values!
     */
    SELECT;
}
