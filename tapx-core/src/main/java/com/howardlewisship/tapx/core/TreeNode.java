package com.howardlewisship.tapx.core;

import java.util.List;

/**
 * A node within a {@link TreeModel}.
 * 
 * @param <T>
 *            type of node
 * @since 1.1
 */
public interface TreeNode<T>
{
    /**
     * Returns a string Id for the node that uniquely identifies it.
     * 
     * @return unique string identifying the node
     * @see TreeModel#getById(String)
     */
    String getId();

    /** Returns the value represented by this node. */
    T getValue();

    /**
     * If true, then this node is a leaf node, which never has children (i.e., a file). If false, the node
     * may have children (i.e., a folder).
     * 
     * @return true for leaft nodes, false for folder nodes
     */
    boolean isLeaf();

    /** Returns true if this non-leaf node has child nodes. This will not be invoked for leaf nodes. */
    boolean getHasChildren();

    /** Returns the actual children of this non-leaf node, as additional nodes. */
    List<TreeNode<T>> getChildren();

    // TODO: Some way to influence the rendered output (i.e., to display different icons based on
    // file type).

    /** Returns a textual label for the node. Not all UIs will make use of the label, but default UIs will. */
    public String getLabel();
}
