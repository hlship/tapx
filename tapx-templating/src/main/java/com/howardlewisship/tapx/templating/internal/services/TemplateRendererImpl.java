// Copyright 2009, 2010 Howard M. Lewis Ship
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

import java.io.IOException;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.internal.util.OneShotLock;
import org.apache.tapestry5.ioc.services.ThreadCleanupListener;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.PropertyConduitSource;

import com.howardlewisship.tapx.templating.RenderedStream;
import com.howardlewisship.tapx.templating.TemplateRenderer;

public class TemplateRendererImpl implements TemplateRenderer, ThreadCleanupListener
{
    private final Component page;

    private final PropertyConduitSource conduitSource;

    private final TypeCoercer typeCoercer;

    private final RenderedStreamSource renderedStreamSource;

    private final String pageName;

    private final OneShotLock lock = new OneShotLock();

    private boolean dead;

    public TemplateRendererImpl(Component page, TypeCoercer typeCoercer, PropertyConduitSource conduitSource,
                                RenderedStreamSource renderedStreamSource)
    {
        this.page = page;
        this.typeCoercer = typeCoercer;
        this.conduitSource = conduitSource;
        this.renderedStreamSource = renderedStreamSource;

        pageName = this.page.getComponentResources().getPageName();
    }

    @SuppressWarnings({ "unchecked" })
    public TemplateRenderer init(String propertyExpression, Object value)
    {
        assert InternalUtils.isNonBlank(propertyExpression);

        lock.check();

        checkForDeath();

        PropertyConduit conduit = conduitSource.create(page.getClass(), propertyExpression);

        Object coerced = typeCoercer.coerce(value, conduit.getPropertyType());

        conduit.set(page, coerced);

        return this;
    }


    public void threadDidCleanup()
    {
        dead = true;
    }

    public RenderedStream render() throws IOException
    {
        lock.lock();

        checkForDeath();

        return renderedStreamSource.createRenderedStream(pageName);
    }

    private void checkForDeath()
    {
        if (dead)
            throw new IllegalStateException("TemplateRenderer may not be used once the thread is cleaned up.");
    }

    @Override
    public String toString()
    {
        return String.format("TemplateRenderer[%s]", pageName);
    }
}
