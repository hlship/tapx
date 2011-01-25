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

package com.howardlewisship.tapx.core;

import org.apache.tapestry5.services.javascript.JavaScriptStack;

/**
 * A contribution to an extensible {@link JavaScriptStack}. Such a stack is created in terms of all the contributions.
 * <p>
 * This is part of a mechanism that allows multiple modules to contribute into the tapx-core JavaScriptStack (which is
 * marked by the @{@link TapxCore} annotation).
 * 
 * @since 1.1
 */
public class StackExtension
{
    /** The type of extension. */
    public final StackExtensionType type;

    /** The value contributed; will have symbols expanded, then be converted to the appropriate type. */
    public final String value;

    public StackExtension(StackExtensionType type, String value)
    {
        this.type = type;
        this.value = value;
    }

}
