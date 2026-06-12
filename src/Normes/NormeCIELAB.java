package Normes;
import java.awt.Color;

public class NormeCIELAB implements NormeCouleurs {

   /**
   * Méthode distance entre deux couleurs avec la norme CIELAB
   * @param c1 première couleur
   * @param c2 deuxième couleur
   * @return la distance
   */
    @Override
    public double distanceCouleurs(Color c1, Color c2) {
        // conversion première couleur en coordonnées L*a*b*
        int[] lab1 = MainCIE94.rgb2lab(c1.getRed(), c1.getGreen(), c1.getBlue());
        double l1 = lab1[0];
        double a1 = lab1[1];
        double b1 = lab1[2];

        // deuxième couleur 
        int[] lab2 = MainCIE94.rgb2lab(c2.getRed(), c2.getGreen(), c2.getBlue());
        double l2 = lab2[0];
        double a2 = lab2[1];
        double b2 = lab2[2];

        // calcul des deltas (écarts) sur chaque axe
        double deltaL = l2 - l1;
        double deltaA = a2 - a1;
        double deltaB = b2 - b1;

        // formule Delta E (Distance Euclidienne dans l'espace L*a*b*)
        // d(c1, c2) = racine carrée( (L2 - L1)² + (a2 - a1)² + (b2 - b1)² )
        double sommeCarres = (deltaL * deltaL) + (deltaA * deltaA) + (deltaB * deltaB);

        return Math.sqrt(sommeCarres);
    }
}