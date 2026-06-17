package HAC;

import java.util.ArrayList;
import java.util.List;

// un arbre ce construit a l'envers
// ici on calcul les distances entre les groupes (enfants de l'arbre)
public class Cluster {
    // liste de tout les points
    private List<Integer> points;

    // Les deux groupes de points
    private Cluster c1;
    private Cluster c2;

    //la distance entre les points
    private double distance;

    // un id pour chaque groupe de points
    private int numGroupe;
    private Cluster groupe;

    // constructeur pour cree un enfant, id est l'id de l'enfant(le numero)
    // index c est l'index de la position du point dans la matrice (pour connaitre la distance avec les autres points)
    public Cluster(int id, int index) {
        this.points = new ArrayList<Integer>();
        this.points.add(index);
        this.c1=null;
        this.c2=null;
        this.distance=0;
        this.numGroupe=id;
    }

    // constructeur pour cree les noeuds internes
    // id est l'id du nouveau groupe
    // Cluster droite et gauche les enfants de la nouvelle branche (parent)
    // distance c est la distance entre les deux enfant (l'ecart de couleurs)
    public Cluster(int id, Cluster droite, Cluster gauche, double distance){
        this.numGroupe=id;
        this.c1=droite;
        this.c2=gauche;
        this.distance=distance;
        this.points=new ArrayList<Integer>();
        // fusion des points
        this.points.addAll(droite.getPoints());
        this.points.addAll(gauche.getPoints());

    }

    // calcul du voisin le plus proche
    public double singleLinkage(Cluster autre, double[][] distances){
        // on prends une distance grande
        double min = Double.MAX_VALUE;
        // pour chaque enfant de notre groupe
        for (int i=0; i<this.points.size(); i++){
            //numero du groupe
            int groupeG = this.points.get(i);
            // et chaque enfant de l'autre groupe
            for(int j=0; j<autre.getPoints().size(); j++) {
                // numero du groupe
                int groupeD = autre.getPoints().get(j);
                // distance entre les deux groupes
                double ecart = distances[groupeG][groupeD];
                // on prends l'ecart le plus petit
                if (ecart < min) {
                    min = ecart;
                }
            }
        }
        return min;

    }

    // calcul du voisin le plus eloigné
    public double completeLinkage(Cluster autre, double[][] distances){
        // on prends une distance petite
        double max = 0;
        // pour chaque enfant de notre groupe
        for (int i=0; i<this.points.size(); i++){
            //numero du groupe
            int groupeG = this.points.get(i);
            // et chaque enfant de l'autre groupe
            for(int j=0; j<autre.getPoints().size(); j++) {
                // numero du groupe
                int groupeD = autre.getPoints().get(j);
                // distance entre les deux groupes
                double ecart = distances[groupeG][groupeD];
                // on prends l'ecart le plus petit
                if (ecart > max) {
                    max = ecart;
                }
            }
        }
        return max;

    }

    // calcul de la distance moyenne entre les voisins
    public double averageLinkage(Cluster autre, double[][] distances){
        double somme = 0;
        int n = 0;
        // on prends tout les enfants
        for (int i=0; i<this.points.size(); i++){
            int groupeG = this.points.get(i);
            for(int j=0; j<autre.getPoints().size(); j++) {
                int groupeD = autre.getPoints().get(j);
                // on calcule les distances
                double ecart = distances[groupeG][groupeD];
                somme=somme+ecart;
                n++;
            }
        }

        // on fait la moyenne
        return somme/n;
    }

    // calcul de la distance entre les centres de chaque groupe
    public double centroidLinkage(Cluster autre, double[][] coordonnees){
        double sommexG = 0;
        double sommeyG = 0;
        for (int i=0; i<this.points.size(); i++){
            // recuperer id du point
            int idPointG = this.points.get(i);
            // on calcule la moyenne sur l'axe x
            sommexG += coordonnees[idPointG][0]; // Accumulation des X

            // on calcule la moyenne sur l'axe y
            sommeyG += coordonnees[idPointG][1];
        }
        // moyenne
        double centrexG = sommexG / this.points.size();
        double centreyG = sommeyG / this.points.size();


        double sommexD = 0;
        double sommeyD = 0;
        for (int j=0; j<autre.getPoints().size(); j++){
            // recuperer id du point
            int idPointD = autre.getPoints().get(j);
            // on calcule la moyenne sur l'axe x
            sommexD += coordonnees[idPointD][0]; // Accumulation des X

            // on calcule la moyenne sur l'axe y
            sommeyD += coordonnees[idPointD][1];
        }
        // moyenne
        double centrexD = sommexD / autre.getPoints().size();
        double centreyD = sommeyD / autre.getPoints().size();

        // calcul de la distance

        double diffX = centrexG - centrexD;
        double diffY = centreyG - centreyD;

        // Formule
        return Math.sqrt((diffX * diffX) + (diffY * diffY));
    }

    public List<Integer> getPoints() {
        return points;
    }
    public Cluster getC1() { return c1; }
    public Cluster getC2() { return c2; }
    public double getDistance() { return distance; }
    public int getNumGroupe() { return numGroupe; }
}
