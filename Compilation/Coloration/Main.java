import java.util.Scanner;

public class Main {



    public static void main(String[] args) {

        //creation du graphe
        Graphe g = new Graphe();

        //creation des sommets
        Sommet s0 = new Sommet(0);
        Sommet s1 = new Sommet(1);
        Sommet s2 = new Sommet(2);
        Sommet s3 = new Sommet(3);
        Sommet s4 = new Sommet(4);
        Sommet s5 = new Sommet(5);

        //ajout des sommets au graphe
        g.ajouterSommet(s0);
        g.ajouterSommet(s1);
        g.ajouterSommet(s2);
        g.ajouterSommet(s3);
        g.ajouterSommet(s4);
        g.ajouterSommet(s5);

        //ajout des voisins
        s0.ajouterVoisin(s1);
        s0.ajouterVoisin(s4);
        s0.ajouterVoisin(s5);

        s1.ajouterVoisin(s0);
        s1.ajouterVoisin(s3);
        s1.ajouterVoisin(s4);

        s2.ajouterVoisin(s5);

        s3.ajouterVoisin(s1);
        s3.ajouterVoisin(s4);
        s3.ajouterVoisin(s5);

        s4.ajouterVoisin(s0);
        s4.ajouterVoisin(s1);
        s4.ajouterVoisin(s3);

        s5.ajouterVoisin(s0);
        s5.ajouterVoisin(s2);
        s5.ajouterVoisin(s3);

        //ajout des voisins preferes
        s3.ajouterVoisinPref(s4);
        s4.ajouterVoisinPref(s3);

        //initialisation de la matrice d'adjacence
        g.initMatrice(g.getSommets());


        //affichage des sommets
        System.out.println("Graphe :");
        g.afficherSommets();

        //affichage de la matrice d'adjacence
        System.out.println("Matrice d'adjacence : ");
        g.afficherMatrice();

        System.out.println("graphe créé on va colorer");

        //coloriage du graphe
        //demande à l'utilisateur de saisir le nombre de couleurs

        int nbCouleurs;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("Saisir le nombre de couleurs (degré de la k-coloration) : ");
            nbCouleurs = sc.nextInt();
        } while (nbCouleurs < 1);

        ColoriageApp coloriage = new ColoriageApp(g , nbCouleurs);

        //et là on prie XD
        coloriage.colorierGraphe();


    }


    /*public static void main(String[] args) {

        Graphe g = new Graphe();

        //creation de la matrice d'adjacence
        int[][] mA = new int[5][5];

        //initialisation de la matrice d'adjacence
        mA[0] = new int[]{0,1,1,1,2};
        mA[1] = new int[]{1,0,1,1,1};
        mA[2] = new int[]{1,1,0,1,1};
        mA[3] = new int[]{1,1,1,0,1};
        mA[4] = new int[]{2,1,1,1,0};

        g.setMatriceAdjacence(mA);

        //initialisation du graphe
        g.initSommets(mA);

        //affichage de la matrice d'adjacence
        System.out.println("Matrice d'adjacence : ");
        g.afficherMatrice();

        //affichage des sommets
        System.out.println("Graphe :");
        g.afficherSommets();


        System.out.println("graphe créé on va colorer");

        //coloriage du graphe
        //demande à l'utilisateur de saisir le nombre de couleurs

        int nbCouleurs;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("Saisir le nombre de couleurs (degré de la k-coloration) : ");
            nbCouleurs = sc.nextInt();
        } while (nbCouleurs < 1);

        ColoriageApp coloriage = new ColoriageApp(g , nbCouleurs);

        //et là on prie XD
        coloriage.colorierGraphe();
    }*/
}