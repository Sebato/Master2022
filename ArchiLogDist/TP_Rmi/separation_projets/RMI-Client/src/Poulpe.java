import java.io.Serializable;

public class Poulpe extends Espece implements Serializable {
    public Poulpe() {
        super("Poulpe", 2);
    }

    public void jetdencre()
    {
        System.out.println(this.getNomEsp()+" attaque jet d'encre!");
    }
}
