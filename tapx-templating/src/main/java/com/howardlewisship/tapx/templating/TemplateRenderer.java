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

package com.howardlewisship.tapx.templating;

import java.io.IOException;

/**
 * A wrapper around a Tapestry page, where the page is used as a template.  The renderer may be initialized (which
 * allows properties of the page to be set), and then may be rendered as a {@link RenderedStream}. Note that a {@link
 * com.howardlewisship.tapx.templating.TemplateRenderer} is invalidated when the {@linkplain
 * org.apache.tapestry5.ioc.Registry#cleanupThread() thread is cleaned up}.
 */
public interface TemplateRenderer
{
    /**
     * Updates a property of the template page instance.  The template may be initialized until it is rendered.
     *
     * @param propertyExpression expression, rooted in the page instance, to update (usually just a property name)
     * @param value              to be assigned to the property; the value will be {@linkplain
     *                           org.apache.tapestry5.ioc.services.TypeCoercer coerced} to match the actual property
     *                           type
     * @return the same TemplateRenderer, for further configuration
     */
    TemplateRenderer init(String propertyExpression, Object value);

    /**
     * Renders the template in its current state.  A TemplateRenderer may only render once, and all initialization must
     * preceed the render.
     */
    RenderedStream render() throws IOException;
}
