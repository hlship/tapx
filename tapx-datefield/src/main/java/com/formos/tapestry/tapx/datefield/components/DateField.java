// Copyright 2009 Howard M. Lewis Ship
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.formos.tapestry.tapx.datefield.components;

import com.formos.tapestry.tapx.datefield.DateFieldSymbols;
import com.formos.tapestry.tapx.datefield.services.DateFieldFormatConverter;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.ClientBehaviorSupport;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.Request;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;


/**
 * A replacement for Tapestry's built-in DateField, built around the <a href="http://www.dynarch.com/projects/calendar/">Dynarch
 * JSCalendar Widget</a>.  This is a highly functional calendar, but is distributed as LGPL and so can't be built
 * directly into Tapestry.
 */
public class DateField extends AbstractField
{
    /**
     * A request attribute used to ensure that the JavaScript for the component is only generated once (because there's
     * so many files to calculate).
     */
    private static final String SCRIPTS_INCLUDED = "com.formos.tapestry.tapx.datefield.script-loaded";

    /**
     * The value parameter of a DateField must be a {@link java.util.Date}.
     */
    @Parameter(required = true, principal = true, autoconnect = true)
    private Date value;

    /**
     * The format used to format <em>and parse</em> dates. This is typically specified as a string which is coerced to a
     * DateFormat. You should be aware that using a date format with a two digit year is problematic: Java (not
     * Tapestry) may get confused about the century.
     */
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private DateFormat format;

    /**
     * If true, then  the text field will be hidden, and only the icon for the date picker will be visible. The default
     * is false.
     */
    @Parameter
    private boolean hideTextField;

    /**
     * The object that will perform input validation (which occurs after translation). The translate binding prefix is
     * generally used to provide this object in a declarative fashion.
     */
    @Parameter(defaultPrefix = BindingConstants.VALIDATE)
    @SuppressWarnings("unchecked")
    private FieldValidator<Object> validate;

    @Parameter(defaultPrefix = BindingConstants.ASSET, value = "datefield.gif")
    private Asset icon;

    @Inject
    private ComponentDefaultProvider defaultProvider;

    @Inject
    private ComponentResources resources;

    @Inject
    private Request request;

    @Inject
    private Locale locale;

    @Inject
    private Messages messages;

    @Inject
    private AssetSource assetSource;

    @Environmental
    private ValidationTracker tracker;

    @Environmental
    private RenderSupport renderSupport;

    @Environmental
    private ClientBehaviorSupport clientBehaviorSupport;

    @Inject
    @Symbol(DateFieldSymbols.JSCALENDAR_PATH)
    private String calendarPath;

    @Inject
    @Symbol(DateFieldSymbols.SKIN)
    private String skin;

    @Inject
    @Symbol(DateFieldSymbols.THEME)
    private String theme;

    @Inject
    @Path("tapx-datefield.js")
    private Asset datefieldLibrary;

    @Inject
    private FieldValidationSupport fieldValidationSupport;

    @Inject
    private DateFieldFormatConverter formatConverter;

    // There's support for some language variations as well, but didn't want to get into that.
    private static final Set<String> SUPPORTED_LANGUAGES = CollectionFactory.newSet(TapestryInternalUtils.splitAtCommas(
            "af,al,big,big5,br,ca,cs,da,de,du,el,en,es,eu,fi,fr,hr,hu,it,jp,ko,lt,lv,nl,no,pl,pt,ro,ru,si,sk,sp,sr,sv,tr,zh"));


    /**
     * Computes a default value for the "validate" parameter using {@link org.apache.tapestry5.services.ComponentDefaultProvider}.
     */
    final Binding defaultValidate()
    {
        return defaultProvider.defaultValidatorBinding("value", resources);
    }

    DateFormat defaultFormat()
    {
        DateFormat shortDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);

        if (shortDateFormat instanceof SimpleDateFormat)
        {
            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) shortDateFormat;

            String pattern = simpleDateFormat.toPattern();

            String revised = pattern.replaceAll("([^y])yy$", "$1yyyy");

            return new SimpleDateFormat(revised);
        }

        return shortDateFormat;
    }

    public void beginRender(MarkupWriter writer)
    {
        String value = tracker.getInput(this);

        if (value == null)
            value = formatCurrentValue();

        String clientId = getClientId();
        String triggerId = clientId + "-trigger";

        writer.element("input",

                       "type", hideTextField ? "hidden" : "text",

                       "name", getControlName(),

                       "id", clientId,

                       "value", value);

        if (isDisabled()) writer.attributes("disabled", "disabled");

        validate.render(writer);

        resources.renderInformalParameters(writer);

        decorateInsideField();

        writer.end();

        // Now the trigger icon.

        writer.element("img",

                       "id", triggerId,

                       "class", "t-calendar-trigger",

                       "src", icon.toClientURL(),

                       "alt", "[Show]");
        writer.end(); // img

        if (request.getAttribute(SCRIPTS_INCLUDED) == null)
        {
            String language = locale.getLanguage();

            String supported = SUPPORTED_LANGUAGES.contains(language) ? language : "en";

            // Can't use annotations when so much is calculated dynamically.


            renderSupport.addClasspathScriptLink(calendarPath + "/calendar.js",
                                                 calendarPath + "/calendar-setup.js",
                                                 calendarPath + "/lang/calendar-" + supported + ".js");


            renderSupport.addScriptLink(datefieldLibrary);

            // All calendars on the page will share the same theme and skin, because that theme/skin is
            // implemented via including the correct CSS style sheets (and can't be selected per
            // widget, which would be visually confusing anyway).)

            renderSupport.addStylesheetLink(findCalendarAsset("skins/" + skin + "/theme.css"), null);
            renderSupport.addStylesheetLink(findCalendarAsset("calendar-" + theme + ".css"), null);

            request.setAttribute(SCRIPTS_INCLUDED, true);
        }

        String clientFormat = formatConverter.convertToClient(format);

        renderSupport.addInit("tapxDateField", clientId, clientFormat);
    }


    private Asset findCalendarAsset(String path)
    {
        return assetSource.getAsset(null, calendarPath + "/" + path, null);
    }


    private String formatCurrentValue()
    {
        if (value == null) return "";

        return format.format(value);
    }

    @Override
    protected void processSubmission(String elementName)
    {
        String value = request.getParameter(elementName);

        tracker.recordInput(this, value);

        Date parsedValue = null;

        try
        {
            if (InternalUtils.isNonBlank(value))
                parsedValue = format.parse(value);
        }
        catch (ParseException ex)
        {
            tracker.recordError(this, messages.format("date-value-not-parseable", value));
            return;
        }

        try
        {
            fieldValidationSupport.validate(parsedValue, resources, validate);

            this.value = parsedValue;
        }
        catch (ValidationException ex)
        {
            tracker.recordError(this, ex.getMessage());
        }
    }

}
