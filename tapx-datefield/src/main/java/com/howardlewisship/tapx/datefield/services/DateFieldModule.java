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

package com.howardlewisship.tapx.datefield.services;

import org.apache.tapestry5.VersionUtils;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.LibraryMapping;

import com.howardlewisship.tapx.datefield.DateFieldSymbols;
import com.howardlewisship.tapx.internal.datefield.services.DateFieldFormatConverterImpl;

public class DateFieldModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(DateFieldFormatConverter.class, DateFieldFormatConverterImpl.class);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(DateFieldSymbols.JSCALENDAR_PATH,
                "classpath:/com/howardlewisship/tapx/datefield/jscalendar-1.0");
        configuration.add(DateFieldSymbols.SKIN, "aqua");
        configuration.add(DateFieldSymbols.THEME, "system");
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("tapx", "com.howardlewisship.tapx.datefield"));
    }

    public static void contributeClasspathAssetAliasManager(
            MappedConfiguration<String, String> configuration)
    {
        String version = VersionUtils
                .readVersionNumber("META-INF/maven/com.howardlewisship/tapx-datefield/pom.properties");

        configuration.add(String.format("tapx/%s/datefield", version),
                "com/howardlewisship/tapx/datefield");
    }

    public static void contributeBeanBlockOverrideSource(
            Configuration<BeanBlockContribution> configuration)
    {
        configuration.add(new BeanBlockContribution("date", "tapx/DateFieldEditBlocks", "date",
                true));
    }
}
