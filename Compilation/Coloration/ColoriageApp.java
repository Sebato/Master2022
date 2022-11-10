import java.util.ArrayList;

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
    public void colorerSommet(Sommet sommet) {
        ArrayList<Couleur> couleursDispo = new ArrayList<>(colorTab);

        /*System.out.println("\nCOLORER_SOMMET couleursDispo : " + couleursDispo+"\n");*/

                //parmi les voisins du sommet
        for (Sommet voisin : sommet.getVoisins()) {
                //si le voisin n'est pas un voisin préféré et qu'il est colorié
            if ( (!sommet.checkVoisinPref(voisin.getId())) && (voisin.getCouleur().getId() != -1)) {
                //on retire la couleur du voisin du tableau des couleurs disponibles
                couleursDispo.remove(voisin.getCouleur());
            }
           /* System.out.println("voisin : " + voisin.getId() + " couleur : " + voisin.getCouleur().getId());
            System.out.println("couleursDispo : " + couleursDispo+"");*/
        }

       /* System.out.println("finish : " + couleursDispo.get(0)+"\n");*/

        //on attribue la première couleur disponible
        sommet.setCouleur(couleursDispo.get(0));
        //A voir après pour colorier en fonction d'un voisin préféré

        // ( et si jamais il n'y a pas de couleur disponible ? )
        // ( normalement on appelle cette fonction que si le sommet est trivialement coloriable )
        // ( donc en théorie il y aurait forcément une couleur disponible ? )
        // ( ¯\_(ツ)_/¯ )

        for(Sommet voisin : sommet.getVoisins()) {
            voisin.baisserDegre();
        }
    }

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
