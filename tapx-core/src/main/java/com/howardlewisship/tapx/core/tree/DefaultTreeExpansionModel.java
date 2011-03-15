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

package com.howardlewisship.tapx.core.tree;

import java.util.Set;

import org.apache.tapestry5.BaseOptimizedSessionPersistedObject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

/**
 * Manages a Set of String TreeNode ids.
 * 
 * @param <T>
 */
public class DefaultTreeExpansionModel<T> extends BaseOptimizedSessionPersistedObject implements TreeExpansionModel<T>
{
    private final Set<String> expandedIds = CollectionFactory.newSet();

    public boolean isExpanded(TreeNode<T> node)
    {
        assert node != null;

        return expandedIds.contains(node.getId());
    }

    public void markExpanded(TreeNode<T> node)
    {
        assert node != null;

        if (expandedIds.add(node.getId()))
            markDirty();
    }

    public void markCollapsed(TreeNode<T> node)
    {
        assert node != null;

        if (expandedIds.remove(node.getId()))
            markDirty();
    }

    public void clear()
    {
        if (!expandedIds.isEmpty())
        {
            expandedIds.clear();
            markDirty();
        }
    }
}
