package ghostnets;

import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Past;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

@TransactionManagement(TransactionManagementType.BEAN)
@Named
@RequestScoped
public class BergungEingang implements Serializable {

    private static final Logger logger = Logger.getLogger("Meldeeingang-Logger");

    @PersistenceContext
    private EntityManager em;

    @Inject
    private PersonDAO personDAO;
    @Inject
    private Person person;
    @Inject
    private BergungMeldungDAO bergungMeldungDAO;
    @Inject
    private BergungMeldung bergungMeldung;

    @Inject
    private SessionData sessionData;

    @Past
    private Date zeitpunktBergung;

    // Attribute zur Weitergabe an Person-Instanz und Netz-Instanz
    private String vorname;
    private String nachname;
    private String telefonNr;

    @Transactional
    public String bergungMeldungAbschliessen() {
        EntityTransaction txn = em.getTransaction();
        logger.info("Starte Transaktion...");
        try {
            txn.begin();
            logger.info("Transaktion gestartet.");
            person.setTelefonNr(telefonNr);
            person.setVorname(vorname);
            person.setNachname(nachname);
            if (personDAO.personenAbgleich(person) == null) {
                logger.info("Speichere Person... " + ", Vorname: " + person.getVorname() + ", Nachname: " + person.getNachname() + ", TelNr: " + person.getTelefonNr() + ", Id: " + person.getPersonID());
                personDAO.personSpeichern(person);
                logger.info("Person gespeichert.");
                bergungMeldung.setPerson(person);
            }
            else {
                Person personRef = personDAO.personenAbgleich(person);
                bergungMeldung.setPerson(personRef);
                logger.info("Person bereits vorhanden. Erstelle Referenz. Vorname: " +  person.getVorname() + ", Nachname: " + person.getNachname() + ", TelNr: " + person.getTelefonNr() + ", Id: " + personRef.getPersonID());
            }
            Netz netzPersistent = em.find(Netz.class, sessionData.getNetzIDMarker());
            if (netzPersistent != null) {
                netzPersistent.setStatus(3);
                logger.info("Aktualisiere Netz mit Id: " + netzPersistent.getNetzID() + ", Status: " + netzPersistent.getStatus());
            } else {
                logger.info("Kein Netz gefunden mit ID: " + sessionData.getNetzIDMarker());
                txn.rollback();
                return "meldungFehler";
            }
            Timestamp jetzt = new Timestamp(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(zeitpunktBergung);
            cal.set(Calendar.MILLISECOND, 0);
            Timestamp zeitpunktSichtungSQL = new Timestamp(zeitpunktBergung.getTime());
            bergungMeldung.setZeitpunktMeldung(jetzt);
            bergungMeldung.setZeitpunktBergung(zeitpunktSichtungSQL);
            bergungMeldung.setNetz(netzPersistent);
            logger.info("Speichere Meldung... " + "ZeitpunktMeldung: " + bergungMeldung.getZeitpunktMeldung() + ", ZeitpunktSichtung: " + bergungMeldung.getZeitpunktBergung() + ", NetzID: " + bergungMeldung.getNetz() + ", PersonID: " + bergungMeldung.getPerson());
            bergungMeldungDAO.bergungMeldungSpeichern(bergungMeldung);
            logger.info("Meldung gespeichert.");
            txn.commit();
            if (!txn.isActive()) {
                logger.info("Transaktion abgeschlossen.");
                return "meldungErfolg";
            }
            else {
                txn.rollback();
                logger.info("Transaktion fehlgeschlagen.");
                return "meldungFehler";
            }
        } catch (Exception e) {
            if (txn.isActive()) {
                txn.rollback();
            }
            // Weitere Fehlerbehandlung und Logging @toDo
            e.printStackTrace();
            logger.info("Transaktion fehlgeschlagen.");
            return "meldungFehler";
        }
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getZeitpunktBergung() {
        return zeitpunktBergung;
    }

    public void setZeitpunktBergung(Date zeitpunktBergung) {
        this.zeitpunktBergung = zeitpunktBergung;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getTelefonNr() {
        return telefonNr;
    }

    public void setTelefonNr(String telefonNr) {
        this.telefonNr = telefonNr;
    }

}
