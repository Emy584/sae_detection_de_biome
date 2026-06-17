package Algo_DbScan;

import Algo_Kmeans.AlgoKMeans;
import Normes.NormeCIELAB;
import Normes.NormeRGB;
import java.util.List;
import outils.OutilCouleur;
import outils.Palette;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {

        try {
            BufferedImage image = ImageIO.read(new File("../../img/Planete1(200&200).jpg"));

            ArrayList<Pixel> pts = new ArrayList<>();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pts.add(new Pixel(x, y, image.getRGB(x, y)));
                }
            }
            Pixel[] tpts = pts.toArray(new Pixel[0]);

            Scanner scanner = new Scanner(System.in);

            // demande à l'utilisateur le nb de groupes à traiter
            System.out.println("Nombre de groupes à traiter :");
            int nbGroupes = Integer.parseInt(scanner.nextLine().trim());

            // clustering calculé une seule fois pour toute l'exécution
            AlgoKMeans ak = new AlgoKMeans(nbGroupes, new NormeCIELAB());
            ArrayList<ArrayList<Pixel>> biomes = ak.algorithmeClustering(tpts);

            // fusion des groupes qui partagent le même biome
            Map<String, ArrayList<Pixel>> pixelsParBiome = ak.fusionnerParBiome(biomes, new Palette());
            List<String> nomsBiomesUniques = new ArrayList<>(pixelsParBiome.keySet());

            // demande à l'utilisateur quel biome traiter
            System.out.println("Biomes disponibles :");
            for (int i = 0; i < nomsBiomesUniques.size(); i++) {
                String nom = nomsBiomesUniques.get(i);
                System.out.println("  " + i + " : " + nom + " (" + pixelsParBiome.get(nom).size() + " pixels)");
            }

            System.out.print("\nNuméro du biome à analyser avec DBSCAN : ");
            int numeroDuBiome = Integer.parseInt(scanner.nextLine().trim());

            AlgoDbScan ads = new AlgoDbScan();
            double eps = 3;
            int minPts = 3;
            ArrayList<Pixel> bruits = ads.DBSCAN(biomes.get(numeroDuBiome), eps, minPts);

            BufferedImage image2 = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_3BYTE_BGR
            );
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int numRgbBase = image.getRGB(x, y);
                    image2.setRGB(x, y, OutilCouleur.eclaircirPixel(numRgbBase, 75.0));
                }
            }

            Random random = new Random();

            // Pour chaque cluster
            System.out.println("AffichageCluster");
            for (ArrayList<Pixel> cluster : ads.clusters) {

                Color couleurCluster = new Color(
                        random.nextInt(256),
                        random.nextInt(256),
                        random.nextInt(256)
                );

                for (Pixel p : cluster) {
                    image2.setRGB(p.x, p.y, couleurCluster.getRGB());
                }
            }

            System.out.println("AffichageBruit");
            // Pour les bruits
            for (Pixel p : bruits) {
                image2.setRGB(p.x, p.y, Color.RED.getRGB());
            }

            System.out.println("AffichageReste");
            // Pour tous les autres pixels
            ImageIO.write(image2, "png", new File("../../img/result/dbscan_result.png"));
            System.out.println("Traitement terminé.");


        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }
}