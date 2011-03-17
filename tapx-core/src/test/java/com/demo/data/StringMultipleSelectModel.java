package com.demo.data;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.tapestry5.func.F;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.howardlewisship.tapx.core.multiselect.MultipleSelectModel;

public class StringMultipleSelectModel implements MultipleSelectModel<String>
{
    private final List<String> values = CollectionFactory.newList();

    public StringMultipleSelectModel(String... initialValues)
    {
        values.addAll(Arrays.asList(initialValues));
    }

    @Override
    public String toClient(String value)
    {
        return Integer.toString(values.indexOf(value));
    }

    @Override
    public String toValue(String clientValue)
    {
        int index = Integer.parseInt(clientValue);

        return values.get(index);
    }

    @Override
    public Set<String> getAvailableValues()
    {
        return F.flow(values).toSet();
    }

    @Override
    public String toLabel(String value)
    {
        return value;
    }

    @Override
    public String createValue(String label)
    {
        values.add(label);

        return label;
    }

}
