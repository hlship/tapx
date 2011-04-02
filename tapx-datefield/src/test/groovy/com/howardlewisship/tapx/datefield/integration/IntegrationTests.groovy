// Copyright 2009, 2010 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.datefield.integration

import org.apache.tapestry5.test.SeleniumTestCase
import org.testng.annotations.Test

class IntegrationTests extends SeleniumTestCase
{
    def SUBMIT = "//input[@type='submit']";
    
    @Test
    void basics_behavior() {
        openBaseURL();
        
        clickAndWait "link=DateField Demo"
        clickAndWait "link=setup"
        
        assertFieldValue "date", "3/16/2009"
        assertText "outputdate", "March 16, 2009"
        
        type "date", "03/13/2009"
        select "localeName", "de"
        
        clickAndWait SUBMIT
        
        assertFieldValue "date", "13.03.2009"
        assertText "outputdate", "13. M\u00e4rz 2009"
        
        select "localeName", "en"
        click "time"
        
        clickAndWait SUBMIT
        
        assertFieldValue "date", "3/13/2009 12:00 AM"
        assertText "outputdate", "March 13, 2009 12:00:00 AM"
    }
    
    @Test
    void bean_editor_override() {
        
        openBaseURL()
        
        clickAndWait "link=BeanEditor Demo"
        
        // That's about as far as we take it; this demonstrates that the tapx DateField was used,
        // and that it picked up on the @TimeSignificant annotation.
        
        assertFieldValue "date", "3/16/2009 12:00 AM"
    }
}
