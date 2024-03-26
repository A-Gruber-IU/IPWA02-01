package ghostnets;

import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import java.util.List;

@Startup
@ApplicationScoped
public class PersonDAO {

    @PersistenceContext
    EntityManager em;

    public List<Person> findAll() {
        Query abfrage = em.createQuery("select a from Person a");
        List<Person> allePersonen = abfrage.getResultList();
        return allePersonen;
    }

    public Person personenAbgleich(Person person) {
        try {
            return (Person) em.createQuery(
                            "SELECT p FROM Person p WHERE p.nachname = :personNachname AND p.vorname = :personVorname AND p.telefonNr = :personTelefonNr")
                    .setParameter("personNachname", person.getNachname())
                    .setParameter("personVorname", person.getVorname())
                    .setParameter("personTelefonNr", person.getTelefonNr())
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void personSpeichern(Person person) {
        try {
            em.persist(person);
        } catch (Exception e) {
            // Weitere Fehlerbehandlung und Logging @toDo
            e.printStackTrace();
            throw e;
        }
    }
}
