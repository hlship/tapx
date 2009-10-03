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

package com.howardlewisship.tapx.templating.internal.services;

import com.howardlewisship.tapx.templating.TemplateRenderer;

import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.PropertyConduitSource;

public class TemplateRendererFactoryImpl implements TemplateRendererFactory
{
    private final TypeCoercer typeCoecer;

    private final PropertyConduitSource conduitSource;

    private final PerthreadManager perthreadManager;

    private final RenderedStreamSource renderedStreamSource;

    public TemplateRendererFactoryImpl(PropertyConduitSource conduitSource, TypeCoercer typeCoecer,
                                       PerthreadManager perthreadManager, RenderedStreamSource renderedStreamSource)
    {
        this.conduitSource = conduitSource;
        this.typeCoecer = typeCoecer;
        this.perthreadManager = perthreadManager;
        this.renderedStreamSource = renderedStreamSource;
    }

    public TemplateRenderer createRenderer(Component page)
    {
        TemplateRendererImpl renderer = new TemplateRendererImpl(page, typeCoecer, conduitSource, renderedStreamSource);

        perthreadManager.addThreadCleanupListener(renderer);

        return renderer;
    }
}
