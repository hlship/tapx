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

import com.howardlewisship.tapx.templating.TemplateRenderer;

import org.apache.tapestry5.runtime.Component;

/**
 * Factory service for wrapping {@link org.apache.tapestry5.runtime.Component}s up as {@link
 * com.howardlewisship.tapx.templating.TemplateRenderer}s.
 */
public interface TemplateRendererFactory
{
    /**
     * Creates a TemplateRenderer as a wrapper around the provided page.
     *
     * @param page to act as a template
     * @return renderer
     */
    TemplateRenderer createRenderer(Component page);
}
