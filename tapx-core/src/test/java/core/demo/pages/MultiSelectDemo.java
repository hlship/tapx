package core.demo.pages;

import java.util.Set;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.demo.data.Person;
import com.demo.data.PersonMultiSelectModel;
import com.howardlewisship.tapx.core.multiselect.MultipleSelectModel;

import core.demo.services.PersonDAO;

public class MultiSelectDemo
{
    @Inject
    private PersonDAO personDAO;

    @Property
    @Persist
    private Set<Person> selectedValues;

    @Property
    private Person newPerson;

    void setupRender()
    {
        if (selectedValues == null)
        {
            selectedValues = CollectionFactory.newSet();
        }
    }

    public MultipleSelectModel<Person> getModel()
    {
        return new PersonMultiSelectModel(personDAO);
    }
}
