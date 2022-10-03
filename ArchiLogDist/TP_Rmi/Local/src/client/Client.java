package client;

import Commons.Espece;
import Commons.IAnimal;
import Commons.ICabinet;
/*import server.Animal;*/
import server.Dossier_Suivi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client {
    private Client() {}

    public static void main(String[] args) {
        //String host = (args.length < 1)? null : args[0];
        //String host = "1099";
        System.out.println("Client : Start");
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            /*IAnimal obj = (IAnimal) registry.lookup("animal1");*/
            //System.out.println(obj.idAnimal());
            ICabinet obj = (ICabinet) registry.lookup("cab");
            String response = obj.infoString();
            //System.out.println("Response: " + response);
            //obj.printCabinet();

            IAnimal toto = obj.searchAnimal("toto");


            if (toto!= null) {
                Dossier_Suivi info = new Dossier_Suivi(toto.getNom() + " est en bonne santé");

                toto.setDossier(info);
                toto.printDossier();

                System.out.println(toto.getDossier_suivi().getDossier());
                System.out.println(obj.searchAnimal("toto").getDossier_suivi().getDossier());
            }else System.out.println("toto introuvable");


            /*Espece chien = new Espece("Chien", 15);*/
            Poulpe P = new Poulpe();
            obj.ajoutPatient("Momo", "Jojo", P, "huit tentacules");
            obj.searchAnimal("Momo").printAnimal();
            /*(Poulpe) obj.searchAnimal("Momo").getEspece().;*/


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
