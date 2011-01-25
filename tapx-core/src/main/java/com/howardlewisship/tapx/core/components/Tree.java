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

package com.howardlewisship.tapx.core.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.Renderable;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.howardlewisship.tapx.core.TreeModel;
import com.howardlewisship.tapx.core.TreeNode;

@SuppressWarnings(
{ "rawtypes", "unchecked", "unused" })
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
     * Allows the container to specify additional CSS class names for the outer DIV element. The outer DIV
     * always has the class name "tx-tree-container"; the additional class names are typically used to apply
     * a specific size and width to the component.
     */
    @Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL)
    private String className;

    /**
     * Optional parameter used to inform the container about what TreeNode is currently rendering; this
     * is primarily used when the label parameter is bound.
     */
    @Property
    @Parameter
    private TreeNode node;

    /**
     * Optional parameter used to inform the container about the value of the currently rendering TreeNode; this
     * is often preferable to the TreeNode, and like the node parameter, is primarily used when the label parameter
     * it bound.
     */
    @Parameter
    private Object value;

    @Property
    @Parameter(value = "block:defaultRenderTreeNodeLabel")
    private Block label;

    @Inject
    private Block children;

    @Property
    private List<TreeNode> nodes;

    @Property
    private int nodeIndex;

    @Environmental
    private JavaScriptSupport jss;

    @Inject
    private ComponentResources resources;

    @Property
    private final Renderable renderButton = new Renderable()
    {
        @Override
        public void render(MarkupWriter writer)
        {
            value = node.getValue();

            // The outer span provides the visual structure (the dashes). Since we can't rely on CSS3,
            // we mark the last one explicitly.

            writer.element("span", "class", "tx-structure" + (nodeIndex == nodes.size() - 1 ? " tx-last" : ""));

            Element e = writer.element("span", "class", "tx-tree-icon");

            if (node.isLeaf())
                e.addClassName("tx-leaf-node");
            else if (!node.getHasChildren())
                e.addClassName("tx-empty-node");

            if (!node.isLeaf() && node.getHasChildren())
            {
                String clientId = jss.allocateClientId(resources);

                e.attribute("id", clientId);

                JSONObject spec = new JSONObject("clientId", clientId, "expandURL", resources.createEventLink("expand",
                        node.getId()).toString());

                jss.addInitializerCall("tapxTreeNode", spec);
            }

            writer.end();
            writer.end();
        }
    };

    public String getContainerClass()
    {
        return className == null ? "tx-tree-container" : "tx-tree-container " + className;
    }

    void setupRender()
    {
        nodes = model.getRootNodes();
    }

    Object onExpand(String nodeId)
    {
        TreeNode container = model.getById(nodeId);

        nodes = container.getChildren();

        // The children block contains what needs to be rendered. This will end up as a JSON response, as with a Zone
        // component.

        return children;
    }
}
