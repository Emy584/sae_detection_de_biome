package Normes;
import java.awt.Color;

public class NormeCIE94 implements NormeCouleurs {

   /**
   * Méthode distance entre deux couleurs avec la norme couleurs
   * @param c1 première couleur
   * @param c2 deuxième couleur
   * @return la distance
   */
    @Override
    public double distanceCouleurs(Color c1, Color c2) {
        // conversion des deux couleurs en coordonnées L*a*b* (via la classe MainCIE94 donné sur Arche)
        int[] lab1 = MainCIE94.rgb2lab(c1.getRed(), c1.getGreen(), c1.getBlue());
        double l1 = lab1[0];
        double a1 = lab1[1];
        double b1 = lab1[2];

        int[] lab2 = MainCIE94.rgb2lab(c2.getRed(), c2.getGreen(), c2.getBlue());
        double l2 = lab2[0];
        double a2 = lab2[1];
        double b2 = lab2[2];

        // calcul des écarts de base
        double deltaL = l1 - l2; // ΔL = L*1 - L*2
        double deltaA = a1 - a2;
        double deltaB = b1 - b2;


        // calcul de la Chroma (C1 et C2)
        double c1Chroma = Math.sqrt((a1 * a1) + (b1 * b1));
        double c2Chroma = Math.sqrt((a2 * a2) + (b2 * b2));
        
        // ΔC = C1 - C2
        double deltaC = c1Chroma - c2Chroma; 

        // calcul de la Teinte (ΔH)
        // formule : ΔH² = (Δa)² + (Δb)² - ΔC²
        double deltaHCarre = (deltaA * deltaA) + (deltaB * deltaB) - (deltaC * deltaC);
        
        // deltaHCarre peut être négatif 
        if (deltaHCarre < 0) {
            deltaHCarre = 0;
        }

        // calcul des facteurs de pondération SC et SH basés sur la première couleur (référence)
        double sC = 1.0 + (0.045 * c1Chroma);
        double sH = 1.0 + (0.015 * c1Chroma);

        // formule CIE94
        double termeL = deltaL; // Divisé par SL qui vaut 1 par défaut 
        double termeC = deltaC / sC;
        double termeHCarre = deltaHCarre / (sH * sH);

        // ΔE94 = √( ΔL² + (ΔC/SC)² + ΔH²/SH² )
        return Math.sqrt((termeL * termeL) + (termeC * termeC) + termeHCarre);
    }
}