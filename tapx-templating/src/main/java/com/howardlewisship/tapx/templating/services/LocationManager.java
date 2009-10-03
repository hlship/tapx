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

package com.howardlewisship.tapx.templating.services;

import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;

/**
 * Maps location names to location URLs. This is driven by the service's configuration. The configuration maps location
 * names to location URLs; the location URLs may end with a slash, or not. In addition, any symbols in the contributed
 * URL will be expanded.
 */
@UsesMappedConfiguration(String.class)
public interface LocationManager
{
    /**
     * Returns the URL prefix for the location. The URL prefix will
     *
     * @param locationName
     * @return corresponding location URL
     * @throws IllegalArgumentException if the provided location name is not valid
     */
    String getLocationURL(String locationName);

}
