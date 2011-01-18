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

package com.howardlewisship.tapx.core.services;

import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;
import org.apache.tapestry5.ioc.util.UnknownValueException;

/**
 * A source for named {@link Condition}s.
 * 
 * @since 1.1
 */
@UsesMappedConfiguration(Condition.class)
public interface ConditionSource
{
    /**
     * Gets the condition by its name (ignoring case).
     * 
     * @param name
     *            the name to locate the condition
     * @return the Condition
     * @throws UnknownValueException
     *             if the name does not match a condition
     */
    Condition getCondition(String name);
}
