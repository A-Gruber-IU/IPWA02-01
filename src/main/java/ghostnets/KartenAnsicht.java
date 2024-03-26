package ghostnets;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.*;
import java.io.Serializable;
import jakarta.annotation.PostConstruct;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;


@Named
@ViewScoped
public class KartenAnsicht implements Serializable {

    private MapModel meinModell;
    private Marker marker;
    private boolean erfolgteBergungMelden = false;
    private boolean geplanteBergungMelden = false ;
    private boolean erneuteSichtungMelden = false;
    private boolean verschollenMelden = false;

    @Inject
    private NetzDAO netzDAO;
    @Inject
    private SichtungMeldungDAO sichtungMeldungDAO;
    @Inject
    private SessionData sessionData;


    @PostConstruct
    public void initKarte() {
        meinModell = new DefaultMapModel<>();
        List<Netz> ungeborgeneNetze = netzDAO.findeUngeborgeneNetze();
        for (Netz netz : ungeborgeneNetze) {
            double lat = netz.getLat();
            double lng = netz.getLng();
            LatLng koordinate = new LatLng(lat, lng);
            String netzIDString = String.valueOf(netz.getNetzID());
            String titel = statusTextAbfrage(netz.getStatus());
            Marker marker = new Marker(koordinate, titel, netzIDString);
            // Status: 1 = gemeldet, 2 = bergung bevorstehend, 3 = geborgen, 4 = verschollen;
            switch (netz.getStatus()) {
                case 1:
                    marker.setIcon("/ghostnets-0.1/resources/img/Marker_Warning_r_32px.png");
                    meinModell.addOverlay(marker);
                    break;
                case 2:
                    marker.setIcon("/ghostnets-0.1/resources/img/Marker_Flag_32px.png");
                    meinModell.addOverlay(marker);
                    break;
                case 3:
                    break;
                case 4:
                    marker.setIcon("/ghostnets-0.1/resources/img/Marker_Question_32px.png");
                    meinModell.addOverlay(marker);
                    break;
            }
        }
    }


    // Zusätzliche Methoden für Rendering der Infobox auf Karte
    public String statusTextAbfrage(int statusZahl) {
        String statusText = null;
        switch (statusZahl) {
            case 1:
                statusText = "gemeldet";
                break;
            case 2:
                statusText = "Bergung bevorstehend";
                break;
            case 3:
                statusText = "geborgen";
                break;
            case 4:
                statusText = "verschollen";
                break;
        }
        return statusText;
    }
    public int statusZahlAbfrage(String statusText) {
        int statusZahl = 0;
        switch (statusText) {
            case "gemeldet":
                statusZahl = 1;
                break;
            case "Bergung bevorstehend":
                statusZahl = 2;
                break;
            case "geborgen":
                statusZahl = 3;
                break;
            case "verschollen":
                statusZahl = 4;
                break;
        }
        return statusZahl;
    }

    public String umwandlungMarkerDaten(String netzIDString) {
        int netzID = Integer.parseInt(netzIDString);
        Netz netzX = netzDAO.findeNetzNachID(netzID);
        String markerDaten = "ID-Nr.: " + netzX.getNetzID() + ", Status: " + statusTextAbfrage(netzX.getStatus()) + ", Durchmesser: " + netzX.getDurchmesser() + " m, Letzte Sichtung: " + zeitUmwandlung(sichtungMeldungDAO.findeLetzteSichtung(netzX.getNetzID()));
        return markerDaten;
    }

    private String zeitUmwandlung(Timestamp timestamp) {
        if (timestamp == null) {
            return "n.a.";
        }
        else {
            SimpleDateFormat datumsFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
            return datumsFormat.format(timestamp);
        }
    }

    public MapModel getMeinModell() {
        return meinModell;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }

    public Marker getMarker() {
        return marker;
    }

    public boolean isErfolgteBergungMelden() {
        return erfolgteBergungMelden;
    }

    public void setErfolgteBergungMeldenTrue(String netzIDString) {
        sessionData.setNetzIDMarker(Integer.parseInt(netzIDString));
        erfolgteBergungMelden = true;
        geplanteBergungMelden = false;
        erneuteSichtungMelden = false;
        verschollenMelden = false;
    }

    public boolean isGeplanteBergungMelden() {
        return geplanteBergungMelden;
    }

    public void setGeplanteBergungMeldenTrue(String netzIDString) {
        sessionData.setNetzIDMarker(Integer.parseInt(netzIDString));
        erfolgteBergungMelden = false;
        geplanteBergungMelden = true;
        erneuteSichtungMelden = false;
        verschollenMelden = false;
    }

    public boolean isErneuteSichtungMelden() {
        return erneuteSichtungMelden;
    }

    public void setErneuteSichtungMeldenTrue(String netzIDString) {
        sessionData.setNetzIDMarker(Integer.parseInt(netzIDString));
        erfolgteBergungMelden = false;
        geplanteBergungMelden = false;
        erneuteSichtungMelden = true;
        verschollenMelden = false;
    }

    public boolean isVerschollenMelden() {
        return verschollenMelden;
    }

    public void setVerschollenMeldenTrue(String netzIDString) {
        sessionData.setNetzIDMarker(Integer.parseInt(netzIDString));
        erfolgteBergungMelden = false;
        geplanteBergungMelden = false;
        erneuteSichtungMelden = false;
        verschollenMelden = true;
    }
}

