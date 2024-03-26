package ghostnets;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personID;
    private String vorname;
    private String nachname;
    private String telefonNr;

    public Person() {
    }

    public Person(String vorname, String nachname, String telefonNr) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.telefonNr = telefonNr;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
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

    // Methode um Werte zurückzusetzen
    @PostConstruct
    public void reset() {
        this.vorname = "";
        this.nachname = "";
        this.telefonNr = "";
    }
}