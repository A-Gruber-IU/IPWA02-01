package ghostnets;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class SichtungMeldung implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int meldungID;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp zeitpunktMeldung;

    @Past
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp zeitpunktSichtung;

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

    public Timestamp getZeitpunktMeldung() {
        return zeitpunktMeldung;
    }

    public void setZeitpunktMeldung(Timestamp zeitpunktMeldung) {
        this.zeitpunktMeldung = zeitpunktMeldung;
    }

    public Timestamp getZeitpunktSichtung() {
        return zeitpunktSichtung;
    }

    public void setZeitpunktSichtung(Timestamp zeitpunktSichtung) {
        this.zeitpunktSichtung = zeitpunktSichtung;
    }

    // Methode um Werte zurückzusetzen
    @PostConstruct
    public void reset() {
        this.zeitpunktMeldung = null;
        this.zeitpunktSichtung = null;
        this.person = null;
        this.netz = null;
    }
}