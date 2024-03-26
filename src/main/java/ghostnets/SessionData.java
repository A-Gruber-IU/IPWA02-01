package ghostnets;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@SessionScoped
public class SessionData implements Serializable {
    private int netzIDMarker;

    public int getNetzIDMarker() {
        return netzIDMarker;
    }

    public void setNetzIDMarker(int netzIDMarker) {
        this.netzIDMarker = netzIDMarker;
    }
}
