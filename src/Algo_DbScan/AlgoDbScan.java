package Algo_DbScan;

import outils.OutilCouleur;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Implémentation de l'algorithme DBSCAN pour regrouper des pixels
 * selon leur proximité de couleur.
 */
public class AlgoDbScan {
    /** Liste des pixels déjà traités. */
    public HashSet<Pixel> xTraite ;

    /** Ensemble des pixels à analyser. */
    public ArrayList<Pixel> x ;

    /** Liste des clusters trouvés. */
    public ArrayList<ArrayList<Pixel>> clusters ;

    /**
     * Initialise les structures de données de l'algorithme.
     */
    public AlgoDbScan() {
        this.xTraite = new HashSet<>();
        this.clusters = new ArrayList<>();
    }

    /**
     * Exécute l'algorithme DBSCAN.
     *
     * @param x ensemble des pixels à analyser
     * @param eps distance maximale pour être considéré comme voisin
     * @param minPts nombre minimum de voisins pour former un cluster
     * @return liste des pixels considérés comme du bruit
     */
    public ArrayList<Pixel> DBSCAN(ArrayList<Pixel> x, double eps, int minPts) {
        ArrayList<Pixel> bruits = new ArrayList<>() ;
        this.x = x;
        int compte = 0 ;
        for (Pixel xn : x) {
            compte += 1 ;
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
        return bruits ;
    }

    /**
     * Étend un cluster à partir d'un pixel noyau.
     *
     * @param xn pixel de départ
     * @param vn liste des voisins du pixel
     * @param cluster cluster en cours de construction
     * @param eps distance maximale entre voisins
     * @param minPts nombre minimum de voisins pour former un cluster
     */
    public void expandCluster(Pixel xn, ArrayList<Pixel> vn, ArrayList<Pixel> cluster, double eps, int minPts) {
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
     * Vérifie si un pixel appartient déjà à un cluster.
     *
     * @param p pixel à vérifier
     * @return true si le pixel est déjà dans un cluster, false sinon
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
     * @param xn pixel de référence
     * @param eps distance maximale entre deux pixels
     * @return liste des voisins trouvés
     */
    public ArrayList<Pixel> regionQuery(Pixel xn, double eps) {
        ArrayList<Pixel> v = new ArrayList<>();
        //  System.out.println("Recherche de voisin");
        for (Pixel xi : this.x) {
            if (xn.x != xi.x && xn.y != xi.y && Pixel.distance(xn, xi) <= eps) {
                v.add(xi);
            }
        }
        return v;
    }
}

