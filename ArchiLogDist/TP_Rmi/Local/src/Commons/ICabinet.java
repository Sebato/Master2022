package Commons;

import server.Animal;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ICabinet extends Remote {
//
//    //METHODS
//
//    String idAnimal()throws RemoteException;
//    void printAnimal() throws RemoteException;
//    void printDossier() throws RemoteException;
//
//    ////////////////////////////////////////////////////////
//
//    //GET
//
//    String getNom()throws RemoteException;
//    String getNomMaitre()throws RemoteException;
//    Espece getEspece()throws RemoteException;
//    String getRace()throws RemoteException;
//    Dossier_Suivi getDossier_suivi()throws RemoteException;
//
//
//    //SET
//
//    void setNom(String n)throws RemoteException;
//    void setNomMaitre(String nm)throws RemoteException;
//    void setEspece(Espece e)throws RemoteException;
//    void setRace(String race)throws RemoteException;
//    void setDossier(Dossier_Suivi dossier)throws RemoteException;

    void addPatient(Animal a) throws RemoteException;

    ArrayList<Animal> getListePatients() throws RemoteException;

    void setListePatients(ArrayList<Animal> listePatients) throws RemoteException;

    IAnimal searchAnimal(String nom) throws RemoteException;

    Animal getAnimal(int i) throws RemoteException;

    String StringListePatients()throws RemoteException;
    void printCabinet() throws RemoteException;

    String infoString() throws RemoteException;

    void ajoutPatient(String nom, String nomMaitre, Espece espece, String race) throws RemoteException ;
}

