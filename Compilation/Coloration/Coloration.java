import java.util.ArrayList;
import java.util.Scanner;

public class Coloration {

    public class ColoriageApp {

        private final ArrayList<Couleur> colorTab;
        private final Graphe graphe;
        private final int k;

        private ArrayList<Sommet> sommetsSpill;

        public ColoriageApp(Graphe graphe, int k) {
            this.graphe = graphe;
            this.k = k;
            this.colorTab = new ArrayList<>();
            initCouleurs();
            this.sommetsSpill = new ArrayList<>();
        }

        //initialise le tableau de couleurs en fonction du degré de coloriage voulu
        public void initCouleurs() {
            for (int i = 0; i < k; i++) {
                Couleur couleur = new Couleur(i);
                colorTab.add(couleur);
            }
        }

        //verifie si le sommet passé en paramètre est trivialement coloriable
        public boolean checkTrivColorable(Sommet sommet) {
            return (!sommet.estTraite() && (sommet.getDegre() < k));
        }

        //vérifie si le graphe possède un sommet trivialement coloriable et donne son id si oui
        public int existeTrivColorable() {
            for (Sommet sommet : graphe.getSommets()) {
                if (checkTrivColorable(sommet)) {
                    return sommet.getId();
                }
            }
            return -1;
        }

        //attribue une couleur disponible au sommet passé en paramètre
        public void colorerSommetV2(Sommet sommet) {
            ArrayList<Couleur> couleursDispo = new ArrayList<>(colorTab);
            System.out.println("\nCOLORER_SOMMET couleursDispo : " + couleursDispo+"\n");
            ArrayList<Couleur> couleursPrefs;


            //on retire des dispos les couleurs des voisins non preférés
            couleursDispo = sommet.checkCouleursPossibles(couleursDispo);

            System.out.println("couleursDispo : " + couleursDispo);

            //on récupère les couleurs préférées des voisins (coloriés ou non)
            couleursPrefs = sommet.checkCouleursPrefs(couleursDispo);
            System.out.println("couleursPrefs : " + couleursPrefs);


            //on compare les couleurs disponibles et les couleurs préférées
            for (Couleur couleurPref : couleursPrefs) {
                if (couleursDispo.contains(couleurPref)) {
                    //si la pref est dispo, on l'attribue
                    sommet.setCouleur(couleurPref);
                    //à voir si on choisit plutot celle qui vient le plus souvent dans l'intersection des prefs et dispos
                    return;
                }
            }

            //si aucune couleur pref n'est dispo, on attribue la première couleur dispo
            sommet.setCouleur(couleursDispo.get(0));
            for(Sommet voisin : sommet.getVoisins()) {
                voisin.baisserDegre();
            }
        }

        //Spiller le sommet
        public void spillerSommet(Sommet sommet) {

            this.sommetsSpill.add(sommet);
            sommet.clearDegre();

            //quand on spille on retire le sommet du graphe et des listes de ses voisins ?
            //(pour pas qu'il soit traité)
            //en soi on a toujours la matrice pour récupérer le graphe de départ

            for(Sommet voisin : sommet.getVoisins()) {
                voisin.baisserDegre();
            }
        }

        public void colorierGraphe() {
            System.out.println("Début coloration");

            while (!this.graphe.toutEstTraite()) {
                System.out.println("nb sommet spill : " + this.sommetsSpill.size());

                int trivID = existeTrivColorable();
                System.out.println("\n - post test TrivColo : "+trivID);

                if (trivID!= -1) {

                    Sommet sommetEnTraitement = this.graphe.getSommets().get(trivID);
                    System.out.println(" - sommet en traitement : "+sommetEnTraitement);

                    colorerSommetV2(sommetEnTraitement);

                    System.out.println(" - sommet coloré : "+sommetEnTraitement+"\n");
                }
                else {
                    System.out.println("pas de triv Coloriable je vais spiller.");

                    int spillID = choixSommetSpillV2();
                    /*System.out.println(" DEBUG : "+this.graphe.getSommets());*/

                    Sommet sommetASpiller = this.graphe.getSommets().get(spillID);
                    System.out.println(" - sommet a spiller : "+sommetASpiller);

                    spillerSommet(sommetASpiller);

                    System.out.println("  - spill fait : "+sommetASpiller);
                }
            }
            System.out.println("----------Fin coloration----------\n");
            System.out.println("nb sommets Graphe : " + this.graphe.getNbSommets());
            System.out.println("nb aretes Graphe : " + this.graphe.getNbAretes());
            System.out.println("degré coloration : " + k);
            System.out.println("nb sommets spillés : " + this.sommetsSpill.size());
            System.out.println("sommets spillés : " + this.sommetsSpill+"\n");

            //this.graphe.afficherMatrice();
            this.graphe.afficherSommets();
        }

