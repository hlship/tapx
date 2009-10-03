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

package com.howardlewisship.tapx.plainmessage.internal.services;

import com.howardlewisship.tapx.plainmessage.services.MessageScrubber;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.bindings.LiteralBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;

/**
 * Mapped to the "plain:" binding prefix; this works like the built-in "message:" binding prefix, but {@linkplain
 * com.howardlewisship.tapx.plainmessage.services.MessageScrubber#scrub(String) scrubs} HTML elements and entities out
 * of the content.
 */
public class PlainMessageBindingFactory implements BindingFactory
{
    private final MessageScrubber scrubber;

    public PlainMessageBindingFactory(MessageScrubber scrubber)
    {
        this.scrubber = scrubber;
    }

    public Binding newBinding(String description, ComponentResources container, ComponentResources component,
                              String expression, Location location)
    {
        String messageValue = container.getMessages().get(expression);

        String scurbbed = scrubber.scrub(messageValue);

        return new LiteralBinding(location, description, scurbbed);
    }
}
