package HAC;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class MainDetectionBiomes {
    public static void main(String[] args) throws Exception {
        // pamette de couleurs
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

        BufferedImage img = ImageIO.read(new File("img/Planete1FlouParMoy3.jpg"));
        int largeur = img.getWidth();
        int hauteur  = img.getHeight();

        // eclaircir le font
        BufferedImage fond = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                Color px = new Color(img.getRGB(x, y));
                int r = Math.round(px.getRed()   + 75f/100f * (255 - px.getRed()));
                int g = Math.round(px.getGreen() + 75f/100f * (255 - px.getGreen()));
                int b = Math.round(px.getBlue()  + 75f/100f * (255 - px.getBlue()));
                fond.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }

        // pour chaque pixel, trouver le biome le plus proche
        String[][] biomePix = new String[hauteur][largeur];
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
                biomePix[y][x] = biomeChoisi;
            }
        }

        // une image par biome
        new File("img/biomes_HAC");
        for (String nomBiome : biomes.keySet()) {
            // Copier le fond
            BufferedImage imgBiome = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < hauteur; y++)
                for (int x = 0; x < largeur; x++)
                    imgBiome.setRGB(x, y, fond.getRGB(x, y));

            for (int y = 0; y < hauteur; y++)
                for (int x = 0; x < largeur; x++)
                    if (biomePix[y][x].equals(nomBiome))
                        imgBiome.setRGB(x, y, img.getRGB(x, y));

            // creation des images pour chaque biome
            Graphics2D g2 = imgBiome.createGraphics();
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString(nomBiome, 20, 50);
            g2.dispose();

            ImageIO.write(imgBiome, "png", new File("img/biomes_HAC/" + nomBiome + ".png"));
            System.out.println("Sauvegardé : " + nomBiome);
        }
    }
}