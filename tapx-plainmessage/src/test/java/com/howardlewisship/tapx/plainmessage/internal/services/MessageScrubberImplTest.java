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

import com.howardlewisship.tapx.plainmessage.internal.services.MessageScrubberImpl;
import com.howardlewisship.tapx.plainmessage.services.MessageScrubber;

import org.apache.tapestry5.ioc.MessageFormatter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.MessagesImpl;
import org.apache.tapestry5.ioc.test.TestBase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageScrubberImplTest extends TestBase
{
    private MessageScrubber scrubber = new MessageScrubberImpl();

    @DataProvider
    public Object[][] scrub_data()
    {
        return new Object[][]
                {
                        { "plain text is unchanged", "plain text is unchanged" },
                        { "a simple <code>element</code> is removed", "a simple element is removed" },
                        { "more complex <code id='foo'>elements (some with <em>nested elements</em>)</code> are removed",
                                "more complex elements (some with nested elements) are removed" },
                        { "text can include entities: &lt;, &gt;, &amp; and soon, unicode escapes",
                                "text can include entities: <, >, & and soon, unicode escapes" },
                        { "less common: &quot; (quot), &apos; (apos), &nbsp; (nbsp)",
                                "less common: \" (quot), ' (apos), \u00a0 (nbsp)" },

                        // Note extra space in scrubbed string.

                        { "comments <!-- like this one --> are removed", "comments  are removed" },

                        { "XML escape: &#38;", "XML escape: &" }
                };
    }

    @Test(dataProvider = "scrub_data")
    public void scrub(String input, String expected)
    {
        assertEquals(scrubber.scrub(input), expected);
    }

    @Test
    public void scrub_with_no_changes_is_same_as_input()
    {
        String input = "no changes needed";

        assertSame(scrubber.scrub(input), input);
    }

    @Test
    public void invalid_entity_escape()
    {
        try
        {
            scrubber.scrub("&#abc;");
            unreachable();
        }
        catch (RuntimeException ex)
        {
            assertMessageContains(ex, "Unknown XML entity: '&#abc;'.");
        }
    }

    @Test
    public void unknown_entity_escape()
    {
        try
        {
            scrubber.scrub("&abc;");
            unreachable();
        }
        catch (RuntimeException ex)
        {
            assertMessageContains(ex, "Unknown XML entity: '&abc;'.");
        }
    }

    @Test
    public void scrubbed_messages()
    {
        ResourceBundle bundle = ResourceBundle.getBundle(
                "com.howardlewisship.tapx.plainmessage.internal.services.TestMessages");

        Messages messages = new MessagesImpl(Locale.ENGLISH, bundle);

        Messages scrubbed = scrubber.scrub(messages);

        assertTrue(scrubbed.contains("banner"));
        assertFalse(scrubbed.contains("does-not-exist"));

        assertEquals(scrubbed.get("banner"), "Tapestry is so extensible.");

        assertEquals(scrubbed.format("version", "5.1"), "Tapestry version is 5.1.");

        MessageFormatter formatter = scrubbed.getFormatter("version");

        assertEquals(formatter.format("5.1.0.5"), "Tapestry version is 5.1.0.5.");
    }

}
