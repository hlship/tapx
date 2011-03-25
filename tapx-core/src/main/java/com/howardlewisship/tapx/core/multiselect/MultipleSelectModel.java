package com.howardlewisship.tapx.core.multiselect;

import java.util.Set;

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
     * Returns the values that may be selected by the client. These will be presented to the user
     * in sorted order by {@linkplain #toLabel(Object) label}.
     */
    Set<T> getAvailableValues();

    /**
     * Converts an individual value into a label that may be presented to the client.
     * 
     * @return Unique, user presentable string
     */
    String toLabel(T value);

    /** Creates a new, empty instance of the object ready to have its properties filled in as necessary. */
    T createEmptyInstance();

    /**
     * Once the values of the instance are filled in, the instance is then persisted. After persisting the value,
     * its id and {@linkplain #toLabel(Object) label} will be provided to the client side.
     */
    void persistNewInstance(T newInstance);
}
