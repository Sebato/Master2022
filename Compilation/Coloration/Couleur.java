public class Couleur {

    private int id;

    public Couleur(int id) {
        this.id = id;
    }

    public Couleur(){
        this.id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean equals(Couleur couleur) {
        return this.id == couleur.getId();
    }

    @Override
    public String toString() {
        return this.id + "";
    }

}
