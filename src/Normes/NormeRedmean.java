package Normes;
import java.awt.Color;

public class NormeRedmean implements NormeCouleurs {
  
  /**
   * Méthode distance entre deux couleurs avec la norme Redmean
   * @param c1 première couleur
   * @param c2 deuxième couleur
   * @return la distance
   */
  @Override
    public double distanceCouleurs(Color c1, Color c2) {
        // R, G, B
        int r1 = c1.getRed();
        int g1 = c1.getGreen();
        int b1 = c1.getBlue();

        int r2 = c2.getRed();
        int g2 = c2.getGreen();
        int b2 = c2.getBlue();

        // calcul des deltas (écarts)
        int deltaR = r1 - r2;
        int deltaG = g1 - g2;
        int deltaB = b1 - b2;

        // calcul (la moyenne des rouges)
        double rBarre = (r1 + r2) / 2.0;

        // formule redmean
        double termeRouge = (2.0 + (rBarre / 256.0)) * (deltaR * deltaR);
        double termeVert  = 4.0 * (deltaG * deltaG);
        double termeBleu  = (2.0 + ((255.0 - rBarre) / 256.0)) * (deltaB * deltaB);

        // La formule donne le carré de la distance
        // Math.sqrt() pour distance réelle ΔC
        return Math.sqrt(termeRouge + termeVert + termeBleu);
    }
}
