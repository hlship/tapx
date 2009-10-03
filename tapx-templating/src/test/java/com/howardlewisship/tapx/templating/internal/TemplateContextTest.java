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

package com.howardlewisship.tapx.templating.internal;

import org.apache.tapestry5.ioc.test.TestBase;
import org.apache.tapestry5.services.Context;
import org.testng.annotations.Test;

import com.howardlewisship.tapx.templating.internal.TemplateContext;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class TemplateContextTest extends TestBase
{
    private static final String ROOT_PATH = "src/test/context.1";

    private static final File ROOT_DIRECTORY = new File(ROOT_PATH);

    @Test
    public void scan_root_directory()
    {
        Context c = new TemplateContext(ROOT_DIRECTORY);

        List<String> actual = c.getResourcePaths("");

        assertListsEquals(actual, "file-a", "file-b", "subdir/file-c", "subdir/file-d");

        c = new TemplateContext(new File("src/test/context.1/"));

        assertListsEquals(c.getResourcePaths(""), actual);

        assertListsEquals(c.getResourcePaths("/"), actual);
    }

    @Test
    public void scan_subdirectory()
    {
        Context c = new TemplateContext(ROOT_DIRECTORY);

        List<String> actual = c.getResourcePaths("subdir");

        assertListsEquals(actual, "subdir/file-c", "subdir/file-d");

        assertListsEquals(c.getResourcePaths("subdir/"), actual);

        c = new TemplateContext(new File("src/test/context.1/"));

        assertListsEquals(c.getResourcePaths("subdir"), actual);

        assertListsEquals(c.getResourcePaths("subdir/"), actual);
    }

    @Test
    public void attribute_storage()
    {
        Context c = new TemplateContext(ROOT_DIRECTORY);

        assertTrue(c.getAttributeNames().isEmpty());

        assertNull(c.getAttribute("does-not-matter"));
    }

    @Test
    public void get_missing_file()
    {
        Context c = new TemplateContext(ROOT_DIRECTORY);

        File f = c.getRealFile("missing.txt");

        assertNotNull(f);
        assertFalse(f.exists());
    }

    @Test
    public void get_existing_URL() throws MalformedURLException
    {
        Context c = new TemplateContext(ROOT_DIRECTORY);

        URL actual = c.getResource("file-a");

        assertEquals(actual, new File(ROOT_DIRECTORY, "file-a").toURI().toURL());
    }

    @Test
    public void get_URL_for_missing_file()
    {
        Context c = new TemplateContext(ROOT_DIRECTORY);

        assertNull(c.getResource("file-z"));
    }
}
