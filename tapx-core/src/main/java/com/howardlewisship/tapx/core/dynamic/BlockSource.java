// Copyright 2010 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.core.dynamic;

import org.apache.tapestry5.Block;

import com.howardlewisship.tapx.core.components.Dynamic;

/**
 * Used by implementations of {@link DynamicTemplate} to obtain {@link Blocks} as replacements
 * for elements within the template. The Blocks are passed to the {@link Dynamic} component as informal parameters.
 * 
 * @since 1.1
 */
public interface BlockSource
{
    /**
     * Returns the Block with the given unique name.
     * 
     * @throws RuntimeException
     *             if no such block exists
     */
    Block getBlock(String name);
}
