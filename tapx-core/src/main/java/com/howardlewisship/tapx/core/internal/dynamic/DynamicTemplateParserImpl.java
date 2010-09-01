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

package com.howardlewisship.tapx.core.internal.dynamic;

import java.util.Map;

import org.apache.tapestry5.internal.services.PagePool;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.URLChangeTracker;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.services.BindingSource;
import org.apache.tapestry5.services.UpdateListener;

import com.howardlewisship.tapx.core.dynamic.DynamicTemplate;
import com.howardlewisship.tapx.core.dynamic.DynamicTemplateParser;

public class DynamicTemplateParserImpl implements DynamicTemplateParser, UpdateListener
{
    private final Map<Resource, DynamicTemplate> cache = CollectionFactory.newConcurrentMap();

    private final PagePool pagePool;

    private final BindingSource bindingSource;

    private final URLChangeTracker tracker;

    public DynamicTemplateParserImpl(ClasspathURLConverter converter, PagePool pagePool, BindingSource bindingSource)
    {
        this.pagePool = pagePool;
        this.bindingSource = bindingSource;

        tracker = new URLChangeTracker(converter);
    }

    public DynamicTemplate parseTemplate(Resource resource)
    {
        DynamicTemplate result = cache.get(resource);

        if (result == null)
        {
            result = doParse(resource);
            cache.put(resource, result);

            tracker.add(resource.toURL());
        }

        return result;
    }

    private DynamicTemplate doParse(Resource resource)
    {
        return new DynamicTemplateSaxParser(resource, bindingSource).parse();
    }

    public void checkForUpdates()
    {
        if (tracker.containsChanges())
        {
            tracker.clear();
            cache.clear();

            // A typical case is that a "context:" or "asset:" binding is used with the Dynamic component's template
            // parameter. This causes the Asset to be converted to a Resource and parsed. However, those are invariant
            // bindings, so even if it is discovered that the underlying file has changed, the parsed template
            // is still cached inside the component. Clearing the page pool forces the page instance to be
            // rebuilt, which is a crude way of clearing out that data. Other alternatives exist, such as
            // yielding up a proxy to the DynamicTemplate that is more change-aware.

            pagePool.clear();
        }
    }

}
