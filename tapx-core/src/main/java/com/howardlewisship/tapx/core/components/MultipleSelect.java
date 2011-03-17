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

import java.util.Set;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.components.Palette;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.howardlewisship.tapx.core.multiselect.MultipleSelectModel;

/**
 * Both a simplification of the Tapestry {@link Palette} component, and an extension in that it supports
 * adding new values on the fly on the client side.
 */
@Import(stack = "tapx-core")
public class MultipleSelect extends AbstractField
{
    /**
     * The set of values edited by the component; this is used when rendering. When the form is submitted,
     * the values are first cleared, then repopulated with the selected values (some of which may be created
     * for the first time as part of this process).
     */
    @Parameter(required = true, allowNull = false, autoconnect = true)
    private Set<?> values;

    @SuppressWarnings("rawtypes")
    @Parameter(required = true, allowNull = false)
    private MultipleSelectModel model;

    /**
     * Additional CSS class(es), beyond the mandatory default of "tx-multiselect".
     */
    @Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL)
    private String className;

    @Environmental
    private JavaScriptSupport jss;

    public String getComputedClassName()
    {
        return className == null ? "tx-multiselect" : "tx-multiselect " + className;
    }

    @SuppressWarnings("unchecked")
    void afterRender()
    {
        JSONObject spec = new JSONObject("clientId", getClientId());

        for (Object value : values)
        {
            String clientValue = model.toClient(value);

            spec.append("values", clientValue);
        }

        for (Object value : model.getAvailableValues())
        {
            String clientValue = model.toClient(value);
            String label = model.toLabel(value);

            JSONArray row = new JSONArray(clientValue, label);
            spec.append("model", row);
        }

        jss.addInitializerCall("tapxMultipleSelect", spec);
    }

    protected void processSubmission(String name)
    {

    }
}
