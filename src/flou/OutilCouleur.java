package flou;

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
}
