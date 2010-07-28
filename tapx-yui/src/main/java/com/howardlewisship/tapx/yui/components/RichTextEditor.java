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

package com.howardlewisship.tapx.yui.components;

import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.howardlewisship.tapx.yui.ImportYUI;

@ImportYUI(
{ "yahoo-dom-event", "element", "container/container_core", "menu", "button", "editor/simpleeditor" })
@Import(library = "richtexteditor.js", stylesheet = "${tapx-yui.skins}/sam/skin.css")
public class RichTextEditor extends TextArea
{
    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Parameter
    private int width = 385;

    @Parameter
    private int height = 200;

    void afterRender()
    {
        JSONObject spec = new JSONObject("clientId", getClientId(), "width", Integer.toString(width), "height",
                Integer.toString(height));

        javaScriptSupport.addInitializerCall("tapxRichTextEditor", spec);
    }
}
