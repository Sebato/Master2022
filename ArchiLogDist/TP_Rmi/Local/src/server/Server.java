package server;

import Commons.Espece;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    /* CONSTRUCTOR */
    public Server() {

    }

    /* METHODS */
    public static void main(String[] args) {

        //System.setProperty("java.rmi.server.codebase", "file:/home/e20170009949/IdeaProjects/TPRMi1/out/production/TPRMi1/client/Poulpe.class");
        System.setProperty( "java.security.policy","/home/e20170009949/IdeaProjects/TPRMi1/src/server/animal.policy");
        SecurityManager security = new SecurityManager();

        // System.setSecurityManager(new SecurityManager());


        try {
            Espece chat = new Espece("chat", 9);
            Espece chien = new Espece("chien", 15);
            Espece rongeur = new Espece("rongeur", 2);

            Animal toto = new Animal("toto", "Tata", chat, "Siamois");
            Animal titi = new Animal("titi", "Tata", chat, "Persan");
            Animal tutu = new Animal("tutu", "Tata", chat, "Roux");
            Animal tete = new Animal("tete", "Joe", chat, "Gouttière");
            Animal truc = new Animal("truc", "Jack", chat, "Gouttière");

            Animal bidul = new Animal("bidul", "Joe", chien, "teckel");
            Animal bidule = new Animal("bidule", "Tata", chien, "caniche");
            Animal machin = new Animal("machin", "Ernest", chien, "dobermann");
            Animal machine = new Animal("machine", "Jack", chien, "PitBull");

            Animal chaussette = new Animal("chaussette", "Jack", rongeur, "hamster nain");
            Animal cthulhu = new Animal("cthulhu", "Peter", rongeur, "Monstre Océanique");

            Cabinet purina = new Cabinet();

            purina.addPatient(toto);
           // System.out.println(toto.idAnimal());
            purina.addPatient(titi);
            purina.addPatient(tete);
            purina.addPatient(truc);
            purina.addPatient(tutu);
            purina.addPatient(chaussette);
            purina.addPatient(cthulhu);
            purina.addPatient(bidule);
            purina.addPatient(bidul);
            purina.addPatient(machine);
            purina.addPatient(machin);

            System.out.println("Server : objets créés");

            //System.out.println(purina.infoString());

            Registry registry = LocateRegistry.createRegistry(1099);

            if (registry == null)
                System.err.println("Registry not found on port 1099");
            else {
                registry.bind("cab", purina);
                System.err.println("Server ready");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

    }
}
