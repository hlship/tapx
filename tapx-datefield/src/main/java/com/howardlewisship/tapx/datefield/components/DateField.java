// Copyright 2009, 2010 Howard M. Lewis Ship
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

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.components.BeanEditor;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.ClientBehaviorSupport;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.Request;

import com.howardlewisship.tapx.datefield.DateFieldSymbols;
import com.howardlewisship.tapx.datefield.TimeSignificant;
import com.howardlewisship.tapx.datefield.services.DateFieldFormatConverter;

/**
 * A replacement for Tapestry's built-in DateField, built around the <a
 * href="http://www.dynarch.com/projects/calendar/old/">Dynarch
 * JSCalendar Widget</a>. This is a highly functional calendar, but is distributed as LGPL and so can't be built
 * directly into Tapestry.
 */
public class DateField extends AbstractField
{
    /**
     * A request attribute used to ensure that the JavaScript for the component is only generated once (because there's
     * so many files to calculate).
     */
    private static final String SCRIPTS_INCLUDED = "com.howardlewisship.tapx.datefield.script-loaded";

    /**
     * The value parameter of a DateField must be a {@link java.util.Date}.
     */
    @Parameter(required = true, principal = true, autoconnect = true)
    private Date value;

    /**
     * The format used to format <em>and parse</em> dates. This is typically specified as a string which is coerced to a
     * DateFormat. You should be aware that using a date format with a two digit year is problematic: Java (not
     * Tapestry) may get confused about the century. The default is either the localized short date format or (if the
     * time parameter is true), the localized short date time format.
     */
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private DateFormat format;

    /**
     * If true, then the calendar will include time selection as well as date selection. This is normally false, unless
     * the property has the {@link TimeSignificant} annotation.
     * 
     * @since 1.1
     */
    @Parameter
    private boolean time;

    /**
     * If true, then the text field will be hidden, and only the icon for the date picker will be visible. The default
     * is false.
     */
    @Parameter
    private boolean hideTextField;

    /**
     * Object will will provide access to annotations (such as {@link TimeSignificant}). This is only used
     * when configuring DateField to work within the {@link BeanEditor}. Normally, annotations come from
     * the property bound the value parameter.
     * 
     * @since 1.1
     */
    @Parameter
    private AnnotationProvider annotationProvider;

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
    private static final Set<String> SUPPORTED_LANGUAGES = CollectionFactory
            .newSet(TapestryInternalUtils
                    .splitAtCommas("af,al,big,big5,br,ca,cs,da,de,du,el,en,es,eu,fi,fr,hr,hu,it,jp,ko,lt,lv,nl,no,pl,pt,ro,ru,si,sk,sp,sr,sv,tr,zh"));

    /**
     * Computes a default value for the "validate" parameter using
     * {@link org.apache.tapestry5.services.ComponentDefaultProvider}.
     */
    Binding defaultValidate()
    {
        return defaultProvider.defaultValidatorBinding("value", resources);
    }

    AnnotationProvider defaultAnnotationProvider()
    {
        return new AnnotationProvider()
        {
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
            {
                return resources.getParameterAnnotation("value", annotationClass);
            }
        };
    }

    Binding defaultTime()
    {
        return new AbstractBinding(resources.getLocation())
        {
            public Object get()
            {
                return annotationProvider.getAnnotation(TimeSignificant.class) != null;
            }
        };
    }

    Binding defaultFormat()
    {
        return new AbstractBinding(resources.getLocation())
        {
            @Override
            public boolean isInvariant()
            {
                return false;
            }

            public Object get()
            {
                DateFormat shortDateFormat = time ? DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,
                        locale) : DateFormat.getDateInstance(DateFormat.SHORT, locale);

                if (shortDateFormat instanceof SimpleDateFormat)
                {
                    SimpleDateFormat simpleDateFormat = (SimpleDateFormat) shortDateFormat;

                    String pattern = simpleDateFormat.toPattern();

                    String revised = pattern.replaceAll("([^y])yy$", "$1yyyy");

                    return new SimpleDateFormat(revised);
                }

                return shortDateFormat;
            }
        };
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

        if (isDisabled())
            writer.attributes("disabled", "disabled");

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

        JSONObject spec = new JSONObject();

        spec.put("clientId", clientId);
        spec.put("time", time);
        spec.put("clientDateFormat", formatConverter.convertToClient(format));

        renderSupport.addInit("tapxDateField", spec);
    }

    private Asset findCalendarAsset(String path)
    {
        return assetSource.getAsset(null, calendarPath + "/" + path, null);
    }

    private String formatCurrentValue()
    {
        if (value == null)
            return "";

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
