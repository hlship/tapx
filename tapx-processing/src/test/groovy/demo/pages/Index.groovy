// Copyright 2010 [ORG]
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

package demo.pages

import org.apache.tapestry5.Asset 
import org.apache.tapestry5.ComponentResources 
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.PageActivationContext 
import org.apache.tapestry5.annotations.Property 
import org.apache.tapestry5.ioc.annotations.Inject 
import org.apache.tapestry5.services.AssetSource 

@Import(stylesheet="style.css")
class Index
{
    enum Demo {
        BASIC(200, 200, "basic.psj", "Circle follows mouse"),
        MOLTEN(600, 150, "molten.psj", "Molten Bar Chart")
        
        final int width, height;
        
        final String path, title;
        
        private Demo(width, height, path, title) {
            this.width = width;
            this.height = height;
            this.path = path;
            this.title = title;
        }
    }
    
    @Property
    @PageActivationContext
    private Demo demo = Demo.BASIC;
    
    @Inject
    private AssetSource assetSource;
    
    @Inject
    private ComponentResources resources;
    
    @Inject
    private Locale locale;
    
    Asset getScript() {
        return assetSource.getAsset(resources.baseResource, demo.path, locale);
    }
}
