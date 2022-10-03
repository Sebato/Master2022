import java.io.Serializable;

public class Espece implements Serializable {

    private final String nomEsp;
    private final int pv;

    public Espece(String e, int pv) {
        this.nomEsp = e;
        this.pv = pv;
    }

    public String getNomEsp() {
        return nomEsp;
    }

//    public void setNomEsp(String e) {
//        this.nomEsp = e;
//    }

    public int getPv() {
        return pv;
    }

//    public void setPv(int pv) {
//        this.pv = pv;
//    }
}
