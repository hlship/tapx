// Copyright 2011 Howard M. Lewis Ship
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

package core.itest

import org.apache.tapestry5.test.SeleniumTestCase
import org.testng.annotations.Test

class CondBindingTest extends SeleniumTestCase
{
    @Test
    void cond_prefixes() {
        openBaseURL()

        clickAndWait "link=CondBindingPrefix Demo"

        assertText "test-mode", "ON"
        assertText "inv-alwaysfalse", "ON"
    }
}
