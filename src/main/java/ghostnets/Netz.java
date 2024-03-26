package ghostnets;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Netz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int netzID;

    // Aufteilung Position in Länge und Breite der Koordinate
    private double lat;
    private double lng;
    // Durchmesser in Meter
    private int durchmesser;
    // Status: 1 = gemeldet, 2 = bergung bevorstehend, 3 = geborgen, 4 = verschollen
    private int status;

    @OneToMany(mappedBy = "netz", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SichtungMeldung> sichtungMeldungen = new LinkedHashSet<>();

    public Set<SichtungMeldung> getSichtungMeldungen() {
        return sichtungMeldungen;
    }

    public void setSichtungMeldungen(Set<SichtungMeldung> sichtungMeldungen) {
        this.sichtungMeldungen = sichtungMeldungen;
    }

    public Netz() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNetzID() {
        return netzID;
    }

    public void setNetzID(int netzID) {
        this.netzID = netzID;
    }

    // Methode um Werte zurückzusetzen
    @PostConstruct
    public void reset() {
        this.status = 0;
        this.lat = 0;
        this.lng = 0;
        this.durchmesser = 0;
        this.sichtungMeldungen = null;
    }

}
