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