        //choisit le sommet de plus haut degré parmi en priorisant ceux qui n'ont pas de préférence
        private int choixSommetSpillV2() {
            int maxDegre = 0;
            int idSommet = -1;
            for (Sommet sommet : graphe.getSommets()) {
                System.out.println(" - sommet : "+sommet);
                if (sommet.getVoisinsPrefs().isEmpty() && !sommet.estTraite() && (sommet.getDegre() > maxDegre)) {
                    maxDegre = sommet.getDegre();
                    idSommet = sommet.getId();
                }
            }
            //si aucun sommet n'a été trouvé on fait à l'ancienne, on prend juste le plus haut degré
            if (idSommet == -1) {
                for (Sommet sommet : graphe.getSommets()) {
                    if (!sommet.estTraite() && (sommet.getDegre() > maxDegre)) {
                        maxDegre = sommet.getDegre();
                        idSommet = sommet.getId();
                    }
                }
            }
            return idSommet;
        }

        //choisit le sommet de plus haut degré
        private int choixSommetSpill() {
            int maxDegre = 0;
            int idSommet = -1;
            for (Sommet sommet : graphe.getSommets()) {
                System.out.println(" - sommet : "+sommet);
                if (!sommet.estTraite() && (sommet.getDegre() > maxDegre)) {
                    maxDegre = sommet.getDegre();
                    idSommet = sommet.getId();
                }
            }
            return idSommet;
        }
    }
    //////////////////////////////////////////
    public class Couleur {

        private int id;

        public Couleur(int id) {
            this.id = id;
        }

        public Couleur(){
            this.id = -1;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean equals(Couleur couleur) {
            return this.id == couleur.getId();
        }

        @Override
        public String toString() {
            return this.id + "";
        }

    }
    //////////////////////////////////////////
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

        //plus de sommets à traiter, ça sent la fin quand même
        public boolean toutEstTraite() {
            for (Sommet s : this.sommets) {
                if (!s.estTraite()) {
                    return false;
                }
            }
            return true;
        }

        // DISPLAY

