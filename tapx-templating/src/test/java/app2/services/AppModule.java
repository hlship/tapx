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

package app2.services;

import com.howardlewisship.tapx.templating.base.AbstractTemplatingIntegrationTest;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;

public class AppModule
{
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0");
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,fr");

        configuration.add("content.version", "1.1");
    }

    public static void contributeLocationManager(MappedConfiguration<String, String> configuration)
    {
        configuration.add(AbstractTemplatingIntegrationTest.DEFAULT_LOCATION,
                          "http://defaultcdn.foo.com/content/${content.version}");
        configuration.add("europe", "http://eurocdn.foo.com/public/${content.version}/shared-content/");
    }
}

