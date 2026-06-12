package Algo_DbScan;

import java.awt.*;
import java.util.ArrayList;

public class Palette {
    ArrayList<Color> couleurs ;
    public Palette(ArrayList<Color> couleurs) {
        this.couleurs = couleurs ;
    }

    public Color getPlusProche(Color c) {
        Color min = couleurs.get(0);
        for (int i = 1; i < couleurs.size(); i++) {
            if(OutilCouleur.distanceColor(c.getRGB(),couleurs.get(i).getRGB())<OutilCouleur.distanceColor(c.getRGB(),min.getRGB())){
                min = couleurs.get(i);
            }
        }
        return min;
    }

    public Color getPlusProcheRedmean(Color c) {
        Color min = couleurs.getFirst();
        NormeRedmean norm = new NormeRedmean();
        for (int i = 1; i < couleurs.size(); i++) {
            if(norm.distanceCouleur(c,couleurs.get(i))<norm.distanceCouleur(c,min)){
                min = couleurs.get(i);
            }
        }
        return min;
    }
}
