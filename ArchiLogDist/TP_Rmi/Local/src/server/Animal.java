package server;

import Commons.Espece;
import Commons.IAnimal;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Animal extends UnicastRemoteObject implements IAnimal, Serializable {

    private String nom;
    private String nomMaitre;
    private Espece espece;
    private String race;
    private Dossier_Suivi dossier;

    //GET
    public String getNom() {
        return nom;
    }
    public String getNomMaitre() {
        return nomMaitre;
    }
    public Espece getEspece() {
        return espece;
    }
    public String getRace() {
        return race;
    }
    public Dossier_Suivi getDossier_suivi() {
        return dossier;
    }

    //SET
    public void setNom(String n) {
        this.nom = n;
    }
    public void setNomMaitre(String nm) {
        this.nomMaitre = nm;
    }
    public void setEspece(Espece e) {
        this.espece = e;
    }
    public void setRace(String race) {
        this.race = race;
    }
    public void setDossier(Dossier_Suivi dossier) {
        this.dossier = dossier;
    }

    //CONSTRUCTORS

    public Animal() throws RemoteException {
        super();
    }
    public Animal(String nom, String nomMaitre, Espece espece, String race) throws RemoteException {
        setNom(nom);
        setNomMaitre(nomMaitre);
        setEspece(espece);
        setRace(race);
    }
    public Animal(String nom, String nomMaitre, Espece espece, String race, Dossier_Suivi dossier) throws RemoteException {
        setNom(nom);
        setNomMaitre(nomMaitre);
        setEspece(espece);
        setRace(race);
        setDossier(dossier);
    }

    //METHODS

    public String idAnimal() {
        return "\"" + this.getNom() + "\" le " + this.getEspece().getNomEsp() + " " + this.getRace() + " apartenant à " + this.getNomMaitre();
    }
    public void printAnimal() throws RemoteException {
        System.out.println("Server printing :" + this.idAnimal());
    }
    public void printDossier() throws RemoteException {
        System.out.println("Server printing :" + this.getInfoDossier());
    }
    public String getInfoDossier() throws RemoteException{
        return this.dossier.getDossier();
    }
}