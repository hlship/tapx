// Copyright 2010 Howard M. Lewis Ship
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

package com.howardlewisship.tapx.core.dynamic;

import org.apache.tapestry5.ioc.Resource;

/**
 * Parses are dynamic template based on a resource or an input stream.
 * 
 * @since 1.1
 */
public interface DynamicTemplateParser
{
    /**
     * Given a Resource, parse the XML file into a template. The result is cached (using the Resource itself as the
     * key). The DynamicTemplateParser is change aware; it will clear its cache if any underlying Resource
     * changes.
     */
    DynamicTemplate parseTemplate(Resource resource);
}
