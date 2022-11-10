import java.util.ArrayList;

public class Sommet {

    int id;
    Couleur couleur;
    ArrayList<Sommet> voisins;
    ArrayList<Sommet> voisinsPrefs;
    private int degre;


    public Sommet(int id) {
        this.id = id;
        this.couleur = new Couleur();
        this.voisins = new ArrayList<Sommet>();
        this.voisinsPrefs = new ArrayList<Sommet>();
        this.degre = 0;
    }

    //GETTERS

    public int getId() {
        return id;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public ArrayList<Sommet> getVoisins() {
        return voisins;
    }

    public ArrayList<Sommet> getVoisinsPrefs() {
        return voisinsPrefs;
    }

    private ArrayList<Sommet> getVoisinsNonPrefs() {
        ArrayList<Sommet> voisinsNonPrefs = new ArrayList<Sommet>();
        for (Sommet voisin : this.voisins) {
            if (!this.voisinsPrefs.contains(voisin)) {
                voisinsNonPrefs.add(voisin);
            }
        }
        return voisinsNonPrefs;
    }

    public int getDegre() {
        return this.degre;
    }

    //SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setCouleur(Couleur couleur) {
        this.couleur = couleur;
    }

    public void setVoisins(ArrayList<Sommet> voisins) {
        this.voisins = voisins;
        this.degre = voisins.size();
    }

    public void setVoisinsPrefs(ArrayList<Sommet> voisinsPrefs) {
        this.voisinsPrefs = voisinsPrefs;
    }

    public void ajouterVoisin(Sommet voisin) {
        this.voisins.add(voisin);
        this.degre++;
    }

    public void ajouterVoisinPref(Sommet voisinPref) {
        this.voisinsPrefs.add(voisinPref);
    }

    public void retirerVoisin(Sommet voisin) {
        this.voisins.remove(voisin);
        this.degre--;
    }

    public void retirerVoisinPref(Sommet voisinPref) {
        this.voisinsPrefs.remove(voisinPref);
    }
    public boolean checkVoisinPref(int id) {
        for (Sommet voisin : this.voisinsPrefs) {
            if (voisin.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public void baisserDegre() {
        this.degre--;
    }

    public void clearDegre() {
        this.degre = -1;
    }

    public boolean estTraite() {
        return (this.degre <= -1 || (this.couleur.getId() > -1));
    }

    public ArrayList<Couleur> checkCouleursPossibles(ArrayList<Couleur> couleurs) {
        for (Sommet voisin : this.getVoisinsNonPrefs()) {
            if (voisin.getCouleur().getId() > -1) {
                couleurs.remove(voisin.getCouleur());
            }
        }
        return couleurs;
    }

    public ArrayList<Couleur> checkCouleursPrefs(ArrayList<Couleur> couleursDispo) {
        ArrayList<Couleur> couleursPrefs = new ArrayList<Couleur>();
        for (Sommet voisin : this.getVoisinsPrefs()) {
            if ( voisin.getCouleur().getId() != -1){
                //on ajoute la couleur du voisin au tableau des couleurs préférées
                couleursPrefs.add(voisin.getCouleur());
            }else {
                //on ajoute les couleurs possibles du voisin au tableau des couleurs préférées
                couleursPrefs.addAll(voisin.checkCouleursPossibles(couleursDispo));
            }
        }
        return couleursPrefs;
    }


    @Override
    public String toString() {
        return "Sommet{" +
                "id=" + id +
                ", couleur=" + couleur +
                ", degre=" + degre +
                '}';
    }
}
