// Copyright 2010 The Apache Software Foundation
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

package core.itest

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

class DynamicTest extends SeleniumTestCase
{
    void clickThru(link) {
        openBaseURL();
        
        clickAndWait "link=$link"
    }
    
    @Test
    void dynamic_component() {
        clickThru "Dynamic Demo"
        
        assertText "templateid", "1"
        assertText "target", "2"
        
        clickAndWait "link=2"
        
        assertText "templateid", "2"
        assertText "target", "1"
        
        clickAndWait "link=1"
        
        assertText "templateid", "1"
    }
    
    @Test
    void expansion_in_dynamic_template_body() {
        clickThru "Expansions in Dynamic Templates"
        
        // red is from a property, the expansion is in expansion.xml (not the TML file)
        
        assertText "color-value", "red"
    }
}
