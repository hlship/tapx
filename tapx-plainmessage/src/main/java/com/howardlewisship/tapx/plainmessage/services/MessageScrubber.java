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

package com.howardlewisship.tapx.plainmessage.services;

import org.apache.tapestry5.ioc.Messages;

/**
 * Used to "scrub" HTML markup out of a message. HTML elements (i.e., start and end tags) are stripped out, and HTML
 * entities are converted back into ordinary characters. The result is the input represented as plain text.
 */
public interface MessageScrubber
{
    /**
     * Removes markup from the string and converts entities.
     *
     * @param input string to be scrubbed
     * @return string with markup removed
     */
    String scrub(String input);

    /**
     * Creates a wrapper around an existing Messages object that scrubs the result of any content.
     *
     * @param messages
     * @return messages whose output is scrubbed
     */
    Messages scrub(Messages messages);
}