        //affiche la matrice d'adjacence
        public void afficherMatrice() {
            for (int i = 0; i < this.nbSommets; i++) {
                for (int j = 0; j < this.nbSommets; j++) {
                    System.out.print(this.matriceAdjacence[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }

        //affiche la liste des sommets
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
    }
    //////////////////////////////////////////
    public class Sommet {

        int id;
        Couleur couleur;
        ArrayList<Sommet> voisins;
        ArrayList<Sommet> voisinsPrefs;
        private int degre;


        public Sommet(int id) {
            this.id = id;
            this.couleur = new Couleur();
            this.voisins = new ArrayList<Sommet>();
            this.voisinsPrefs = new ArrayList<Sommet>();
            this.degre = 0;
        }

        //GETTERS

        public int getId() {
            return id;
        }

        public Couleur getCouleur() {
            return couleur;
        }

        public ArrayList<Sommet> getVoisins() {
            return voisins;
        }

        public ArrayList<Sommet> getVoisinsPrefs() {
            return voisinsPrefs;
        }

        private ArrayList<Sommet> getVoisinsNonPrefs() {
            ArrayList<Sommet> voisinsNonPrefs = new ArrayList<Sommet>();
            for (Sommet voisin : this.voisins) {
                if (!this.voisinsPrefs.contains(voisin)) {
                    voisinsNonPrefs.add(voisin);
                }
            }
            return voisinsNonPrefs;
        }

        public int getDegre() {
            return this.degre;
        }

        //SETTERS

        public void setId(int id) {
            this.id = id;
        }

        public void setCouleur(Couleur couleur) {
            this.couleur = couleur;
        }

        public void setVoisins(ArrayList<Sommet> voisins) {
            this.voisins = voisins;
            this.degre = voisins.size();
        }

        public void setVoisinsPrefs(ArrayList<Sommet> voisinsPrefs) {
            this.voisinsPrefs = voisinsPrefs;
        }

        public void ajouterVoisin(Sommet voisin) {
            this.voisins.add(voisin);
            this.degre++;
        }

        public void ajouterVoisinPref(Sommet voisinPref) {
            this.voisinsPrefs.add(voisinPref);
        }

        public void retirerVoisin(Sommet voisin) {
            this.voisins.remove(voisin);
            this.degre--;
        }

        public void retirerVoisinPref(Sommet voisinPref) {
            this.voisinsPrefs.remove(voisinPref);
        }
        public boolean checkVoisinPref(int id) {
            for (Sommet voisin : this.voisinsPrefs) {
                if (voisin.getId() == id) {
                    return true;
                }
            }
            return false;
        }

        public void baisserDegre() {
            this.degre--;
        }

        public void clearDegre() {
            this.degre = -1;
        }

        public boolean estTraite() {
            return (this.degre <= -1 || (this.couleur.getId() > -1));
        }

        public ArrayList<Couleur> checkCouleursPossibles(ArrayList<Couleur> couleurs) {
            for (Sommet voisin : this.getVoisinsNonPrefs()) {
                if (voisin.getCouleur().getId() > -1) {
                    couleurs.remove(voisin.getCouleur());
                }
            }
            return couleurs;
        }

        public ArrayList<Couleur> checkCouleursPrefs(ArrayList<Couleur> couleursDispo) {
            ArrayList<Couleur> couleursPrefs = new ArrayList<Couleur>();
            for (Sommet voisin : this.getVoisinsPrefs()) {
                if ( voisin.getCouleur().getId() != -1){
                    //on ajoute la couleur du voisin au tableau des couleurs préférées
                    couleursPrefs.add(voisin.getCouleur());
                }else {
                    //on ajoute les couleurs possibles du voisin au tableau des couleurs préférées
                    couleursPrefs.addAll(voisin.checkCouleursPossibles(couleursDispo));
                }
            }
            return couleursPrefs;
        }


        @Override
        public String toString() {
            return "Sommet{" +
                    "id=" + id +
                    ", couleur=" + couleur +
                    ", degre=" + degre +
                    '}';
        }
    }
    //////////////////////////////////////////


}
//class Main2 {
//
//
//
//    public static void main(String[] args) {
//
//        //creation du graphe
//        Graphe g = new Graphe();
//
//        //creation des sommets
//        Sommet s0 = new Sommet(0);
//        Sommet s1 = new Sommet(1);
//        Sommet s2 = new Sommet(2);
//        Sommet s3 = new Sommet(3);
//        Sommet s4 = new Sommet(4);
//        Sommet s5 = new Sommet(5);
//
//        //ajout des sommets au graphe
//        g.ajouterSommet(s0);
//        g.ajouterSommet(s1);
//        g.ajouterSommet(s2);
//        g.ajouterSommet(s3);
//        g.ajouterSommet(s4);
//        g.ajouterSommet(s5);
//
//        //ajout des voisins
//        s0.ajouterVoisin(s1);
//        s0.ajouterVoisin(s4);
//        s0.ajouterVoisin(s5);
//
//        s1.ajouterVoisin(s0);
//        s1.ajouterVoisin(s3);
//        s1.ajouterVoisin(s4);
//
//        s2.ajouterVoisin(s5);
//
//        s3.ajouterVoisin(s1);
//        s3.ajouterVoisin(s4);
//        s3.ajouterVoisin(s5);
//
//        s4.ajouterVoisin(s0);
//        s4.ajouterVoisin(s1);
//        s4.ajouterVoisin(s3);
//
//        s5.ajouterVoisin(s0);
//        s5.ajouterVoisin(s2);
//        s5.ajouterVoisin(s3);
//
//        //ajout des voisins preferes
//        s3.ajouterVoisinPref(s4);
//        s4.ajouterVoisinPref(s3);
//
//        //initialisation de la matrice d'adjacence
//        g.initMatrice(g.getSommets());
//
//
//        //affichage des sommets
//        System.out.println("Graphe :");
//        g.afficherSommets();
//
//        //affichage de la matrice d'adjacence
//        System.out.println("Matrice d'adjacence : ");
//        g.afficherMatrice();
//
//        System.out.println("graphe créé on va colorer");
//
//        //coloriage du graphe
//        //demande à l'utilisateur de saisir le nombre de couleurs
//
//        int nbCouleurs;
//        Scanner sc = new Scanner(System.in);
//
//        do {
//            System.out.println("Saisir le nombre de couleurs (degré de la k-coloration) : ");
//            nbCouleurs = sc.nextInt();
//        } while (nbCouleurs < 1);
//
//        ColoriageApp coloriage = new ColoriageApp(g , nbCouleurs);
//
//        //et là on prie XD
//        coloriage.colorierGraphe();
//
//
//    }
//}
