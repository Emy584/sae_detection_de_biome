package Algo_DbScan;

import java.awt.*;
import java.util.ArrayList;

public class AlgoDbScan {
    public ArrayList<Point> xTraite ;
    public ArrayList<Point> x ;
    public ArrayList<ArrayList<Point>> clusters ;

    public void DBSCAN(ArrayList<Point> x, double eps, int minPts) {
        this.x = x;
        for (Point xn : x) {
            if (this.xTraite.contains(xn)) {
                continue;
            }
            this.xTraite.add(xn);
            ArrayList<Point> vn = this.regionQuery(xn, eps);
            if (vn.size() >= minPts) {
                ArrayList<Point> cluster = new ArrayList<>();
                clusters.add(cluster);
                expandCluster(xn, vn, cluster, eps, minPts);
            }
        }
    }

    public void expandCluster(Point xn, ArrayList<Point> vn, ArrayList<Point> cluster, double eps, int minPts) {
        cluster.add(xn);
        int i = 0;
        while (i < vn.size()) {
            Point xi = vn.get(i);
            if (!this.xTraite.contains(xi)) {
                this.xTraite.add(xi);
                ArrayList<Point> vi = regionQuery(xi, eps);
                if (vi.size() >= minPts) {
                    for (Point p : vi) {
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

    private boolean estDansUnCluster(Point p) {
        for (ArrayList<Point> cluster : this.clusters) {
            if (cluster.contains(p)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Point> regionQuery(Point xn, double eps) {
        ArrayList<Point> v = new ArrayList<>();
        for (Point xi : this.x) {
            if (!xn.equals(xi) && d(xn, xi) <= eps) {
                v.add(xi);
            }
        }
        return v;
    }
}

