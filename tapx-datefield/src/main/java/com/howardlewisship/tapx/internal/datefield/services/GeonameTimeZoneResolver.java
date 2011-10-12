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

import com.howardlewisship.tapx.datefield.DateFieldSymbols;
import com.howardlewisship.tapx.datefield.services.LatLongToTimeZoneResolver;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TimeZone;

/**
 * Makes use of the web service at http://ws.geonames.org/timezoneJSON to convert a latitude and
 * longitude into TimeZone.
 */
public class GeonameTimeZoneResolver implements LatLongToTimeZoneResolver {
    private final Logger logger;

    private final String geonamesURL;

    public GeonameTimeZoneResolver(Logger logger, @Symbol(DateFieldSymbols.GEONAMES_URL) String geonamesURL) {
        this.logger = logger;
        this.geonamesURL = geonamesURL;
    }

    public TimeZone resolveTimeZone(double latitude, double longitude) {
        try {
            String content = readContent(latitude, longitude);

            if (content == null) {
                return null;
            }

            JSONObject response = new JSONObject(content);

            String timeZoneId = response.getString("timezoneId");

            return TimeZone.getTimeZone(timeZoneId);
        } catch (Exception ex) {
            logger.error(String.format("Unable to use %s to resolve time zone for %f, %f: %s", geonamesURL, latitude,
                    longitude, InternalUtils.toMessage(ex)), ex);

            return null;
        }

    }

    private String readContent(double latitude, double longitude) throws IOException {
        URL url = new URL(String.format("%s?lat=%f&lng=%f", geonamesURL, latitude, longitude));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.connect();

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            logger.info(String.format("Attempt to resolve location to time zone got %d result from %s",
                    connection.getResponseCode(), url));

            connection.disconnect();

            return null;
        }

        String content = readContentFromConnection(connection);

        connection.disconnect();

        return content;

    }

    private String readContentFromConnection(HttpURLConnection connection) throws IOException {
        StringBuilder result = new StringBuilder(200);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;

            while (true) {
                line = reader.readLine();

                if (line == null)
                    break;

                result.append(line).append('\n');
            }
        } finally {
            InternalUtils.close(reader);
        }

        return result.toString();
    }
}
