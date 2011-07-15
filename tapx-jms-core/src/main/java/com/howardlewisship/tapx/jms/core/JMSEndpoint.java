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

package com.howardlewisship.tapx.jms.core;


import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Method annotation used to identify service methods that should be looped through a JMS queue.
 * Methods should only be applied to methods that are void, have no checked exceptions, and whose parameters
 * can all be reasonably encoded as JSON.
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
@Documented
@UseWith(AnnotationUseContext.SERVICE)
public @interface JMSEndpoint {
    /**
     * The queue attribute defines the name of the underlying JMS queue. The default is blank,
     * in which ase a queue name is assigned based on the service interface name and method name.
     */
    String queue() default "";
}
