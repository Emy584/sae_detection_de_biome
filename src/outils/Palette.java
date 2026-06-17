package outils;

import Normes.NormeRedmean;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import Normes.NormeCouleurs;

public class Palette {
    
    // toutes les couleurs disponibles avec le nom du biome correspondant
    private HashMap<String, Color> listeCouleurs;
    ArrayList<Color> couleurs ;
    public Palette(ArrayList<Color> couleurs) {
        this.couleurs = couleurs ;
    }

    // constructeur qui initialise les biomes avec leurs couleurs
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

    /**
     * méthode pour trouver le biome dont la couleur est la plus proche de la couleur donnée
     * en utilisant une norme spécifique
     */
    public String trouverBiomeLePlusProche(Color couleurCentroide, NormeCouleurs norme) {
        // initialisation
        String meilleurBiome = "Inconnu";
        double distanceMin = Double.MAX_VALUE;

        // on parcourt la palette des biomes
        for (HashMap.Entry<String, Color> entry : listeCouleurs.entrySet()) {
            String biome = entry.getKey();
            Color c = entry.getValue();

            // on calcule la distance entre la couleur du centroide et la couleur
            double distance = norme.distanceCouleurs(couleurCentroide, c);

            if (distance < distanceMin) {
                distanceMin = distance;
                meilleurBiome = biome;
            }
        }
        // on retourne le nom du biome le plus proche
        return meilleurBiome;
    }

    // getter pour récupérer la couleur exacte d'un biome
    public Color getCouleurBiome(String nomBiome) {
        return listeCouleurs.get(nomBiome);
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
            if(norm.distanceCouleurs(c,couleurs.get(i))<norm.distanceCouleurs(c,min)){
                min = couleurs.get(i);
            }
        }
        return min;
    }

}

