package server;

import Commons.Espece;
import Commons.IAnimal;
import Commons.ICabinet;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Cabinet extends UnicastRemoteObject implements ICabinet {

    private ArrayList<Animal> listePatients;

    public Cabinet() throws RemoteException {
        this.listePatients = new ArrayList<>();
    }

    public ArrayList<Animal> getListePatients() throws RemoteException {
        return listePatients;
    }

    public void setListePatients(ArrayList<Animal> listePatients) throws RemoteException {
        this.listePatients = listePatients;
    }

    public IAnimal searchAnimal(String nom) throws RemoteException{
        for (Animal a: this.listePatients) {
            //System.out.println("debug "+a.getNom());
            //System.out.println("valeur = "+a.getNom().equals(nom));

            if (a.getNom().equals(nom)){
                return a;}
        }
        return null;
    }
    public Animal getAnimal(int i)throws RemoteException
    {
        return this.listePatients.get(i);
    }

    public void addPatient(Animal a) throws RemoteException
    {
        this.listePatients.add(a);
    }

    public void ajoutPatient(String nom, String nomMaitre, Espece espece, String race) throws RemoteException {

        this.listePatients.add(new Animal(nom, nomMaitre, espece, race));
        alertCab();
    }

    public void alertCab(){
        switch(this.listePatients.size()){

            case 12:
                System.out.println("100 clients atteints");
                break;

            case 200:
                System.out.println("200 clients atteints");
                break;

            case 500:
                System.out.println("500 clients atteints");
                break;
            default:
                break;
        }
    }

    public String StringListePatients()throws RemoteException
    {
          StringBuilder listP= new StringBuilder();

        for (Animal a: this.listePatients) {
            //System.out.println(a.idAnimal());
            listP.append("\n").append(a.idAnimal());
        }
        return listP.toString();
    }

    public void printCabinet() throws RemoteException{
        System.out.println("Server printing :\n");
        for (Animal a: this.listePatients) {
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
