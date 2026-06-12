package Normes;
import java.awt.Color;

public class NormeRGB implements NormeCouleurs {

/**
   * Méthode distance entre deux couleurs avec la norme RGB
   * @param c1 première couleur
   * @param c2 deuxième couleur
   * @return la distance
   */
    @Override
    public double distanceCouleurs(Color c1, Color c2) {
        int r1 = c1.getRed();
        int g1 = c1.getGreen();
        int b1 = c1.getBlue();

        int r2 = c2.getRed();
        int g2 = c2.getGreen();
        int b2 = c2.getBlue();

        int deltaR = r1 - r2;
        int deltaG = g1 - g2;
        int deltaB = b1 - b2;

        // d(c1, c2) = (R1-R2)² + (G1-G2)² + (B1-B2)²
        // retourne la racine carrée garder un double
        return Math.sqrt((deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB));
    }
}