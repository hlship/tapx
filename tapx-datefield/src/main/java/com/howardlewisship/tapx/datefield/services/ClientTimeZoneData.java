// Copyright 2012 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.datefield.services;

/**
 * Contains data obtained, via JavaScript, from the client.
 */
public class ClientTimeZoneData
{
    public final String date;

    public final int offsetMinutes;

    public final long epochMillis;

    /**
     * @param date
     *         the client-side date as a string, formatted by the client using toString()
     * @param offsetMinutes
     *         offset, in minutes, from GMT
     * @param epochMillis
     *         client reported time in milliseconds sinch the epoch (Jan 1 1970)
     */
    public ClientTimeZoneData(String date, int offsetMinutes, long epochMillis)
    {
        this.date = date;
        this.offsetMinutes = offsetMinutes;
        this.epochMillis = epochMillis;
    }

}
