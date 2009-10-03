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

/**
 * An enclosure to a {@link com.howardlewisship.tapx.templating.RenderedStream}.
 */
public interface RenderedStreamEnclosure extends ContentStream
{
    /**
     * Returns the content id. The main body of the rendered stream may include references to this enclosure via URLs
     * with the "cid:" protocol. Content id strings are assigned unpredictably, but the same resource, represented as an
     * enclosure multiple times, will have the same content id (for any single rendering of a template).
     *
     * @return a content id string
     */
    String getContentID();
}
