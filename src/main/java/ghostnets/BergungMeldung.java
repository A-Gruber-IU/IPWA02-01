package ghostnets;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class BergungMeldung implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int meldungID;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp zeitpunktMeldung;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp zeitpunktBergung;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "netz_netz_id", nullable = false)
    private Netz netz;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_person_id")
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Netz getNetz() {
        return netz;
    }

    public void setNetz(Netz netz) {
        this.netz = netz;
    }

    public int getMeldungID() {
        return meldungID;
    }

    public void setMeldungID(int meldungID) {
        this.meldungID = meldungID;
    }

    public Timestamp getZeitpunktMeldung() {
        return zeitpunktMeldung;
    }

    public void setZeitpunktMeldung(Timestamp zeitpunktMeldung) {
        this.zeitpunktMeldung = zeitpunktMeldung;
    }

    public Timestamp getZeitpunktBergung() {
        return zeitpunktBergung;
    }

    public void setZeitpunktBergung(Timestamp zeitpunktBergung) {
        this.zeitpunktBergung = zeitpunktBergung;
    }

    // Methode um Werte zurückzusetzen
    @PostConstruct
    public void reset() {
        this.zeitpunktMeldung = null;
        this.zeitpunktBergung = null;
        this.person = null;
        this.netz = null;
    }
}