import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ICabinet extends Remote {

    void addPatient(IAnimal a) throws RemoteException;

    ArrayList<IAnimal> getListePatients() throws RemoteException;

    void setListePatients(ArrayList<IAnimal> listePatients) throws RemoteException;

    IAnimal searchAnimal(String nom) throws RemoteException;

    IAnimal getAnimal(int i) throws RemoteException;

    String StringListePatients()throws RemoteException;
    void printCabinet() throws RemoteException;

    String infoString() throws RemoteException;

    void ajoutPatient(String nom, String nomMaitre, Espece espece, String race) throws RemoteException ;
}

