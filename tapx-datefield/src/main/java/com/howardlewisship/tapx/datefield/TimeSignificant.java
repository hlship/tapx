// Copyright 2010 The Apache Software Foundation
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

package com.howardlewisship.tapx.datefield;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Date;

import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;

import com.howardlewisship.tapx.datefield.components.DateField;

/**
 * Annotation put on properties of type {@link Date} indicate that the time portion
 * of the date is significant. A {@link DateField} component will know to include
 * time selection. The client-side calendar widget only supports accuracy to one minute (seconds
 * and below can not be selected).
 * 
 * @since 1.1.
 */
@Target(
{ ElementType.FIELD, ElementType.METHOD })
@Retention(RUNTIME)
@Documented
@UseWith(AnnotationUseContext.BEAN)
public @interface TimeSignificant
{

}
