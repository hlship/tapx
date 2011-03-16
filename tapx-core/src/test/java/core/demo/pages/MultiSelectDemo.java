package core.demo.pages;

import java.util.Set;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.demo.data.StringMultipleSelectModel;

public class MultiSelectDemo
{
    // Not thread safe: ok for testing only
    @Property
    private final StringMultipleSelectModel model = new StringMultipleSelectModel("Red", "Green", "Blue");

    @Property
    @Persist
    private Set<String> selectedValues;

    void setupRender()
    {
        if (selectedValues == null)
        {
            selectedValues = CollectionFactory.newSet();
            selectedValues.add("Blue");
        }
    }
}
