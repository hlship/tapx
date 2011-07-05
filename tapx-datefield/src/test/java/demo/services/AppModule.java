// Copyright 2009, 2011 Howard M. Lewis Ship
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

package demo.services;

import com.howardlewisship.tapx.core.services.CoreModule;
import com.howardlewisship.tapx.datefield.services.DateFieldModule;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.func.Flow;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;

import java.util.List;

@SubModule(
{ CoreModule.class, DateFieldModule.class })
public class AppModule
{
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,fr,de");
    }

    @SuppressWarnings("rawtypes")
    public void contributeTypeCoercer(Configuration<CoercionTuple> configuration)
    {
        configuration.add(CoercionTuple.create(Flow.class, List.class, new Coercion<Flow, List>()
        {
            public List coerce(Flow input)
            {
                return input.toList();
            }
        }));
    }

}
