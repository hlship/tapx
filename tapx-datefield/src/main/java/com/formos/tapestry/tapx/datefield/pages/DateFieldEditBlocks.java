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

package com.formos.tapestry.tapx.datefield.pages;

import com.formos.tapestry.tapx.datefield.components.DateField;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;                                                                                                          
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.FieldValidator;

public class DateFieldEditBlocks
{
    @Property
    @Environmental
    private PropertyEditContext context;

    @SuppressWarnings("unused")
    @Component(
            parameters = { "value=context.propertyValue", "label=prop:context.label",
                    "clientId=prop:context.propertyid",
                    "validate=prop:dateFieldValidator" })
    private DateField dateField;

    public FieldValidator getDateFieldValidator()
    {
        return context.getValidator(dateField);
    }
}
