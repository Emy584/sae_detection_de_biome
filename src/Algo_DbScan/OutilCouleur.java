package Algo_DbScan;

import java.awt.image.BufferedImage;

public class OutilCouleur {
    static int[] getTabColor(int c){
        int r = c & 0xff;
        int g = (c & 0xff00) >> 8;
        int b = (c & 0xff0000) >> 16;
        return new int[]{r, g,b};
    }

    static int distanceColor(int c1, int c2){
        int rc1 = (c1 >> 16) & 0xff;
        int rc2 = (c2 >> 16) & 0xff;
        int g1 = (c1 >> 8) & 0xff;
        int g2 = (c2 >> 8) & 0xff;
        int b1 = (c1) & 0xff;
        int b2 = (c2) & 0xff;
        return ((rc1-rc2)*(rc1-rc2))+((g1-g2)*(g1-g2)+((b1-b2)*(b1-b2)));
    }

    public static double erreurMoyenne(BufferedImage originale, BufferedImage resultat) {

        if (originale.getWidth() != resultat.getWidth()
                || originale.getHeight() != resultat.getHeight()) {
            throw new IllegalArgumentException("Les images doivent avoir la même taille");
        }

        double somme = 0;

        for (int x = 0; x < originale.getWidth(); x++) {
            for (int y = 0; y < originale.getHeight(); y++) {

                int rgb1 = originale.getRGB(x, y);
                int rgb2 = resultat.getRGB(x, y);

                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = rgb1 & 0xff;

                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = rgb2 & 0xff;

                double distance = Math.sqrt(
                        (r1 - r2) * (r1 - r2)
                                + (g1 - g2) * (g1 - g2)
                                + (b1 - b2) * (b1 - b2)
                );

                somme += distance;
            }
        }

        return somme / (originale.getWidth() * originale.getHeight());
    }

    public static double similarite(BufferedImage originale, BufferedImage resultat) {
        double erreur = erreurMoyenne(originale, resultat);
        return 100.0 * (1.0 - erreur / 441.67295593);
    }
}

