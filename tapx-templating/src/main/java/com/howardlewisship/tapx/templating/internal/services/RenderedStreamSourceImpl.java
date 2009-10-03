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

import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.ResponseRenderer;

import java.io.IOException;

public class RenderedStreamSourceImpl implements RenderedStreamSource
{
    private final RequestGlobals requestGlobals;

    private final ResponseRenderer responseRenderer;

    private final MultipartResourceTracker multipartResourceTracker;

    public RenderedStreamSourceImpl(ResponseRenderer responseRenderer, RequestGlobals requestGlobals,
                                    MultipartResourceTracker multipartResourceTracker)
    {
        this.responseRenderer = responseRenderer;
        this.requestGlobals = requestGlobals;
        this.multipartResourceTracker = multipartResourceTracker;
    }

    /**
     * This is where it all comes together. We need to "trick out" the Tapestry Response object so that we can capture
     * the response stream.
     *
     * @param pageName name of page to be rendered
     * @return the page, rendered as a stream
     * @throws IOException
     */
    public RenderedStream createRenderedStream(String pageName) throws IOException
    {
        Request request = requestGlobals.getRequest();

        ResponseRenderedStream response = new ResponseRenderedStream(pageName);

        // Many our special Response object visible to anyone who cares to inject it.
        requestGlobals.storeRequestResponse(request, response);

        // The response object, as interface Response, will collect provide an output stream and
        // a print writer.  It can then turn around and, as interface RenderedStream, provide access
        // to that streamed content (as well as content type, etc.).

        responseRenderer.renderPageMarkupResponse(pageName);

        return multipartResourceTracker.convert(response);
    }
}
