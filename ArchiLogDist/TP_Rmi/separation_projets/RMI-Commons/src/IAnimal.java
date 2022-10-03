import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAnimal extends Remote {

        //METHODS

        String idAnimal()throws RemoteException;
        void printAnimal() throws RemoteException;
        void printDossier() throws RemoteException;

        ////////////////////////////////////////////////////////

        //GET

        String getNom()throws RemoteException;
        String getNomMaitre()throws RemoteException;
        Espece getEspece()throws RemoteException;
        String getRace()throws RemoteException;
        Dossier_Suivi getDossier_suivi()throws RemoteException;


        //SET

        void setNom(String n)throws RemoteException;
        void setNomMaitre(String nm)throws RemoteException;
        void setEspece(Espece e)throws RemoteException;
        void setRace(String race)throws RemoteException;
        void setDossier(Dossier_Suivi dossier)throws RemoteException;


}
