// Copyright 2010 [ORG]
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

package com.howardlewisship.tapx.processing.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@SupportsInformalParameters
@Import(library = "${tapx.processing}")
public class Processing implements ClientElement
{
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.ASSET)
    private Asset script;

    @Parameter(value = "500")
    private int width;

    @Parameter(value = "500")
    private int height;

    @Inject
    private ComponentResources resources;

    @Environmental
    private JavaScriptSupport jsSupport;

    private String clientId;

    void setupRender()
    {
        clientId = jsSupport.allocateClientId(resources);
    }

    public String getClientId()
    {
        return clientId;
    }

    boolean beginRender(MarkupWriter writer)
    {
        writer.element("canvas", "id", clientId, "datasrc", script.toClientURL(), "width", width, "height", height);

        resources.renderInformalParameters(writer);

        writer.end();

        return false;

        // TODO: A fallback image inside the <canvas>
    }

}
