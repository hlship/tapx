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

package com.howardlewisship.tapx.templating.internal;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.Context;

public class TemplateContext implements Context
{
    private final File rootDirectory;

    public TemplateContext(File rootDirectory)
    {
        this.rootDirectory = rootDirectory;
    }

    public Object getAttribute(String name)
    {
        return null;
    }

    public List<String> getAttributeNames()
    {
        return Collections.emptyList();
    }

    public String getInitParameter(String name)
    {
        return null;
    }

    public String getMimeType(String file)
    {
        return null;
    }

    public File getRealFile(String path)
    {
        assert path != null;

        return new File(rootDirectory, path);
    }

    public URL getResource(String path)
    {
        File file = getRealFile(path);

        if (file == null || !file.exists())
            return null;

        try
        {
            return file.toURI().toURL();
        }
        catch (MalformedURLException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public List<String> getResourcePaths(String path)
    {
        List<String> result = CollectionFactory.newList();
        List<File> queue = CollectionFactory.newList();

        String rootDirectoryPath = rootDirectory.getPath();

        int rootDirectoryPathLength = rootDirectoryPath.length();

        if (!rootDirectoryPath.endsWith("/"))
            rootDirectoryPathLength++;

        File start = new File(rootDirectory, path);
        queue.add(start);

        while (!queue.isEmpty())
        {
            File f = queue.remove(0);

            if (f.isFile())
            {
                String fullPath = f.getPath();

                result.add(toRelative(fullPath.substring(rootDirectoryPathLength)));

                continue;
            }

            if (f.isDirectory())
            {
                for (File child : f.listFiles())
                {
                    // Follow Linux semantics and ignore folders that start with a "."; this
                    // is especially useful for ignoring .svn or .cvs directories.

                    if (!child.getName().startsWith("."))
                        queue.add(child);
                }
            }
        }

        Collections.sort(result);

        return result;
    }

    private String toRelative(String path)
    {
        return path.startsWith("/") ? path.substring(1) : path;
    }

    @Override
    public String toString()
    {
        return String.format("TemplateContext[%s]", rootDirectory.getAbsolutePath());
    }
}
