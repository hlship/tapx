// Copyright 2010, 2011 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.yui.services;

import com.howardlewisship.tapx.yui.YuiSymbols;
import com.howardlewisship.tapx.yui.services.internal.ImportYUIWorker;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;

public class YuiModule
{
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(YuiSymbols.BASE, "classpath:com/howardlewisship/tapx/yui_2_8_0r4");
        configuration.add(YuiSymbols.SKINS, "${tapx-yui.yui-base}/build/assets/skins");

    }

    public static void contributeComponentClassTransformWorker(
            OrderedConfiguration<ComponentClassTransformWorker2> configuration)
    {
        configuration.addInstance("ImportYUI", ImportYUIWorker.class, "before:RenderPhase");
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("tapx", "com.howardlewisship.tapx.yui"));
    }
}
