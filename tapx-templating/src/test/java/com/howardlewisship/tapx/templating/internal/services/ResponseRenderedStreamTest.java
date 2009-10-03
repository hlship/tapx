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

import org.apache.tapestry5.Link;
import org.apache.tapestry5.test.TapestryTestCase;
import org.testng.annotations.Test;

import com.howardlewisship.tapx.templating.internal.services.ResponseRenderedStream;

import java.io.IOException;

public class ResponseRenderedStreamTest extends TapestryTestCase
{
    private final ResponseRenderedStream stream = new ResponseRenderedStream("MyPage");

    @Test
    public void encodeRedirectURL_is_unsupported()
    {
        try
        {
            stream.encodeRedirectURL("");
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            assertEquals(ex.getMessage(), "Method encodeRedirectURL() is not supported.");
        }
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void encodeURL_is_unsupported()
    {
        stream.encodeRedirectURL("");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void sendError_is_unsupported() throws IOException
    {
        stream.sendError(0, "");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void sendRedirect_as_Link_is_unsupported() throws IOException
    {
        stream.sendRedirect((Link) null);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void sendRedirect_as_String_is_unsuported() throws IOException
    {
        stream.sendRedirect("");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void setContentLength_not_supported()
    {
        stream.setContentLength(-1);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void setDateHeader_not_supported()
    {
        stream.setDateHeader("", 0);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void setHeader_not_supported()
    {
        stream.setHeader("", "");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void setIntHeader_not_supported()
    {
        stream.setIntHeader("", 0);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void setStatus_not_supported()
    {
        stream.setStatus(0);
    }

}
