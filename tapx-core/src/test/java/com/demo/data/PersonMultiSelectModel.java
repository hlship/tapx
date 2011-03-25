package com.demo.data;

import java.util.Set;

import com.howardlewisship.tapx.core.multiselect.MultipleSelectModel;

import core.demo.services.PersonDAO;

public class PersonMultiSelectModel implements MultipleSelectModel<Person>
{
    private final PersonDAO dao;

    public PersonMultiSelectModel(PersonDAO dao)
    {
        this.dao = dao;
    }

    @Override
    public String toClient(Person value)
    {
        return String.valueOf(value.getId());
    }

    @Override
    public Person toValue(String clientValue)
    {
        long id = Long.parseLong(clientValue);

        return dao.findById(id);
    }

    @Override
    public Set<Person> getAvailableValues()
    {
        return dao.findAll();
    }

    @Override
    public String toLabel(Person value)
    {
        return String.format("%s %s <%s>", value.getFirstName(), value.getLastName(), value.getEmail());
    }

    @Override
    public Person createEmptyInstance()
    {
        return new Person();
    }

    @Override
    public void persistNewInstance(Person newInstance)
    {
        dao.persist(newInstance);
    }
}
