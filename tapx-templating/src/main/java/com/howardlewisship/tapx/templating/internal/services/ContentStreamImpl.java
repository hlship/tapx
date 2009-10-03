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

import com.howardlewisship.tapx.templating.ContentStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Implementation of {@link com.howardlewisship.tapx.templating.RenderedStream} built around a ByteArrayOutputStream.
 */
public class ContentStreamImpl implements ContentStream
{
    protected final ByteArrayOutputStream outputStream;

    private String contentType;

    public ContentStreamImpl(ByteArrayOutputStream outputStream)
    {
        this(null, outputStream);
    }

    public ContentStreamImpl(String contentType, ByteArrayOutputStream outputStream)
    {
        this.contentType = contentType;
        this.outputStream = outputStream;
    }


    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    // RenderedResponse methods

    public String getContentType()
    {
        return contentType;
    }

    public int getSize()
    {
        return outputStream.size();
    }

    public InputStream getStream()
    {
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

}
