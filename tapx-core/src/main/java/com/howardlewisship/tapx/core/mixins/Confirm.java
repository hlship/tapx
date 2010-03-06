package com.howardlewisship.tapx.core.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavascriptSupport;

/**
 * Uses <a href="http://github.com/okonet/modalbox/tree/release1.6.1">ModalBox 1.6.1</a> to present a confirmation
 * message to the user. This mixin may be attached to a link or a submit button. It will work correctly with
 * ordinary links such as {@link PageLink} or {@link EventLink}, and will operate correctly when
 * the link is being used as a {@link Zone} trigger.
 */
@IncludeJavaScriptLibrary(
{ "${tapestry.scriptaculous}/builder.js", "modalbox.js", "confirm.js" })
@IncludeStylesheet("modalbox.css")
public class Confirm
{
    @InjectContainer
    private ClientElement container;

    @Parameter(value = "message:default-confirm-message", defaultPrefix = BindingConstants.LITERAL)
    private String message;

    @Parameter(value = "message:default-confirm-title", defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Environmental
    private JavascriptSupport javascriptSupport;

    void afterRender()
    {
        JSONObject spec = new JSONObject();

        spec.put("clientId", container.getClientId());
        spec.put("message", message);
        spec.put("title", title);

        // Late, to overwrite other event handlers
        javascriptSupport.addInitializerCall(InitializationPriority.LATE, "tapxConfirm", spec);
    }
}
