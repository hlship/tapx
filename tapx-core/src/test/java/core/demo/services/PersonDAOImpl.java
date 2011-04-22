package core.demo.services;

import java.util.Set;

import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.demo.data.Person;

public class PersonDAOImpl implements PersonDAO
{
    private static final Mapper<Person, Person> SAFE_COPY = new Mapper<Person, Person>()
    {
        public Person map(Person value)
        {
            return new Person(value);
        }
    };

    private final Set<Person> db = CollectionFactory.newSet();

    private long nextId = System.nanoTime();

    public PersonDAOImpl()
    {
        persist(new Person(0, "Fred", "Flintstone", "fred@bedrock.org"));
        persist(new Person(0, "Wilma", "Flintstone", "wilma@bedrock.org"));
        persist(new Person(0, "George", "Jetson", "gj@spacely.com"));
        persist(new Person(0, "Buffy", "Sommers", "buffy@vault.com"));
        persist(new Person(0, "Luke", "Skywalker", "luke@lightside.gov"));
    }

    public Set<Person> findAll()
    {
        return F.flow(db).map(SAFE_COPY).toSet();
    }

    public void persist(Person person)
    {
        Person persisted = new Person(person);
        persisted.setId(nextId++);
        db.add(persisted);

        person.setId(persisted.getId());
    }

    public Person findById(final long id)
    {
        return F.flow(db).filter(new Predicate<Person>()
        {
            public boolean accept(Person object)
            {
                return object.getId() == id;
            }
        }).map(SAFE_COPY).first();
    }
}
