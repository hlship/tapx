// Copyright 2010, 2011 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.core.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.howardlewisship.tapx.core.CoreSymbols;

/**
 * Uses <a href="http://github.com/okonet/modalbox/tree/release1.6.1">ModalBox 1.6.1</a> to present a confirmation
 * message to the user. This mixin may be attached to a link or a submit button. It will work correctly with
 * ordinary links such as {@link PageLink} or {@link EventLink}, and will operate correctly when
 * the link is being used as a {@link Zone} trigger.
 * <p>
 * If the element has the CSS class "tx-disable-confirm" at the time the link is clicked (or form submitted), then the
 * modal dialog will NOT run. Often, client-side JavaScript will add or remove this class to enable or disable the
 * confirmation behavior.
 * <p>
 * Because of problems using Selenium with the Confirm mixin, the {@linkplain CoreSymbols#TEST_MODE
 * test mode} behavior is changed: the Modalbox dialog is replaced with a simple JavaScript
 * <code>window.confirm()</code>.
 */
@Import(stack = "tapx-core")
public class Confirm
{
    @InjectContainer
    private ClientElement container;

    @Parameter(value = "message:tapx-default-confirm-message", defaultPrefix = BindingConstants.LITERAL)
    private String message;

    @Parameter(value = "message:tapx-default-confirm-title", defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Environmental
    private JavaScriptSupport javascriptSupport;

    void afterRender()
    {
        JSONObject spec = new JSONObject("clientId", container.getClientId(), "message", message, "title", title);

        // Late, to overwrite other event handlers
        javascriptSupport.addInitializerCall(InitializationPriority.LATE, "tapxConfirm", spec);
    }
}
