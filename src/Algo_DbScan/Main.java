package Algo_DbScan;

import Algo_Kmeans.AlgoKMeans;
import Normes.NormeCIELAB;
import Normes.NormeRGB;
import outils.OutilCouleur;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {


    static void main(String[] args) {

        try {
            BufferedImage image = ImageIO.read(new File("./img/Planete1(200&200).jpg"));

            ArrayList<Pixel> pts = new ArrayList<>() ;
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int rgb = image.getRGB(x, y);
                    pts.add(new Pixel(x, y, rgb));
                }
            }
            Pixel[] tpts = pts.toArray(new Pixel[pts.size()]);
            AlgoKMeans ak = new AlgoKMeans(10, new NormeCIELAB());
            ArrayList<ArrayList<Pixel>> biomes = ak.algorithmeClustering(tpts);
            AlgoDbScan ads = new AlgoDbScan();
            double eps = 3;
            int minPts = 3;
            ArrayList<Pixel> bruits = ads.DBSCAN(biomes.get(2), eps, minPts);

            //70 au rouge et au bleu si pas dans a liste

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
            ImageIO.write(image2, "png", new File("./img/result/dbscan_result.png"));
            System.out.println("Traitement terminé.");


        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }
}