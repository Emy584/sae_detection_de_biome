package Algo_DbScan;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;


public class AlgoDbScan {
    public HashSet<Pixel> xTraite ;

    public ArrayList<Pixel> x ;

    public ArrayList<ArrayList<Pixel>> clusters ;

    public AlgoDbScan() {
        this.xTraite = new HashSet<>();
        this.clusters = new ArrayList<>();
    }


    public ArrayList<Pixel> DBSCAN(ArrayList<Pixel> x, double eps, int minPts) {
        System.out.println("Début de la boucle DBSCAN...");
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
        System.out.println("Fin DBSCAN");
        return bruits ;
    }


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


    private boolean estDansUnCluster(Pixel p) {
        for (ArrayList<Pixel> cluster : this.clusters) {
            if (cluster.contains(p)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Pixel> regionQuery(Pixel xn, double eps) {
        ArrayList<Pixel> v = new ArrayList<>();
        double eps2 = eps * eps;

        for (Pixel xi : this.x) {
            if (xn == xi) continue;

            int dx = xi.x - xn.x;
            int dy = xi.y - xn.y;

            if (dx * dx + dy * dy <= eps2) {
                v.add(xi);
            }
        }

        return v;
    }
}

