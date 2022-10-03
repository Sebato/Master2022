import java.io.Serializable;

public class Dossier_Suivi implements Serializable {
    private String Dossier;

    public String getDossier() {
        return Dossier;
    }

    public void setDossier(String dossier) {
        Dossier = dossier;
    }

    public Dossier_Suivi() {}
    public Dossier_Suivi(String info) {this.Dossier=info;}

}

