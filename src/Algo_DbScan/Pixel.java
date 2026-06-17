package Algo_DbScan;

public class Pixel {
    int x ;
    int y ;
    int c ;

    public Pixel (int x, int y, int couleur) {
        this.x = x;
        this.y = y;
        this.c =couleur;
    }

    public static double distance(Pixel p1, Pixel p2) {
        int dx = p2.x - p1.x;
        int dy = p2.y - p1.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
