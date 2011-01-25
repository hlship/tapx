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

import java.util.List;

import com.howardlewisship.tapx.core.components.Tree;

/**
 * A model for tree-oriented data used by the {@link Tree} component.
 * 
 * @param <T>
 *            type of data in the tree
 * @since 1.1
 */
public interface TreeModel<T>
{
    /**
     * Returns the node or nodes that are the top level of the tree.
     */
    List<TreeNode<T>> getRootNodes();

    /**
     * Locates a node in the tree by its unique id.
     * 
     * @throws IllegalArgumentException
     *             if no such node exists
     * @see TreeNode#getId()
     */
    TreeNode<T> getById(String id);
}
