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

package demo.pages;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.PersistentLocale;

public class Index
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
    { "deprecation" })
    void onActionFromSetup()
    {
        setLocaleName("en");
        date = new Date(109, Calendar.MARCH, 16);
        time = false;
    }
}
