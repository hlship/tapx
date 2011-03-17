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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.components.Palette;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.howardlewisship.tapx.core.multiselect.MultipleSelectModel;

/**
 * Both a simplification of the Tapestry {@link Palette} component, and an extension in that it supports
 * adding new values on the fly on the client side.
 */
@Import(stack = "tapx-core")
@SuppressWarnings("rawtypes")
public class MultipleSelect extends AbstractField
{
    /**
     * The set of values edited by the component; this is used when rendering. When the form is submitted,
     * the values are first cleared, then repopulated with the selected values (some of which may be created
     * for the first time as part of this process).
     */
    @Parameter(required = true, allowNull = false, autoconnect = true)
    private Set values;

    @Parameter(required = true, allowNull = false)
    private MultipleSelectModel model;

    /**
     * Additional CSS class(es), beyond the mandatory default of "tx-multiselect".
     */
    @Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL)
    private String className;

    @Environmental
    private JavaScriptSupport jss;

    @Inject
    private Request request;

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

    @SuppressWarnings("unchecked")
    protected void processSubmission(String name)
    {
        JSONArray selected = new JSONArray(request.getParameter(name));

        values.clear();

        // First the values that have a server-side id.

        for (String clientValue : toStrings(selected, 0))
        {
            Object serverValue = model.toValue(clientValue);

            if (serverValue == null)
                throw new RuntimeException(String.format("Unable to convert client value '%s' to a server-side value.",
                        clientValue));

            values.add(serverValue);
        }

        for (String label : toStrings(selected, 1))
        {
            Object serverValue = model.createValue(label);

            if (serverValue == null)
                throw new RuntimeException(String.format("Model returned null when creating value for label '%s'.",
                        label));

            values.add(serverValue);
        }

    }

    private List<String> toStrings(JSONArray selected, int index)
    {
        JSONArray values = selected.getJSONArray(index);

        List<String> result = new ArrayList<String>(values.length());

        for (int i = 0; i < values.length(); i++)
        {
            result.add(values.getString(i));
        }

        return result;
    }
}
