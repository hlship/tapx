// Copyright 2011 Howard M. Lewis Ship
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TimeZone;

import org.apache.tapestry5.ioc.annotations.InjectResource;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;

import com.howardlewisship.tapx.datefield.services.LatLongToTimeZoneResolver;

/**
 * Makes use of the web service at http://ws.geonames.org/timezoneJSON to convert a latitude and
 * longitude into TimeZone.
 */
public class GeonameTimeZoneResolver implements LatLongToTimeZoneResolver
{
    @InjectResource
    private Logger logger;

    public TimeZone resolveTimeZone(double latitude, double longitude)
    {
        try
        {
            String content = readContent(latitude, longitude);

            JSONObject response = new JSONObject(content);

            String timeZoneId = response.getString("timezoneId");

            return TimeZone.getTimeZone(timeZoneId);
        }
        catch (Exception ex)
        {
            logger.error(String.format("Unable to use geonames.org to resolve time zone for %f, %f: %s", latitude,
                    longitude, InternalUtils.toMessage(ex)), ex);

            return null;
        }

    }

    private String readContent(double latitude, double longitude) throws IOException
    {
        URL url = new URL(String.format("http://ws.geonames.org/timezoneJSON?lat=%f&lng=%f", latitude, longitude));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.connect();

        String content = readContentFromConnection(connection);

        connection.disconnect();

        return content;

    }

    private String readContentFromConnection(HttpURLConnection connection) throws IOException
    {
        StringBuilder result = new StringBuilder(200);
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;

            while (true)
            {
                line = reader.readLine();

                if (line == null)
                    break;

                result.append(line).append('\n');
            }
        }
        finally
        {
            InternalUtils.close(reader);
        }

        return result.toString();
    }
}
