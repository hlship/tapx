package com.howardlewisship.tapx.core.multiselect;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;

import com.howardlewisship.tapx.core.components.MultipleSelect;

/**
 * Model used with a {@link MultipleSelect} component, to define the available values, to convert to and from
 * client side representation, to define the client-side label for the value, and to allow for
 * new values to be created as needed.
 * 
 * @param <T>
 */
public interface MultipleSelectModel<T> extends ValueEncoder<T>
{
    /**
     * Returns the values that may be selected by the client, in presentation order (typically, alphabetical order).
     */
    List<T> getAvailableValues();

    /**
     * Converts an individual value into a label that may be presented to the client.
     * 
     * @return Unique, user presentable string
     */
    String toLabel(T value);

    /**
     * Used in the case where the client adds a new value on the client side; as part of the form submission,
     * a new instance of the value type will be created from the label (as entered on the client side).
     * 
     * @param label
     * @return
     */
    T createValue(String label);
}
