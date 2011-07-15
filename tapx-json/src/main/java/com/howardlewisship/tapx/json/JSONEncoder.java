// Copyright 2011 The Apache Software Foundation
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

package com.howardlewisship.tapx.json;

import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;

/**
 * Used to coerce objects into JSON format.
 */
@UsesMappedConfiguration(key = Class.class, value = JSONEncoder.class)
public interface JSONEncoder<T> {
    /**
     * Encodes an arbitrary input object into a JSON object
     * (an object that may be used as a value in a {@link org.apache.tapestry5.json.JSONArray}
     * or {@link org.apache.tapestry5.json.JSONObject}.
     *
     * @param input
     * @return a valid JSON value
     */
    Object encodeAsJSON(T input);
}
