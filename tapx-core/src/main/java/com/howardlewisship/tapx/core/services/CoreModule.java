// Copyright 2010 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.core.services;

import org.apache.tapestry5.VersionUtils;
import org.apache.tapestry5.ioc.MappedConfiguration;

public class CoreModule
{
    /**
     * Temporary: enable access to all of tapx as T5.2.0 is overly protective.
     */
    public void contributeRegexAuthorizer(
            org.apache.tapestry5.ioc.Configuration<String> configuration)
    {
        configuration.add("^com/howardlewisship/tapx/.*");
    }

    /** All of the different modules (core, datefield, etc.) work off this one mapping. */
    public static void contributeClasspathAssetAliasManager(
            MappedConfiguration<String, String> configuration)
    {
        String version = VersionUtils
                .readVersionNumber("META-INF/maven/com.howardlewisship/tapx-core/pom.properties");

        configuration.add(String.format("tapx/%s", version), "com/howardlewisship/tapx");
    }
}