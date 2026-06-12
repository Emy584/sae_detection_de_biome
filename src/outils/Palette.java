package outils;

import java.awt.Color;
import java.util.HashMap;

public class Palette {
    
    // toutes les couleurs disponibles
    private static HashMap<String, Color> listeCouleurs;



    public Palette() {
        this.listeCouleurs = new HashMap<>();
        listeCouleurs.put("Tundra", new Color(71, 70, 61));
        listeCouleurs.put("Taïga", new Color(43, 50, 35));
        listeCouleurs.put("Forêt tempérée", new Color(59, 66, 43));
        listeCouleurs.put("Forêt tropicale", new Color(46, 64, 34));
        listeCouleurs.put("Savane", new Color(84, 106, 70));
        listeCouleurs.put("Prairie", new Color(104, 95, 82));
        listeCouleurs.put("Désert", new Color(152, 140, 120));
        listeCouleurs.put("Glacier", new Color(200, 200, 200));
        listeCouleurs.put("Eau peu profonde", new Color(49, 83, 100));
        listeCouleurs.put("Eau profonde", new Color(12, 31, 47));
    }

}

