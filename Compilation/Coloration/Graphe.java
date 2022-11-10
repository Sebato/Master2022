import java.util.ArrayList;

public class Graphe {

    int nbSommets;

    int nbAretes;

    ArrayList<Sommet> sommets;

    int[][] matriceAdjacence;

    public Graphe() {
        this.nbSommets = 0;
        this.nbAretes = 0;
        this.sommets = new ArrayList<Sommet>();
        this.matriceAdjacence = new int[nbSommets][nbSommets];
    }

    public Graphe(int nbSommets, ArrayList<Sommet> sommets, int[][] matriceAdjacence) {
        this.nbSommets = nbSommets;
        this.sommets = sommets;
        this.matriceAdjacence = matriceAdjacence;
        this.nbAretes = caclulnbAretes();
    }

    private int caclulnbAretes() {
        int nbAretes = 0;
        for (int i = 0; i < this.nbSommets; i++) {
            for (int j = 0; j < this.nbSommets; j++) {
                if (this.matriceAdjacence[i][j] >= 1) {
                    nbAretes++;
                }
            }
        }
        return nbAretes;
    }

    //GETTERS

    public int getNbSommets() {
        return nbSommets;
    }

    public int getNbAretes() {
        return nbAretes;
    }

    public ArrayList<Sommet> getSommets() {
        return sommets;
    }

    public int[][] getMatriceAdjacence() {
        return matriceAdjacence;
    }

    //SETTERS

    public void setMatriceAdjacence(int[][] mA) {
        this.nbSommets =  mA.length;
        this.matriceAdjacence = new int[nbSommets][nbSommets];

        for (int i = 0; i < this.nbSommets; i++) {
            for (int j = 0; j < this.nbSommets; j++) {
                this.matriceAdjacence[i][j] = mA[i][j];
            }
        }
        this.nbAretes = caclulnbAretes();
    }

    //METHODES

    //initialise la matrice d'adjacence à partir de la liste des sommets

    public void initMatrice (ArrayList<Sommet> sommets) {
        this.nbSommets = sommets.size();
        this.matriceAdjacence = new int[nbSommets][nbSommets];
        this.sommets = sommets;

        for( Sommet s : this.sommets ) {
            for( Sommet voisin : s.getVoisins() ) {
                this.matriceAdjacence[s.getId()][voisin.getId()] = 1;
                if (s.checkVoisinPref(voisin.getId())) {
                    this.matriceAdjacence[s.getId()][voisin.getId()] = 2;
                }
            }
        }
        this.nbAretes = caclulnbAretes();
    }
    //initialise la liste des sommets à partir d'une matrice d'adjacence

    public void initSommets(int[][] matriceAdjacence) {

        this.sommets = new ArrayList<>();
        int n = matriceAdjacence.length;

        //creation des sommets
        for (int i = 0; i < n; i++) {
            this.sommets.add(new Sommet(i));
        }

        //ajout des voisins
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matriceAdjacence[i][j] >= 1) {
                    this.sommets.get(i).ajouterVoisin(this.sommets.get(j));
                }
                if (matriceAdjacence[i][j] == 2) {
                    this.sommets.get(i).ajouterVoisinPref(this.sommets.get(j));
                }
            }
        }
    }



    public void ajouterSommet(Sommet s1) {
        this.sommets.add(s1);
        this.nbSommets++;
    }

    public void retirerSommet(Sommet sommet) {
        this.sommets.remove(sommet);
        this.nbSommets--;
    }

    //ne plus traiter un sommet qui a été spillé

    //DISPLAY

    public void afficherMatrice() {
        for (int i = 0; i < this.nbSommets; i++) {
            for (int j = 0; j < this.nbSommets; j++) {
                System.out.print(this.matriceAdjacence[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void afficherSommets() {
        System.out.println("Liste des sommets : ");
        for (Sommet s : this.sommets) {
            System.out.println("Sommet " + s.getId() + " :");
            System.out.println("Couleur : " + s.getCouleur());
            System.out.println("Degre : " + s.getDegre());
            System.out.println("Voisins : ");
            for (Sommet voisin : s.getVoisins()) {
                System.out.print(voisin.getId() + " ");
            }
            System.out.println();
            System.out.println("Voisins préférés : ");
            for (Sommet voisinPref : s.getVoisinsPrefs()) {
                System.out.print(voisinPref.getId() + " ");
            }
            System.out.println("\n");
        }
    }

    public boolean toutEstTraite() {
        for (Sommet s : this.sommets) {
            if (!s.estTraite()) {
                return false;
            }
        }
        return true;
    }
}
