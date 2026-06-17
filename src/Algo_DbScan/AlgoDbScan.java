package Algo_DbScan;

import outils.OutilCouleur;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class AlgoDbScan {
    public ArrayList<Pixel> xTraite ;
    public ArrayList<Pixel> x ;
    public ArrayList<ArrayList<Pixel>> clusters ;

    public AlgoDbScan() {
        this.xTraite = new ArrayList<>();
        this.clusters = new ArrayList<>();
    }


    public ArrayList<Pixel> DBSCAN(ArrayList<Pixel> x, double eps, int minPts) {
        ArrayList<Pixel> bruits = new ArrayList<>() ;
        this.x = x;
        for (Pixel xn : x) {
            System.out.println("Nouveau Point");
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
    public ArrayList<Color> getPalette() {
        ArrayList<Color> pal = new ArrayList<>();

        for (ArrayList<Pixel> cluster : this.clusters) {
            long r = 0, g = 0, b = 0;

            for (Pixel rgb : cluster) {
                Color c = new Color(rgb.c);
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

    public ArrayList<Pixel> regionQuery(Pixel xn, double eps) {
        System.out.println("Recherche de voisin");
        ArrayList<Pixel> v = new ArrayList<>();
        for (Pixel xi : this.x) {
            if (xn.c != xi.c && OutilCouleur.distanceColor(xn.c, xi.c) <= eps) {
                v.add(xi);
            }
        }
        return v;
    }
}

