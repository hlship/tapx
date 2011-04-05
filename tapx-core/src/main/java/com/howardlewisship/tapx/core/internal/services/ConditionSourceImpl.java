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

package com.howardlewisship.tapx.core.internal.services;

import java.util.Map;

import org.apache.tapestry5.ioc.util.AvailableValues;
import org.apache.tapestry5.ioc.util.UnknownValueException;

import com.howardlewisship.tapx.core.services.Condition;
import com.howardlewisship.tapx.core.services.ConditionSource;

public class ConditionSourceImpl implements ConditionSource
{
    private final Map<String, Condition> configuration;

    public ConditionSourceImpl(Map<String, Condition> configuration)
    {
        this.configuration = configuration;
    }

    public Condition getCondition(String name)
    {
        Condition result = configuration.get(name);

        if (result == null)
            throw new UnknownValueException(String.format("Unrecognized condition name '%s'.", name),
                    new AvailableValues("Configured conditions", configuration));

        return result;
    }

}

