package Algo_DbScan;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Implémentation de l'algorithme DBSCAN (Density-Based Spatial Clustering of Applications with Noise).
 *
 * Cet algorithme regroupe les pixels en clusters selon leur proximité spatiale.
 * Deux pixels sont considérés comme voisins si leur distance euclidienne est
 * inférieure ou égale à la valeur epsilon (eps).
 *
 * Les pixels qui ne possèdent pas suffisamment de voisins sont considérés comme du bruit.
 */
public class AlgoDbScan {

    /** Ensemble des pixels déjà traités par l'algorithme. */
    public HashSet<Pixel> xTraite;

    /** Ensemble des pixels à analyser. */
    public ArrayList<Pixel> x;

    /** Liste des clusters détectés. */
    public ArrayList<ArrayList<Pixel>> clusters;

    /**
     * Constructeur de l'algorithme DBSCAN.
     *
     * Initialise les structures nécessaires au traitement :
     * la liste des clusters et l'ensemble des pixels déjà visités.
     */
    public AlgoDbScan() {
        this.xTraite = new HashSet<>();
        this.clusters = new ArrayList<>();
    }

    /**
     * Exécute l'algorithme DBSCAN sur un ensemble de pixels.
     *
     * Pour chaque pixel non encore traité :
     *     Recherche ses voisins dans un rayon eps.
     *     Si le nombre de voisins est supérieur ou égal à minPts,
     *     un nouveau cluster est créé.
     *     Sinon, le pixel est considéré comme du bruit.
     * @param x ensemble des pixels à analyser
     * @param eps distance maximale entre deux pixels pour être considérés comme voisins
     * @param minPts nombre minimum de voisins requis pour former un cluster
     * @return liste des pixels considérés comme du bruit
     */
    public ArrayList<Pixel> DBSCAN(ArrayList<Pixel> x, double eps, int minPts) {
        System.out.println("Début de la boucle DBSCAN...");
        ArrayList<Pixel> bruits = new ArrayList<>();
        this.x = x;

        int compte = 0;

        for (Pixel xn : x) {
            compte++;

            if (this.xTraite.contains(xn)) {
                continue;
            }

            this.xTraite.add(xn);

            ArrayList<Pixel> vn = this.regionQuery(xn, eps);

            if (vn.size() >= minPts) {
                ArrayList<Pixel> cluster = new ArrayList<>();
                clusters.add(cluster);
                expandCluster(xn, vn, cluster, eps, minPts);
            } else {
                bruits.add(xn);
            }
        }

        System.out.println("Fin DBSCAN");
        return bruits;
    }

    /**
     * Étend récursivement un cluster à partir d'un pixel noyau.
     *
     * Tous les voisins du pixel de départ sont explorés.
     * Lorsqu'un voisin possède lui-même suffisamment de voisins,
     * ses voisins sont ajoutés à la liste d'exploration.
     *
     * @param xn pixel noyau du cluster
     * @param vn liste des voisins du pixel noyau
     * @param cluster cluster en cours de construction
     * @param eps distance maximale entre deux pixels voisins
     * @param minPts nombre minimum de voisins pour qu'un pixel soit considéré comme noyau
     */
    public void expandCluster(Pixel xn, ArrayList<Pixel> vn,
                              ArrayList<Pixel> cluster,
                              double eps, int minPts) {

        cluster.add(xn);

        int i = 0;

        while (i < vn.size()) {

            Pixel xi = vn.get(i);

            if (!this.xTraite.contains(xi)) {

                this.xTraite.add(xi);

                ArrayList<Pixel> vi = regionQuery(xi, eps);

                if (vi.size() >= minPts) {

                    for (Pixel p : vi) {
                        if (!vn.contains(p)) {
                            vn.add(p);
                        }
                    }
                }
            }

            if (!estDansUnCluster(xi)) {
                cluster.add(xi);
            }

            i++;
        }
    }

    /**
     * Vérifie si un pixel appartient déjà à un cluster existant.
     *
     * @param p pixel à vérifier
     * @return true si le pixel appartient déjà à un cluster, false sinon
     */
    private boolean estDansUnCluster(Pixel p) {

        for (ArrayList<Pixel> cluster : this.clusters) {
            if (cluster.contains(p)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Recherche tous les voisins d'un pixel dans un rayon donné.
     *
     * La distance utilisée est la distance euclidienne dans le plan.
     * Pour optimiser les calculs, la comparaison est effectuée sur les
     * distances au carré afin d'éviter l'utilisation de la racine carrée.
     *
     * @param xn pixel de référence
     * @param eps rayon maximal de recherche
     * @return liste des pixels voisins de xn
     */
    public ArrayList<Pixel> regionQuery(Pixel xn, double eps) {

        ArrayList<Pixel> v = new ArrayList<>();
        double eps2 = eps * eps;

        for (Pixel xi : this.x) {

            if (xn == xi) {
                continue;
            }

            int dx = xi.x - xn.x;
            int dy = xi.y - xn.y;

            if (dx * dx + dy * dy <= eps2) {
                v.add(xi);
            }
        }

        return v;
    }
}