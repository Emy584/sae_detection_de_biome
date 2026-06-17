package Algo_DbScan;

import Algo_Kmeans.AlgoKMeans;

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
            BufferedImage image = ImageIO.read(new File("./img/image.jpg"));

            ArrayList<Pixel> pts = new ArrayList<>() ;
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int rgb = image.getRGB(x, y);
                    pts.add(new Pixel(x, y, rgb));
                }
            }

            AlgoKMeans ak = new AlgoKMeans(10);
            ArrayList<ArrayList<Pixel>> biomes = ak.algorithmeClustering(pts);
            AlgoDbScan ads = new AlgoDbScan();
            double eps = 10;
            int minPts = 5;
            ArrayList<Pixel> bruits = ads.DBSCAN(biomes.get(0), eps, minPts);

            //70 au rouge et au bleu si pas dans a liste

            BufferedImage image2 = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_3BYTE_BGR
            );

            Random random = new Random();

            // Pour chaque cluster
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

            // Pour les bruits
            for (Pixel p : bruits) {
                image2.setRGB(p.x, p.y, Color.RED.getRGB());
            }

            // Pour tous les autres pixels
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {

                    if (image2.getRGB(x, y) == 0) {
                        Color c = new Color(image.getRGB(x, y));

                        int r = Math.min(c.getRed() + 70, 255);
                        int g = c.getGreen();
                        int b = Math.min(c.getBlue() + 70, 255);

                        image2.setRGB(x, y, new Color(r, g, b).getRGB());
                    }
                }
            }

            System.out.println("Traitement terminé.");

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }
}