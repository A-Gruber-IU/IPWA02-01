package ghostnets;

import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import java.util.List;
import java.util.logging.Logger;

@Startup
@ApplicationScoped
public class NetzDAO {

    @PersistenceContext
    EntityManager em;

    private static final Logger logger = Logger.getLogger("NetzDAO-Logger");

    public List<Netz> findAll() {
        Query abfrage = em.createQuery("select netz from Netz netz");
        List<Netz> alleNetze = abfrage.getResultList();
        return alleNetze;
    }

    public Netz findeNetzNachID(int netzID) {
        try {
            return (Netz) em.createQuery("SELECT netz FROM Netz netz WHERE netz.netzID = :SuchNetzID")
                    .setParameter("SuchNetzID", netzID)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<Netz> findeUngeborgeneNetze() {
        Query abfrage = em.createQuery("select netz from Netz netz where netz.status <> 3");
        List<Netz> ungeborgeneNetze = abfrage.getResultList();
        return ungeborgeneNetze;
    }

    public void netzSpeichern(Netz netz) {
        try {
            em.persist(netz);
        } catch (Exception e) {
            // Weitere Fehlerbehandlung und Logging @toDo
            e.printStackTrace();
            throw e;
        }
    }

    public void netzStatusAktualisieren(Netz netz) {
        try {
            logger.info("Aktualisiere Netz mit Netz-ID: " + netz.getNetzID());
            Netz netzPersistent = em.merge(netz);
            netzPersistent.setStatus(netz.getStatus());
        } catch (NullPointerException npe) {
            // Weitere Fehlerbehandlung und Logging @toDo
            npe.printStackTrace();
            logger.info("Kein Netz gefunden mit ID: " + netz.getNetzID());
            throw npe;
        }
        catch (Exception e) {
            // Weitere Fehlerbehandlung und Logging @toDo
            e.printStackTrace();
            logger.info("Fehler beim aktualisieren des Netz-Status.");
            throw e;
        }
    }
}


// Test-Implementierung ohne Datenbank
/*
    public static List<Netz> findeUngeborgeneNetze() {
        Netz netz1 = new Netz();
        netz1.setDurchmesser(1000);
        netz1.setLat(30);
        netz1.setLng(-50);
        netz1.setStatus(1);
        netz1.setNetzID(1);
        Netz netz2 = new Netz();
        netz2.setDurchmesser(1000);
        netz2.setLat(32);
        netz2.setLng(-52);
        netz2.setStatus(2);
        netz2.setNetzID(2);
        Netz netz3 = new Netz();
        netz3.setDurchmesser(500);
        netz3.setLat(34);
        netz3.setLng(-54);
        netz3.setStatus(3);
        netz3.setNetzID(3);
        Netz netz4 = new Netz();
        netz4.setDurchmesser(300);
        netz4.setLat(54.392792);
        netz4.setLng(7.502898);
        netz4.setStatus(4);
        netz4.setNetzID(4);
        return Arrays.asList(netz1, netz2, netz3, netz4);
    }*/


