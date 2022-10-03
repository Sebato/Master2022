package client;

import Commons.Espece;

import java.io.Serializable;
import java.util.List;

public class Poulpe extends Espece implements Serializable {
    public Poulpe() {
        super("Poulpe", 2);
    }

    public void jetdencre()
    {
        System.out.println(this.getNomEsp()+" attaque jet d'encre!");
    }
}
