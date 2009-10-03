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

import java.io.IOException;

/**
 * After a page is configured via {@link com.howardlewisship.tapx.templating.TemplateRenderer#init(String, Object)}, it is
 * converted to a RenderedStream here.
 */
public interface RenderedStreamSource
{
    RenderedStream createRenderedStream(String pageName) throws IOException;
}
