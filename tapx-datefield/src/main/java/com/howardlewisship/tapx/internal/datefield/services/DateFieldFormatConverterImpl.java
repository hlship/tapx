// Copyright 2009, 2010 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.internal.datefield.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.howardlewisship.tapx.datefield.services.DateFieldFormatConverter;

public class DateFieldFormatConverterImpl implements DateFieldFormatConverter
{
    /**
     * Pairs: left column is Java/server-side, right column is client-side. Order longest to shortest.
     */
    private final String[] conversion = new String[] {

            // year
            "yyyy", "Y",
            "yy", "y",

            // month
            "MMMM", "B",
            "MMM", "b",
            "MM", "m",
            "M", "o",

            // day
            "dd", "d",
            "d", "e",

            // am/pm
            "a", "p",

            // day name
            "EEEE", "A",
            "EEE", "a",
            "EE", "a",
            "E", "a",
            
            // hour (0-23)            
            "HH", "H",
            "H", "k",
            
            // hour (1-12)            
            "hh", "I",
            "h", "l",
            
            // minute            
            "mm", "M",
            "m", "O"
    };


    public String convertToClient(DateFormat format)
    {
        assert format != null;

        if (!(format instanceof SimpleDateFormat))
            throw new IllegalArgumentException(String.format(
                    "Conversion to client date format requires a SimpleDateFormat instance; unable to convert from class %s.",
                    format.getClass().getName()));


        SimpleDateFormat sdf = (SimpleDateFormat) format;

        String pattern = sdf.toPattern();

        StringBuilder result = new StringBuilder();
        int i = 0;

        outerloop:
        while (i < pattern.length())
        {
            for (int s = 0; s < conversion.length; s += 2)
            {
                String c = conversion[s];

                if (pattern.regionMatches(i, c, 0, c.length()))
                {
                    result.append("%");
                    result.append(conversion[s + 1]);

                    i += c.length();

                    // I believe this is the first time in 10+ years of coding Java that I've resorted to using
                    // a labeled continue.

                    continue outerloop;
                }

            }

            result.append(pattern.charAt(i++));
        }

        return result.toString();
    }
}
