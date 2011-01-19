package com.howardlewisship.tapx.core;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.tapestry5.ioc.annotations.AnnotationUseContext.COMPONENT;
import static org.apache.tapestry5.ioc.annotations.AnnotationUseContext.MIXIN;
import static org.apache.tapestry5.ioc.annotations.AnnotationUseContext.PAGE;
import static org.apache.tapestry5.ioc.annotations.AnnotationUseContext.SERVICE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apache.tapestry5.ioc.annotations.UseWith;

/**
 * A marker annotation used to identify services that are part of the tapx-core module.
 * 
 * @since 1.1
 */
@Target(
{ PARAMETER, FIELD, CONSTRUCTOR, METHOD })
@Retention(RUNTIME)
@Documented
@UseWith(
{ COMPONENT, MIXIN, PAGE, SERVICE })
public @interface TapxCore
{

}
