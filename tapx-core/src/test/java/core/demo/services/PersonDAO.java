package core.demo.services;

import java.util.Set;

import com.demo.data.Person;

/** A silly in-memory interface of {@link Person} objects. */
public interface PersonDAO
{
    Set<Person> findAll();

    void persist(Person person);

    Person findById(long id);
}
