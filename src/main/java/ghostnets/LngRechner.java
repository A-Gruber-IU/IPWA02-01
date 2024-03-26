package ghostnets;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class LngRechner implements Serializable {

    private double grad;
    private double minuten;
    private double sekunden;
    private double dezimal;

    public double getGrad() {
        return grad;
    }
    public void setGrad(double grad) {
        this.grad = grad;
    }
    public double getMinuten() {
        return minuten;
    }
    public void setMinuten(double minuten) {
        this.minuten = minuten;
    }
    public double getSekunden() {
        return sekunden;
    }
    public void setSekunden(double sekunden) {
        this.sekunden = sekunden;
    }
    public double getDezimal() {
        return dezimal;
    }
    public void setDezimal(double dezimal) {
        this.dezimal = dezimal;
    }

    public double umrechnenZuDezimal() {
        dezimal = grad + (minuten / 60) + (sekunden / 3600);
        return dezimal;
    }
}