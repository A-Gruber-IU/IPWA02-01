package ghostnets;

import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Startup
@ApplicationScoped
public class SichtungMeldungDAO {

    @PersistenceContext
    EntityManager em;

    public List<SichtungMeldung> findAll() {
        Query abfrage = em.createQuery("select a from SichtungMeldung a");
        List<SichtungMeldung> alleSichtungen = abfrage.getResultList();
        return alleSichtungen;
    }

    public Timestamp findeLetzteSichtung(int netzID) {
        try {
            return (Timestamp) em.createQuery("SELECT sichtung.zeitpunktSichtung FROM SichtungMeldung sichtung WHERE sichtung.netz.netzID = :SuchNetzID")
                    .setParameter("SuchNetzID", netzID)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void sichtungMeldungSpeichern(SichtungMeldung sichtungMeldung) {
        try {
            em.persist(sichtungMeldung);
        } catch (Exception e) {
            // Weitere Fehlerbehandlung und Logging @toDo
            e.printStackTrace();
            throw e;
        }
    }
}




