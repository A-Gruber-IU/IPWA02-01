package ghostnets;

import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

@Startup
@ApplicationScoped
public class BergeplanMeldungDAO {

    @PersistenceContext
    EntityManager em;

    public List<SichtungMeldung> findAll() {
        Query abfrage = em.createQuery("select a from BergungMeldung a");
        List<SichtungMeldung> alleBergungen = abfrage.getResultList();
        return alleBergungen;
    }

    public Person findeBergendePerson(int netzID) {
        try {
            return (Person) em.createQuery("SELECT bergeplanMeldung.person FROM BergeplanMeldung bergeplanMeldung WHERE bergeplanMeldung.netz = :BergungNetzID")
                    .setParameter("BergungNetzID", netzID)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void bergeplanMeldungSpeichern(BergeplanMeldung bergeplanMeldung) {
        try {
            em.persist(bergeplanMeldung);
        } catch (Exception e) {
            // Weitere Fehlerbehandlung und Logging @toDo
            e.printStackTrace();
            throw e;
        }
    }
}




