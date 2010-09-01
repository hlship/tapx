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

package com.howardlewisship.tapx.templating.internal.services;

import static com.howardlewisship.tapx.templating.internal.TemplatingUtils.notSupported;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.Response;

import com.howardlewisship.tapx.templating.RenderedStream;
import com.howardlewisship.tapx.templating.RenderedStreamEnclosure;

public class ResponseRenderedStream extends ContentStreamImpl implements Response, RenderedStream
{
    private final String pageName;

    public ResponseRenderedStream(String pageName)
    {
        super(new ByteArrayOutputStream());

        this.pageName = pageName;
    }

    public String encodeRedirectURL(String URL)
    {
        return notSupported();
    }

    public String encodeURL(String URL)
    {
        return notSupported();
    }

    public OutputStream getOutputStream(String contentType) throws IOException
    {
        assert InternalUtils.isNonBlank(contentType);

        // TODO: Add checks to ensure getOutputStream() is only called once.

        setContentType(contentType);

        return outputStream;
    }

    public PrintWriter getPrintWriter(String contentType) throws IOException
    {
        // TODO: Add checks to ensure getPrintWriter() is only called once.

        OutputStream os = getOutputStream(contentType);

        // TODO: Figure out how to get the correct character set here!

        Writer w = new OutputStreamWriter(os);

        return new PrintWriter(new BufferedWriter(w));
    }

    public boolean isCommitted()
    {
        return false;
    }

    public void sendError(int sc, String message) throws IOException
    {
        notSupported();
    }

    public void sendRedirect(Link link) throws IOException
    {
        notSupported();
    }

    public void sendRedirect(String URL) throws IOException
    {
        notSupported();
    }

    public void setContentLength(int length)
    {
        notSupported();
    }

    public void setDateHeader(String name, long date)
    {
        notSupported();
    }

    public void setHeader(String name, String value)
    {
        notSupported();
    }

    public void setIntHeader(String name, int value)
    {
        notSupported();
    }

    public void setStatus(int sc)
    {
        notSupported();
    }

    @Override
    public String toString()
    {
        return String.format("RenderedStream[%s]", pageName);
    }

    public Collection<RenderedStreamEnclosure> getEnclosures()
    {
        return Collections.emptyList();
    }

    public void disableCompression()
    {
    }
}
