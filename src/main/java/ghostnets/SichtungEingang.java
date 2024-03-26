package ghostnets;

import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Calendar;
import java.io.Serializable;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Past;
import java.util.logging.Logger;

@TransactionManagement(value= TransactionManagementType.BEAN)
@Named
@RequestScoped
public class SichtungEingang implements Serializable {

    private static final Logger logger = Logger.getLogger("Meldeeingang-Logger");

    @PersistenceContext
    private EntityManager em;

    @Inject
    private PersonDAO personDAO;
    @Inject
    private Person person;

    @Inject
    private SichtungMeldungDAO sichtungMeldungDAO;
    @Inject
    private SichtungMeldung sichtungMeldung;

    @Inject
    private NetzDAO netzDAO;
    @Inject
    private Netz netz;

    // Attribute zur Weitergabe an Netz-Instanz
    private double lat;
    private double lng;
    private int durchmesser;

    // Attribute zur Weitergabe an Person-Instanz
    private String vorname;
    private String nachname;
    private String telefonNr;

    @Past
    private Date zeitpunktSichtung;

    @Transactional
    public String sichtungMeldungAbschliessen() {
        EntityTransaction txn = em.getTransaction();
        logger.info("Starte Transaktion...");
        try {
            txn.begin();
            logger.info("Transaktion gestartet.");
            if (!(telefonNr.isEmpty()&&vorname.isEmpty()&&nachname.isEmpty())) {
                person.setTelefonNr(telefonNr);
                person.setVorname(vorname);
                person.setNachname(nachname);
                if (personDAO.personenAbgleich(person) == null) {
                    logger.info("Speichere Person... " + ", Vorname: " + person.getVorname() +
                            ", Nachname: " + person.getNachname() + ", TelNr: " + person.getTelefonNr() + ", Id: " + person.getPersonID());
                    personDAO.personSpeichern(person);
                    logger.info("Person gespeichert.");
                    sichtungMeldung.setPerson(person);
                }
                else {
                    Person personRef = personDAO.personenAbgleich(person);
                    sichtungMeldung.setPerson(personRef);
                    logger.info("Person bereits vorhanden. Erstelle Referenz. Vorname: " +  person.getVorname() +
                            ", Nachname: " + person.getNachname() + ", TelNr: " + person.getTelefonNr() + ", Id: " + personRef.getPersonID());
                }
            }
            else {
                sichtungMeldung.setPerson(null);
            }
            netz.setStatus(1);
            netz.setDurchmesser(durchmesser);
            netz.setLat(lat);
            netz.setLng(lng);
            logger.info("Speichere Netz... " + "Lat: " + netz.getLat() + ", Lng: " + netz.getLng() + ", Durchmesser: " + netz.getDurchmesser() + ", Id: " + netz.getNetzID() + ", Status: " + netz.getStatus());
            netzDAO.netzSpeichern(netz);
            logger.info("Netz gespeichert.");
            Timestamp jetzt = new Timestamp(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(zeitpunktSichtung);
            cal.set(Calendar.MILLISECOND, 0);
            Timestamp zeitpunktSichtungSQL = new Timestamp(zeitpunktSichtung.getTime());
            sichtungMeldung.setZeitpunktMeldung(jetzt);
            sichtungMeldung.setZeitpunktSichtung(zeitpunktSichtungSQL);
            sichtungMeldung.setNetz(netz);
            logger.info("Speichere SichtungMeldung... " + "ZeitpunktMeldung: " + sichtungMeldung.getZeitpunktMeldung() + ", ZeitpunktSichtung: " + sichtungMeldung.getZeitpunktSichtung() + ", NetzID: " + sichtungMeldung.getNetz() + ", PersonID: " + sichtungMeldung.getPerson());
            sichtungMeldungDAO.sichtungMeldungSpeichern(sichtungMeldung);
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

    public Netz getNetz() {
        return netz;
    }

    public void setNetz(Netz netz) {
        this.netz = netz;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getDurchmesser() {
        return durchmesser;
    }

    public void setDurchmesser(int durchmesser) {
        this.durchmesser = durchmesser;
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

    public Date getZeitpunktSichtung() {
        return zeitpunktSichtung;
    }

    public void setZeitpunktSichtung(Date zeitpunktSichtung) {
        this.zeitpunktSichtung = zeitpunktSichtung;
    }
}
