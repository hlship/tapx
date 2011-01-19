package com.howardlewisship.tapx.core;

import org.apache.tapestry5.services.javascript.JavaScriptStack;

/**
 * A contribution to an extensible {@link JavaScriptStack}. Such a stack is created in terms of all the contributions.
 * <p>
 * This is part of a mechanism that allows multiple modules to contribute into the tapx-core JavaScriptStack (which is
 * marked by the @{@link TapxCore} annotation).
 * 
 * @since 1.1
 */
public class StackExtension
{
    /** The type of extension. */
    public final StackExtensionType type;

    /** The value contributed; will have symbols expanded, then be converted to the appropriate type. */
    public final String value;

    public StackExtension(StackExtensionType type, String value)
    {
        this.type = type;
        this.value = value;
    }

}
