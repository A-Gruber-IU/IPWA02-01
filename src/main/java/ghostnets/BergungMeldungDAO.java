package ghostnets;

import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

@Startup
@ApplicationScoped
public class BergungMeldungDAO {

    @PersistenceContext
    EntityManager em;

    public List<SichtungMeldung> findAll() {
        Query abfrage = em.createQuery("select a from BergungMeldung a");
        List<SichtungMeldung> alleBergungen = abfrage.getResultList();
        return alleBergungen;
    }

    public void bergungMeldungSpeichern(BergungMeldung bergungMeldung) {
        try {
            em.persist(bergungMeldung);
        } catch (Exception e) {
            // Weitere Fehlerbehandlung und Logging @toDo
            e.printStackTrace();
            throw e;
        }
    }
}




