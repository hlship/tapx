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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Component that renders a placeholder for content with controls to expand or collapsed
 * the content. The content is not rendered until the expando is first expanded. The content
 * is the body of the Expando component.
 * 
 * @since 1.1
 */
@Import(stack = "tapx-core")
public class Expando implements ClientElement
{
    @Property
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private Block title;

    private String clientId;

    @Inject
    private ComponentResources resources;

    @Environmental
    private JavaScriptSupport jss;

    @InjectComponent
    private Zone content;

    void setupRender()
    {
        clientId = jss.allocateClientId(resources);
    }

    public String getClientId()
    {
        return clientId;
    }

    void afterRender()
    {
        Link link = resources.createEventLink("expand");

        JSONObject spec = new JSONObject("clientId", clientId, "zoneId", content.getClientId(), "contentURL",
                link.toString());

        jss.addInitializerCall("tapxExpando", spec);
    }

    Object onExpand()
    {
        return resources.getBody();
    }
}
