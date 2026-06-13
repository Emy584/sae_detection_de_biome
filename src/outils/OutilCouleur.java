package outils;

import java.awt.Color;


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
  
  public static int combineCouleur(int r, int g, int b) {
    Color color = new Color(r, g, b);
    return color.getRGB();
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
