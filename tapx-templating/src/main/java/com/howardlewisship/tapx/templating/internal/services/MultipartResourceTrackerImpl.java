// Copyright 2009, 2010, 2011 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.templating.internal.services;

import static org.apache.tapestry5.ioc.ScopeConstants.PERTHREAD;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.assets.ContentTypeAnalyzer;

import com.howardlewisship.tapx.templating.RenderedStream;
import com.howardlewisship.tapx.templating.RenderedStreamEnclosure;

@Scope(PERTHREAD)
public class MultipartResourceTrackerImpl implements MultipartResourceTracker
{
    private final ContentTypeAnalyzer contentTypeAnalyzer;

    private long uid = System.nanoTime();

    private static class StoredResource
    {
        final String contentId;

        final Resource resource;

        final String contentURL;

        private StoredResource(String contentId, Resource resource)
        {
            this.contentId = contentId;
            this.resource = resource;

            contentURL = "cid:" + contentId;
        }

    }

    public MultipartResourceTrackerImpl(ContentTypeAnalyzer contentTypeAnalyzer)
    {
        this.contentTypeAnalyzer = contentTypeAnalyzer;
    }

    /**
     * Map from extended path (domain + ":" + path) to StoredResource.
     */
    private Map<String, StoredResource> storedResources;

    public String getContentURL(String domain, Resource resource)
    {
        assert InternalUtils.isNonBlank(domain);
        assert resource != null;

        if (storedResources == null)
            storedResources = CollectionFactory.newMap();

        String key = domain + ":" + resource.getPath();

        StoredResource stored = storedResources.get(key);

        if (stored == null)
        {
            stored = new StoredResource(createContentId(domain, resource), resource);

            storedResources.put(key, stored);
        }

        return stored.contentURL;
    }

    private String createContentId(String domain, Resource resource)
    {
        return domain + "_" + nextUid() + "_" + resource.getFile();
    }

    private String nextUid()
    {
        return Long.toHexString(uid++);
    }

    public RenderedStream convert(final RenderedStream contentStream) throws IOException
    {
        if (storedResources == null)
            return contentStream;

        final List<RenderedStreamEnclosure> enclosures = CollectionFactory.newList();

        for (final StoredResource r : storedResources.values())
        {
            final String contentType = getContentType(r.resource);

            RenderedStreamEnclosure enclosure = new RenderedStreamEnclosure()
            {
                public String getContentID()
                {
                    return r.contentId;
                }

                public int getSize()
                {
                    return -1;
                }

                public String getContentType()
                {
                    return contentType;
                }

                public InputStream getStream() throws IOException
                {
                    return r.resource.openStream();
                }

                @Override
                public String toString()
                {
                    return String.format("RenderedStreamEnclosure[%s, %s]", r.contentId, r.resource);
                }
            };

            enclosures.add(enclosure);
        }

        return new RenderedStream()
        {
            public Collection<RenderedStreamEnclosure> getEnclosures()
            {
                return enclosures;
            }

            public int getSize()
            {
                return contentStream.getSize();
            }

            public String getContentType()
            {
                return contentStream.getContentType();
            }

            public InputStream getStream() throws IOException
            {
                return contentStream.getStream();
            }

            @Override
            public String toString()
            {
                return contentStream.toString();
            }
        };
    }

    private String getContentType(Resource resource) throws IOException
    {
        return contentTypeAnalyzer.getContentType(resource);
    }
}
