package HAC;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // palette de couleurs
        Map<String, Color> biomes = new HashMap<>();
        biomes.put("Tundra",         new Color(71,  70,  61));
        biomes.put("Taiga",          new Color(43,  50,  35));
        biomes.put("ForetTemperee",  new Color(59,  66,  43));
        biomes.put("ForetTropicale", new Color(46,  64,  34));
        biomes.put("Savane",         new Color(84, 106,  70));
        biomes.put("Prairie",        new Color(104, 95,  82));
        biomes.put("Desert",         new Color(152,140, 120));
        biomes.put("Glacier",        new Color(200,200, 200));
        biomes.put("EauPeuProfonde", new Color(49,  83, 100));
        biomes.put("EauProfonde",    new Color(12,  31,  47));

        // choix de l'algo
        System.out.println("Choix de l'algorithme");
        System.out.println("1: Single Linkage");
        System.out.println("2: Complete Linkage");
        System.out.println("3: Average Linkage");
        System.out.println("4: Centroid Linkage");
        System.out.print("choix: ");
        int choix = new Scanner(System.in).nextInt();

        //exportation de l'image de base (flouté)
        BufferedImage img = ImageIO.read(new File("img/Planete1FlouParMoy3.jpg"));
        int largeur = img.getWidth();
        int hauteur = img.getHeight();

        // HAC sur 10 biomes a partir de la palette
        String[] noms = biomes.keySet().toArray(new String[0]);
        Color[] paletteBiomes = new Color[10];
        double[][] couleursBiomes = new double[10][3];
        for (int i = 0; i < 10; i++) {
            paletteBiomes[i] = biomes.get(noms[i]);
            couleursBiomes[i][0] = paletteBiomes[i].getRed();
            couleursBiomes[i][1] = paletteBiomes[i].getGreen();
            couleursBiomes[i][2] = paletteBiomes[i].getBlue();
        }

        double[][] distances = new double[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                double dr = couleursBiomes[i][0] - couleursBiomes[j][0];
                double dg = couleursBiomes[i][1] - couleursBiomes[j][1];
                double db = couleursBiomes[i][2] - couleursBiomes[j][2];
                distances[i][j] = Math.sqrt(dr*dr + dg*dg + db*db);
            }
        }

        double[][] matriceHAC = (choix == 4) ? couleursBiomes : distances;
        HAC hac = new HAC(matriceHAC, choix);
        java.util.List<Cluster> clusters = hac.calculer();


        // pour chaque pixel, trouver le biome le plus proche
        BufferedImage imgResultat = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                Color px = new Color(img.getRGB(x, y));
                String biomeChoisi = null;
                double distMin = Double.MAX_VALUE;
                for (Map.Entry<String, Color> entry : biomes.entrySet()) {
                    Color cb = entry.getValue();
                    double dr = px.getRed()   - cb.getRed();
                    double dg = px.getGreen() - cb.getGreen();
                    double db = px.getBlue()  - cb.getBlue();
                    double d  = Math.sqrt(dr*dr + dg*dg + db*db);
                    if (d < distMin) { distMin = d; biomeChoisi = entry.getKey(); }
                }
                imgResultat.setRGB(x, y, biomes.get(biomeChoisi).getRGB());
            }
        }

        ImageIO.write(imgResultat, "png", new File("img/Planete1HAC_resultat.png"));
        System.out.println("Image sauvegardée.");
    }
}