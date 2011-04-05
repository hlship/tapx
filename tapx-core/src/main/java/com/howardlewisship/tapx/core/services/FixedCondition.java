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

/**
 * {@link Condition} implementation that returns a fixed value.
 * 
 * @since 1.1
 */
public class FixedCondition implements Condition
{
    private final boolean value;

    public FixedCondition(boolean value)
    {
        this.value = value;
    }

    public boolean isConditionTrue()
    {
        return value;
    }

}
