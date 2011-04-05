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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;

import com.howardlewisship.tapx.core.services.Condition;
import com.howardlewisship.tapx.core.services.ConditionSource;

public class CondBindingFactory implements BindingFactory
{
    private final ConditionSource conditionSource;

    private final Pattern parser = Pattern.compile("^\\s*(!?)\\s*([a-zA-Z_][a-zA-Z0-9_-]*)\\s*$");

    public CondBindingFactory(ConditionSource conditionSource)
    {
        this.conditionSource = conditionSource;
    }

    public Binding newBinding(String description, ComponentResources container, ComponentResources component,
            String expression, Location location)
    {
        final Condition condition = toCondition(expression);

        return new AbstractBinding(location)
        {
            /**
             * Returns false; conditions values are expected to be different at different times,
             * even for the same client.
             */
            @Override
            public boolean isInvariant()
            {
                return false;
            }

            /** Return Boolean.class. */
            @SuppressWarnings("rawtypes")
            public Class getBindingType()
            {
                return Boolean.class;
            }

            /**
             * Invokes and returns {@link Condition#isConditionTrue()}.
             */
            public Object get()
            {
                return condition.isConditionTrue();
            }
        };
    }

    private Condition toCondition(String expression)
    {
        Matcher matcher = parser.matcher(expression);

        if (!matcher.matches())
            throw new RuntimeException(
                    String.format(
                            "Condition name '%s' is not valid. Names should only contain word characters, and may be prefixed with an '!' to invert the condition.",
                            expression));

        Condition condition = conditionSource.getCondition(matcher.group(2));

        boolean invert = matcher.group(1).equals("!");

        return invert ? invertCondition(condition) : condition;
    }

    private Condition invertCondition(final Condition condition)
    {
        return new Condition()
        {
            public boolean isConditionTrue()
            {
                return !condition.isConditionTrue();
            }
        };
    }
}
