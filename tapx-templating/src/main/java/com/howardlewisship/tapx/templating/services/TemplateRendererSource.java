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

package com.howardlewisship.tapx.templating.services;

import com.howardlewisship.tapx.templating.TemplateRenderer;

/**
 * A source for {@link com.howardlewisship.tapx.templating.TemplateRenderer}s, which are used to transform a Tapestry page
 * (the template) and a set of data into {@link com.howardlewisship.tapx.templating.RenderedStream}.
 */
public interface TemplateRendererSource
{
    /**
     * Creates a new rendered for the named template. The renderer should only be used in the thread in which it was
     * created.
     *
     * @param templateName name of template; a page name
     * @param localeName   identifies the locale in which the response should be generated
     * @param location     identifies the location of the recipient, which can be used to adjust aspects of the template
     *                     (it is intended to support location-specific content delivery servers)
     * @return a renderer that can be configured and from which a stream can be obtained
     */
    TemplateRenderer createRenderer(String templateName, String localeName, String location);
}
