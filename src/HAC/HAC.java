package HAC;

import java.util.ArrayList;
import java.util.List;

// classe pour faire l'algorithme de HAC
public class HAC {

    private double[][] distances;
    private double[][] coordonnees;
    private int choix;

    // choix c est le choix des distances qu'on veut
    public HAC(double[][] d, int c) {
        this.distances = d;
        this.choix = c;
    }

    public Cluster calculer() {
        int n = this.distances.length;
        int idCompteur = n;

        List<Cluster> L = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            L.add(new Cluster(i, i));
        }

        while (L.size() > 1) {
            Cluster c1meilleur = null;
            Cluster c2meilleur = null;
            double distanceMin = Double.MAX_VALUE;

            for (int i = 0; i < L.size(); i++) {
                for (int j = i + 1; j < L.size(); j++) {
                    // clusters (deux premieres branches enfants)
                    Cluster c1 = L.get(i);
                    Cluster c2 = L.get(j);

                    // Calcul de la distance selon le choix
                    double dist = 0;
                    if (this.choix == 1) {
                        dist = c1.singleLinkage(c2, this.distances);
                    } else if (this.choix == 2) {
                        dist = c1.completeLinkage(c2, this.distances);
                    } else if (this.choix == 3) {
                        dist = c1.averageLinkage(c2, this.distances);
                    } else if (this.choix == 4) {
                        dist = c1.centroidLinkage(c2, this.coordonnees);
                    }

                    // calcul de la meilleur distance par rapport a l'algo
                    if (dist < distanceMin) {
                        distanceMin = dist;
                        c1meilleur = c1;
                        c2meilleur = c2;
                    }
                }
            }

            Cluster nouveauCluster = new Cluster(idCompteur++, c1meilleur, c2meilleur, distanceMin);
            L.remove(c1meilleur);
            L.remove(c2meilleur);
            L.add(nouveauCluster);
        }

        return L.get(0);
    }
}
