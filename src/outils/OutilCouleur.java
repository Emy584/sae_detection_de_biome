package outils;

import java.awt.Color;
import java.awt.image.BufferedImage;


public class OutilCouleur {

    public static int[] getTabColor(int c) {  // c : entier rgb
        int blue = c & 0xff;
        int green = (c & 0xff00) >> 8;
        int red = (c & 0xff0000) >> 16;

        int[] rgb = {red, green, blue};
        return rgb;
    }

    public static int getColor(int r, int g, int b) {
        Color color = new Color(r, g, b);
        return color.getRGB();
    }

    public static int distanceColor(int c1, int c2) {
        int rc1 = (c1 >> 16) & 0xff;
        int rc2 = (c2 >> 16) & 0xff;
        int g1 = (c1 >> 8) & 0xff;
        int g2 = (c2 >> 8) & 0xff;
        int b1 = (c1) & 0xff;
        int b2 = (c2) & 0xff;
        return ((rc1 - rc2) * (rc1 - rc2)) + ((g1 - g2) * (g1 - g2) + ((b1 - b2) * (b1 - b2)));
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

    /**
     * méthode qui éclaircit un pixel selon un pourcentage donné
     * nouveau = valeur + (pourcentage/100) * (255 - valeur)
     */
    public static int eclaircirPixel(int rgb, double pourcentage) {
        // extraction des canaux
        // alpha
        int a = (rgb >> 24) & 0xFF;
        int[] tab = getTabColor(rgb);
        int r = tab[0];
        int g = tab[1];
        int b = tab[2];

        // calcul des nouvelles valeurs
        int nouveauR = (int) Math.round(r + (pourcentage / 100.0) * (255 - r));
        int nouveauG = (int) Math.round(g + (pourcentage / 100.0) * (255 - g));
        int nouveauB = (int) Math.round(b + (pourcentage / 100.0) * (255 - b));

        // vérif de ne pas dépasser 255
        nouveauR = Math.min(255, nouveauR);
        nouveauG = Math.min(255, nouveauG);
        nouveauB = Math.min(255, nouveauB);

        // recombinaison des canaux
        return (a << 24) | (nouveauR << 16) | (nouveauG << 8) | nouveauB;
    }
}