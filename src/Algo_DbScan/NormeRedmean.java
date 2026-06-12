package Algo_DbScan;

import java.awt.*;

public class NormeRedmean {

    public int distanceCouleur(Color c1, Color c2) {
        int r1 = c1.getRed();
        int g1 = c1.getGreen();
        int b1 = c1.getBlue();

        int r2 = c2.getRed();
        int g2 = c2.getGreen();
        int b2 = c2.getBlue();

        double rMean = (r1 + r2) / 2.0;

        int deltaR = r1 - r2;
        int deltaG = g1 - g2;
        int deltaB = b1 - b2;

        double termeR = (2.0 + rMean / 256.0) * Math.pow(deltaR, 2);
        double termeG = 4.0 * Math.pow(deltaG, 2);
        double termeB = (2.0 + (255.0 - rMean) / 256.0) * Math.pow(deltaB, 2);

        double distance = Math.sqrt(termeR + termeG + termeB);

        return (int) Math.round(distance);
    }
}
