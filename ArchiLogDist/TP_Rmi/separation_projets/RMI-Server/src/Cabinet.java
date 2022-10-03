
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Cabinet extends UnicastRemoteObject implements ICabinet {

    private ArrayList<IAnimal> listePatients;

    public Cabinet() throws RemoteException {
        this.listePatients = new ArrayList<>();
    }

    public ArrayList<IAnimal> getListePatients() throws RemoteException {
        return listePatients;
    }

    public void setListePatients(ArrayList<IAnimal> listePatients) throws RemoteException {
        this.listePatients = listePatients;
    }

    public IAnimal searchAnimal(String nom) throws RemoteException{
        for (IAnimal a: this.listePatients) {
            //System.out.println("debug "+a.getNom());
            //System.out.println("valeur = "+a.getNom().equals(nom));

            if (a.getNom().equals(nom)){
                return a;}
        }
        return null;
    }
    public IAnimal getAnimal(int i)throws RemoteException
    {
        return this.listePatients.get(i);
    }

    public void addPatient(IAnimal a) throws RemoteException
    {
        this.listePatients.add(a);
    }

    public void ajoutPatient(String nom, String nomMaitre, Espece espece, String race) throws RemoteException {

        this.listePatients.add(new Animal(nom, nomMaitre, espece, race));
    }


    public String StringListePatients()throws RemoteException
    {
          StringBuilder listP= new StringBuilder();

        for (IAnimal a: this.listePatients) {
            //System.out.println(a.idAnimal());
            listP.append("\n").append(a.idAnimal());
        }
        return listP.toString();
    }

    public void printCabinet() throws RemoteException{
        System.out.println("Server printing :\n");
        for (IAnimal a: this.listePatients) {
            try {
                a.printAnimal();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String infoString() throws RemoteException{
        return "\"LISTE DES PATIENTS :\n" + this.StringListePatients();
    }
}
