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

import com.howardlewisship.tapx.templating.RenderedStream;

import org.apache.tapestry5.ioc.Resource;

import java.io.IOException;

/**
 * Responsible for tracking resources provided as part of a multi-part response. Each {@link org.apache.tapestry5.Asset}
 * that is included in the response is assigned a unique id.  It can then convert a {@link
 * com.howardlewisship.tapx.templating.RenderedStream} for the body (i.e. the HTML content) into a combined multipart/mixed
 * MIME stream including any attached resources.
 */
public interface MultipartResourceTracker
{
    /**
     * Converts a Resource (the core of an {@link org.apache.tapestry5.Asset}) into a content id, a special URL
     * including a "cid:" protocol. The ensures that two Resources of the same domain and for the same path will get the
     * same content id.
     *
     * @param domain   domain of the resource, i.e., "context", "classpath", etc.
     * @param resource to obtain a content id for
     * @return content id for the resource
     */
    String getContentURL(String domain, Resource resource);


    /**
     * Determines if a multipart stream is necessary; if so, the content stream is converted into a new stream
     * containing multiple parts.
     *
     * @param contentStream stream to wrap as necessary
     * @return the content stream unchanged if not multipart, or encapsulated in a complete stream otherwise
     */
    RenderedStream convert(RenderedStream contentStream) throws IOException;
}
