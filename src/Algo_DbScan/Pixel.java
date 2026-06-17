package Algo_DbScan;

public class Pixel {
    public int x;
    public int y;
    public int c;

    public Pixel (int x, int y, int couleur) {
        this.x = x;
        this.y = y;
        this.c =couleur;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pixel)) return false;
        Pixel object = (Pixel) obj;
        return this.x == object.x && this.y == object.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
