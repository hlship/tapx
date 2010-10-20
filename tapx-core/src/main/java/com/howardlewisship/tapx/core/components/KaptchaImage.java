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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;

import com.howardlewisship.tapx.core.services.KaptchaProducer;

/**
 * Part of a Captcha based authentication scheme; a KaptchaImage generates a new
 * text image whenever it <em>renders</em> and can provide the previously
 * rendred text subsequently (it is stored persistently in the session).
 * <p>
 * The component renders an &lt;img&gt; tag, including width and height attributes. Other attributes come from informal
 * parameters.
 */
@SupportsInformalParameters
public class KaptchaImage
{

    @Persist
    private String captchaText;

    @Inject
    private KaptchaProducer producer;

    @Inject
    private ComponentResources resources;

    @Inject
    private Response response;

    public String getCaptchaText()
    {
        return captchaText;
    }

    void setupRender()
    {
        captchaText = producer.createText();
    }

    boolean beginRender(MarkupWriter writer)
    {
        Link link = resources.createEventLink("image");

        writer.element("img",

        "src", link.toURI(),

        "width", producer.getWidth(),

        "height", producer.getHeight());

        resources.renderInformalParameters(writer);

        writer.end();

        return false;
    }

    void onImage() throws IOException
    {
        BufferedImage image = producer.createImage(captchaText);

        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.setHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        OutputStream stream = response.getOutputStream("image/jpeg");

        ImageIO.write(image, "jpg", stream);

        stream.flush();

        stream.close();
    }
}
