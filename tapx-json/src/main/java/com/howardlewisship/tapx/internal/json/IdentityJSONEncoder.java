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

package com.howardlewisship.tapx.internal.json;

import com.howardlewisship.tapx.json.JSONEncoder;

/**
 * Identity encoder for objects that are already valid JSON values.
 */
public class IdentityJSONEncoder implements JSONEncoder<Object> {
    @Override
    public Object encodeAsJSON(Object input) {
        return input;
    }
}