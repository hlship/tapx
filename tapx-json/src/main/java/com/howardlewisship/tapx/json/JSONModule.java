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

package com.howardlewisship.tapx.json;

import com.howardlewisship.tapx.internal.json.IdentityJSONEncoder;
import com.howardlewisship.tapx.internal.json.IterableJSONEncoder;
import com.howardlewisship.tapx.internal.json.MapJSONEncoder;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.services.StrategyBuilder;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.json.JSONString;

import java.util.Map;

public class JSONModule {

    @Primary
    public static JSONEncoder buildJSONEncoder(StrategyBuilder builder, Map<Class, JSONEncoder> configuration) {
        return builder.build(JSONEncoder.class, configuration);
    }

    @Contribute(JSONEncoder.class)
    @Primary
    public static void standardJSONEncoders(MappedConfiguration<Class, JSONEncoder> configuration) {
        JSONEncoder identity = new IdentityJSONEncoder();

        configuration.add(String.class, identity);
        configuration.add(Number.class, identity);
        configuration.add(Boolean.class, identity);
        configuration.add(JSONObject.class, identity);
        configuration.add(JSONArray.class, identity);
        configuration.add(JSONString.class, identity);
        configuration.add(void.class, identity);

        configuration.addInstance(Iterable.class, IterableJSONEncoder.class);
        configuration.addInstance(Map.class, MapJSONEncoder.class);
    }

}
