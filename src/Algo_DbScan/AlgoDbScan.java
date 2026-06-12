package Algo_DbScan;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class AlgoDbScan {
    public ArrayList<Integer> xTraite ;
    public HashSet<Integer> x ;
    public ArrayList<ArrayList<Integer>> clusters ;

    public AlgoDbScan() {
        this.xTraite = new ArrayList<>();
        this.clusters = new ArrayList<>();
    }


    public void DBSCAN(HashSet<Integer> x, double eps, int minPts) {
        this.x = x;
        for (int xn : x) {
            System.out.println("Nouveau Point");
            if (this.xTraite.contains(xn)) {
                continue;
            }
            this.xTraite.add(xn);
            ArrayList<Integer> vn = this.regionQuery(xn, eps);
            if (vn.size() >= minPts) {
                ArrayList<Integer> cluster = new ArrayList<>();
                clusters.add(cluster);
                expandCluster(xn, vn, cluster, eps, minPts);
            }
        }
    }

    public void expandCluster(int xn, ArrayList<Integer> vn, ArrayList<Integer> cluster, double eps, int minPts) {
        cluster.add(xn);
        int i = 0;
        while (i < vn.size()) {
            int xi = vn.get(i);
            if (!this.xTraite.contains(xi)) {
                this.xTraite.add(xi);
                ArrayList<Integer> vi = regionQuery(xi, eps);
                if (vi.size() >= minPts) {
                    for (int p : vi) {
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

    private boolean estDansUnCluster(int p) {
        for (ArrayList<Integer> cluster : this.clusters) {
            if (cluster.contains(p)) {
                return true;
            }
        }
        return false;
    }
    public ArrayList<Color> getPalette() {
        ArrayList<Color> pal = new ArrayList<>();

        for (ArrayList<Integer> cluster : this.clusters) {
            long r = 0, g = 0, b = 0;

            for (int rgb : cluster) {
                Color c = new Color(rgb);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }

            int size = cluster.size();

            pal.add(new Color(
                    (int) (r / size),
                    (int) (g / size),
                    (int) (b / size)
            ));
        }

        return pal;
    }

    public ArrayList<Integer> regionQuery(int xn, double eps) {
        System.out.println("Recherche de voisin");
        ArrayList<Integer> v = new ArrayList<>();
        for (int xi : this.x) {
            if (xn != xi && OutilCouleur.distanceColor(xn, xi) <= eps) {
                v.add(xi);
            }
        }
        return v;
    }
}

