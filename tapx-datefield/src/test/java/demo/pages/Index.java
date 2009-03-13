// Copyright 2009 Formos
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

package demo.pages;

import com.formos.tapestry.tapx.datefield.DateFieldSymbols;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.SymbolConstants;

import java.util.Date;
import java.util.Locale;

public class Index
{
    @Property
    @Persist
    private Date date;

    @Property
    @Persist
    @Validate("required")
    private String theme, skin;

    @Inject
    @Symbol(DateFieldSymbols.DEFAULT_SKIN)
    private String defaultSkin;

    @Inject
    @Symbol(DateFieldSymbols.DEFAULT_THEME)
    private String defaultTheme;

    @Inject
    private Locale locale;

    @Inject
    private PersistentLocale persistentLocale;

    @Property
    @Inject
    @Symbol(SymbolConstants.SUPPORTED_LOCALES)
    private String supportedLocales;

    void beginRender()
    {
        if (theme == null) theme = defaultTheme;

        if (skin == null) skin = defaultSkin;
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
}
