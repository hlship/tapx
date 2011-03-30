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

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Substitute for Tapestry's normal Request object, used when rendering a template page.
 */
public class TemplateRequest implements Request
{
    private final Map<String, Object> attributes = CollectionFactory.newMap();

    public Object getAttribute(String name)
    {
        return attributes.get(name);
    }

    public String getContextPath()
    {
        return "";
    }

    public long getDateHeader(String name)
    {
        return 0;
    }

    public String getHeader(String name)
    {
        return null;
    }

    public List<String> getHeaderNames()
    {
        return Collections.emptyList();
    }

    public Locale getLocale()
    {
        return null;
    }

    public String getMethod()
    {
        return "GET";
    }

    public String getParameter(String name)
    {
        return null;
    }

    public List<String> getParameterNames()
    {
        return Collections.emptyList();
    }

    public String[] getParameters(String name)
    {
        return null;
    }

    public String getPath()
    {
        return "/";
    }

    public String getServerName()
    {
        return "<template api>";
    }

    public Session getSession(boolean create)
    {
        if (create)
            throw new IllegalStateException("The Tapestry Template API does not support a persistent Session.");

        return null;
    }

    public boolean isRequestedSessionIdValid()
    {
        return false;
    }

    public boolean isSecure()
    {
        return false;
    }

    public boolean isXHR()
    {
        return false;
    }

    public void setAttribute(String name, Object value)
    {
        attributes.put(name, value);
    }

    /**
     * Returns 80.
     * 
     * @since Tapestry 5.2.0
     */
    public int getLocalPort()
    {
        return 80;
    }

    /**
     * Returns 80.
     * 
     * @since Tapestry 5.2.5
     * @since 1.1
     */
    public int getServerPort()
    {
        return 80;
    }

}
