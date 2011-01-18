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

import org.apache.tapestry5.services.BindingSource;

/**
 * A condition is an arbitrary boolean value that can be determined at runtime. Conditions form the basis for an
 * extensible set of bindings that can simplify Tapestry pages and components, by eliminating the need to inject
 * values, symbols or services.
 * <p>
 * Conditions can be referenced in templates, or elsewhere, using the {@linkplain BindingSource binding prefix} "cond:".
 * For example: <code>&lt;t:if test="cond:test-mode"&gt;</code>. An exclamation point before the name inverts it, thus
 * <code>&lt;t:if test="cond:!production-mode"&gt;</code> will evaluate to inverse of the "production-mode" condition.
 * 
 * @since 1.1
 * @see ConditionSource
 */
public interface Condition
{
    /**
     * Read the condition and return true if the condition holds at this time.
     */
    boolean isConditionTrue();
}
