// Copyright 2010 Howard M. Lewis Ship
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

import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * A SetEditor is an dynamic client-side element used to edit a set of values (currently limited to Strings). The
 * presentation is two columns: a text field on the left and a list on the right. Entering a value into the text field
 * will add it to the list; the list shows the added items, with a control for each of them to delete the value.
 * <p>
 * Behind the scenes is a hidden form field; as values are added or removed from the list, the hidden form field is
 * updated.
 * <p>
 * When the form is submitted, the component will obtain the current Set and clear it (if non-null). If the current Set
 * is null, a new {@link HashSet} instance is created. The empty Set is populated with the values in the submission.
 */
@Import(stack = "tapx-core")
public class SetEditor
{
    @Parameter(required = true, autoconnect = true)
    private Set<String> set;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "block:defaultFieldLabel")
    private Block fieldLabel;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "block:defaultValuesLabel")
    private Block valuesLabel;

    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Environmental
    private FormSupport formSupport;

    @Property
    private String fieldId;

    @Inject
    private ComponentResources resources;

    @Inject
    private Request request;

    static class HandleSubmission implements ComponentAction<SetEditor>
    {
        private static final long serialVersionUID = 490527745794643539L;

        private final String name;

        HandleSubmission(String name)
        {
            this.name = name;
        }

        public void execute(SetEditor component)
        {
            component.handleSubmission(name);
        }
    }

    void setupRender()
    {
        fieldId = javaScriptSupport.allocateClientId(resources);
    }

    void afterRender()
    {
        String name = formSupport.allocateControlName(fieldId);

        JSONObject spec = new JSONObject("id", fieldId, "name", name);

        JSONArray values = new JSONArray();

        if (set != null)
        {
            for (String s : set)
                values.put(s);
        }

        spec.put("values", values);

        javaScriptSupport.addInitializerCall("tapxSetEditor", spec);

        formSupport.store(this, new HandleSubmission(name));
    }

    private void handleSubmission(String name)
    {
        if (set == null)
            set = new HashSet<String>();
        else
            set.clear();

        String valuesJSON = request.getParameter(name);

        // This is not expected to happen, there should always be, at least, an empty JSON array.
        if (valuesJSON == null)
            return;

        JSONArray values = new JSONArray(valuesJSON);

        for (int i = 0; i < values.length(); i++)
            set.add(values.getString(i));
    }
}
