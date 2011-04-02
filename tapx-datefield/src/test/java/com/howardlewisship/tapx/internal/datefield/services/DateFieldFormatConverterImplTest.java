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

package com.howardlewisship.tapx.internal.datefield.services;

import com.howardlewisship.tapx.datefield.services.DateFieldFormatConverter;
import com.howardlewisship.tapx.internal.datefield.services.DateFieldFormatConverterImpl;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.Date;

public class DateFieldFormatConverterImplTest extends Assert
{
    private DateFieldFormatConverter converter = new DateFieldFormatConverterImpl();

    private static class DateFormatFixture extends DateFormat
    {
        @Override
        public StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition)
        {
            return null;
        }

        @Override
        public Date parse(String s, ParsePosition parsePosition)
        {
            return null;
        }
    }

    @DataProvider
    private Object[][] format_conversion_data()
    {
        return new Object[][]
        {
        { "dd/MM/yyyy", "%d/%m/%Y" },
        { "MMM dd, yyyy", "%b %d, %Y" },
        { "HH:mm", "%H:%M" },
        { "hh:m", "%I:%O" },
        { "h:m", "%l:%O" },
        { "d/M", "%e/%o" } };
    }

    @Test(dataProvider = "format_conversion_data")
    public void format_conversion(String pattern, String expectedClientFormat)
    {
        SimpleDateFormat format = new SimpleDateFormat(pattern);

        assertEquals(converter.convertToClient(format), expectedClientFormat);
    }

    @Test
    public void english_default_locale()
    {
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);

        assertEquals(converter.convertToClient(format), "%b %e, %Y");
    }

    @Test
    public void english_default_locale_with_time()
    {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.ENGLISH);

        assertEquals(converter.convertToClient(format), "%o/%e/%y %l:%M %p");
    }

    @Test
    public void not_simple_date_format()
    {
        DateFormat fixture = new DateFormatFixture();

        try
        {
            converter.convertToClient(fixture);
            throw new IllegalStateException("Unreachable code.");
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals(
                    ex.getMessage(),
                    "Conversion to client date format requires a SimpleDateFormat instance; unable to convert from class com.howardlewisship.tapx.internal.datefield.services.DateFieldFormatConverterImplTest$DateFormatFixture.");
        }
    }

}
