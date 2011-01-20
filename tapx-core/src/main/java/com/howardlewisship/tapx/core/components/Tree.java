package com.howardlewisship.tapx.core.components;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.howardlewisship.tapx.core.TreeModel;
import com.howardlewisship.tapx.core.TreeNode;

@SuppressWarnings("rawtypes")
@Import(stack = "tapx-core")
public class Tree
{
    /**
     * The model that drives the tree, determining top level nodes and making revealing the overall structure of the
     * tree.
     */
    @Parameter(required = true, autoconnect = true)
    private TreeModel model;

    /**
     * Optional parameter used to inform the container about what TreeNode is currently rendering; this
     * is primarily used when the label parameter is bound.
     */
    @Property
    @Parameter
    private TreeNode node;

    @Property
    @Parameter(value = "block:defaultRenderTreeNodeLabel")
    private Block label;

    @Inject
    private Block children;

    @Property
    private List<TreeNode> nodes;

    void setupRender()
    {
        nodes = model.getRootNodes();
    }

    public String getStyleClassForNode()
    {
        if (node.isLeaf())
            return "tx-leaf-node";

        // This is the initial render and children, if they exist, will not be shown.

        if (!node.getHasChildren())
            return "tx-empty-node";

        // Default: has children, but collapsed.

        return null;
    }
}
