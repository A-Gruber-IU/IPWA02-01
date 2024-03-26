package ghostnets;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.FilterMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class DatenAnsicht implements Serializable {

    private List<Netz> netze;
    private List<Netz> gefilterteNetze;
    private List<FilterMeta> filterAktiv;

    @Inject
    private NetzDAO netzDAO;

    public List<Netz> getNetze() {
        return netze;
    }

    public void setNetze(List<Netz> netze) {
        this.netze = netze;
    }

    @PostConstruct
    public void init() {

        netze = netzDAO.findAll();
        filterAktiv = new ArrayList<>();
    }

    public List<Netz> getGefilterteNetze() {
        return gefilterteNetze;
    }

    public void setGefilterteNetze(List<Netz> gefilterteNetze) {
        this.gefilterteNetze = gefilterteNetze;
    }

    public List<FilterMeta> getFilterAktiv() {
        return filterAktiv;
    }

    public void setFilterAktiv(List<FilterMeta> filterAktiv) {
        this.filterAktiv = filterAktiv;
    }

    // Zusätzliche Methode für Umwandlung Status in Text
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
}

