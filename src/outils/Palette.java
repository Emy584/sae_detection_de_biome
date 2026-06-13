package outils;

import java.awt.Color;
import java.util.HashMap;
import Normes.NormeCouleurs;

public class Palette {
    
    // toutes les couleurs disponibles avec le nom du biome correspondant
    private HashMap<String, Color> listeCouleurs;

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
}

