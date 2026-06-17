package HAC;

import java.util.ArrayList;
import java.util.List;

public class HAC {
    private double[][] distances;
    private int choix;

    public HAC(double[][] d, int c) {
        this.distances = d;
        this.choix = c;
    }

    public List<Cluster> calculer() {
        int n = this.distances.length;
        int idCompteur = n;

        /* 1. Initialisation */
        List<Cluster> L = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            L.add(new Cluster(i, i));
        }

        //Boucle principale
        while (L.size() > 10) {

            //Trouver la paire (C1, C2) avec la distance minimale
            Cluster c1meilleur = null;
            Cluster c2meilleur = null;
            double distanceMin = Double.MAX_VALUE;

            for (int i = 0; i < L.size(); i++) {
                for (int j = i + 1; j < L.size(); j++) {
                    Cluster c1 = L.get(i);
                    Cluster c2 = L.get(j);

                    double dist = 0;
                    if (this.choix == 1) dist = c1.singleLinkage(c2, this.distances);
                    else if (this.choix == 2) dist = c1.completeLinkage(c2, this.distances);
                    else if (this.choix == 3) dist = c1.averageLinkage(c2, this.distances);

                    if (dist < distanceMin) {
                        distanceMin = dist;
                        c1meilleur = c1;
                        c2meilleur = c2;
                    }
                }
            }

            //Fusionner C1 et C2 dans un nouveau cluster C
            Cluster nouveauCluster = new Cluster(idCompteur++, c1meilleur, c2meilleur, distanceMin);

            //Enlever C1 et C2 de L */
            L.remove(c1meilleur);
            L.remove(c2meilleur);



            //Ajouter C à L */
            L.add(nouveauCluster);
        }

        // Retourner les clusters à couper (vos 10 biomes)
        return L;
    }
}