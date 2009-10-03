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

package com.howardlewisship.tapx.templating.services;

import com.howardlewisship.tapx.templating.RenderedStream;

import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * A service to set up a Java Mail {@link javax.mail.Message} with appropriate content from a {@link
 * com.howardlewisship.tapx.templating.RenderedStream}. This includes switching from a simple Mail content to multi-part
 * content if necessary.  The remaining details of mail sending: obtaining a Mail Session, instantiating the Message
 * object, and using Transport to send it, are exclusively your application's responsibility.
 */
public interface MailMessagePreparer
{
    /**
     * Prepares the Message object.
     */
    void prepareMessage(Message message, RenderedStream renderedStream) throws MessagingException;
}
