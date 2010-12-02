// Copyright 2009 Howard M. Lewis Ship
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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import demo.data.NameAndDate;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;

public class BeanEditorDemo
{
    @Property
    private NameAndDate bean;

    @Inject
    private PersistentLocale persistentLocale;

    Object onActivate() {
        // make sure we serve the page with an English locale as the test case
        // will fail otherwise
        if (!TapestryInternalUtils.isEqual(persistentLocale.get(), Locale.ENGLISH))
        {
            persistentLocale.set(Locale.ENGLISH);
            return BeanEditorDemo.class;
        }
        return null;
    }

    void setupRender()
    {
        bean = new NameAndDate();

        bean.setDate(new Date(109, Calendar.MARCH, 16));
    }
}
