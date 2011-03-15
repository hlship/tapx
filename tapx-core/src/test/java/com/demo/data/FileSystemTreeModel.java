// Copyright 2011 Howard M. Lewis Ship
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

package com.demo.data;

import java.io.File;
import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.howardlewisship.tapx.core.tree.DefaultTreeModel;
import com.howardlewisship.tapx.core.tree.TreeModelAdapter;

public class FileSystemTreeModel extends DefaultTreeModel<File>
{
    private static final ValueEncoder<File> ENCODER = new ValueEncoder<File>()
    {

        @Override
        public String toClient(File value)
        {
            return value.getPath();
        }

        @Override
        public File toValue(String clientValue)
        {
            return new File(clientValue);
        }
    };

    private static final TreeModelAdapter<File> ADAPTER = new TreeModelAdapter<File>()
    {

        @Override
        public boolean isLeaf(File value)
        {
            return value.isFile();
        }

        @Override
        public boolean hasChildren(File value)
        {
            return value.list().length > 0;
        }

        @Override
        public List<File> getChildren(File value)
        {
            return CollectionFactory.newList(value.listFiles());
        }

        @Override
        public String getLabel(File value)
        {
            return value.getName();
        }
    };

    public FileSystemTreeModel()
    {
        this(".");
    }

    public FileSystemTreeModel(String rootDir)
    {
        super(ENCODER, ADAPTER, CollectionFactory.newList(new File(rootDir).listFiles()));
    }
}
