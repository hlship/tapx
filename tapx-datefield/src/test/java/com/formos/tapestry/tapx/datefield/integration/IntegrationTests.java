// Copyright 2009 Formos
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

package com.formos.tapestry.tapx.datefield.integration;

import org.apache.tapestry5.test.AbstractIntegrationTestSuite;
import org.testng.annotations.Test;

public class IntegrationTests extends AbstractIntegrationTestSuite
{
    public IntegrationTests()
    {
        super("src/test/webapp");
    }

    @Test
    public void basics_behavior()
    {
        open(BASE_URL);

        clickAndWait("link=setup");

        assertFieldValue("date", "3/16/2009");
        assertText("outputdate", "Mon Mar 16 00:00:00 PDT 2009");

        type("date", "03/13/2009");
        select("localeName", "de");

        clickAndWait(SUBMIT);

        assertFieldValue("date", "13.03.2009");
        assertText("outputdate", "Fri Mar 13 00:00:00 PDT 2009");
    }

    @Test
    public void bean_editor_override()
    {
        open(BASE_URL + "beaneditordemo");

        // An elliptical way of checking that the tapx/datefield was used, not the built-in datefield.

        assertTextPresent("{\"tapxDateField\":[[\"date\",\"%m/%e/%Y\"]]}");
    }
}
