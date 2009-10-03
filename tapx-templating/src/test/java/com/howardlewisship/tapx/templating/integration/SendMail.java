// Copyright 2009 Howard M. Lewis Ship
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.howardlewisship.tapx.templating.integration;

import com.howardlewisship.tapx.templating.RenderedStream;
import com.howardlewisship.tapx.templating.TemplateAPI;
import com.howardlewisship.tapx.templating.TemplateRenderer;
import com.howardlewisship.tapx.templating.services.MailMessagePreparer;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.Properties;

public class SendMail
{
    private static final String TO = "hlship@comcast.net";

    public static void main(String[] args) throws Exception
    {
        TemplateAPI templateAPI = new TemplateAPI("app2", new File("src/test/context.2"));


        TemplateRenderer renderer = templateAPI.createRenderer("ClasspathAssets", "en", "default");
        RenderedStream renderedStream = renderer.render();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.comcast.net");
        props.put("mail.from", "hlship@comcast.net");
        props.put("mail.user", "hlship");
        props.put("mail.password", System.getProperty("mail.password"));
        props.put("mail.debug", "true");

        Session session = Session.getInstance(props, null);

        Message message = new MimeMessage(session);

        message.setFrom();

        message.setSubject("Test message " + new Date());
        message.setSentDate(new Date());

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(TO));


        templateAPI.getRegistry().getService(MailMessagePreparer.class).prepareMessage(message, renderedStream);

        Transport.send(message);

        templateAPI.shutdown();
    }

}
