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
import java.io.InputStream;

/**
 * A stream of content, identifying the size and content type of the data, with the ability to get the stream, or ask
 * the content to write itself to an output stream.
 */
public interface ContentStream
{
    /**
     * The size, in bytes, of the content stream, or -1 if not known.
     */
    int getSize();

    /**
     * The MIME type of the stream content.
     */
    String getContentType();

    /**
     * The byte stream itself.
     */
    InputStream getStream() throws IOException;
}
