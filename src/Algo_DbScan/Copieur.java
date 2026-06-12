package Algo_DbScan;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Copieur {


    public static void copierImage(BufferedImage image, double eps, int minPts, HashSet<Integer> pts) {

        AlgoDbScan a = new AlgoDbScan() ;
        a.DBSCAN(pts, eps, minPts);
        System.out.println("Nombre de couleurs uniques : " + pts.size());
        System.out.println("Nombre de clusters : " + a.clusters.size());
        System.out.println("Taille palette : " + a.getPalette().size());
        Palette p = new Palette(a.getPalette());
        BufferedImage image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                Color c = new Color(rgb);
                image2.setRGB(x, y, p.getPlusProche(c).getRGB());
            }
        }
        try {
            ImageIO.write(image2, "PNG", new File("test.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void optimiserParam(BufferedImage image, HashSet<Integer> pts) {
        double[] epsTests = {100, 200, 400, 900, 1600, 2500, 4900, 10000};
        int[] minPtsTests = {1, 2, 3, 4, 5, 6, 7, 8, 9,  10, 20};

        double meilleurEps = 0;
        int meilleurMinPts = 0;
        int meilleureTaillePalette = 0;
        double meilleurSim = 0 ;

        for (double eps : epsTests) {
            for (int minPts : minPtsTests) {

                AlgoDbScan algo = new AlgoDbScan();
                algo.DBSCAN(pts, eps, minPts);

                ArrayList<Color> paletteCouleurs = algo.getPalette();

                if (paletteCouleurs.isEmpty()) {
                    System.out.println("eps=" + eps + ", minPts=" + minPts + " -> aucun cluster");
                    continue;
                }

                Palette palette = new Palette(paletteCouleurs);

                BufferedImage imageTest = new BufferedImage(
                        image.getWidth(),
                        image.getHeight(),
                        BufferedImage.TYPE_3BYTE_BGR
                );

                for (int x = 0; x < image.getWidth(); x++) {
                    for (int y = 0; y < image.getHeight(); y++) {
                        Color c = new Color(image.getRGB(x, y));
                        imageTest.setRGB(x, y, palette.getPlusProche(c).getRGB());
                    }
                }

                double similarite = OutilCouleur.similarite(image, imageTest);

                if (similarite > meilleurSim) {
                    meilleurEps = eps;
                    meilleurMinPts = minPts;
                    meilleurSim = similarite;
                    meilleureTaillePalette = paletteCouleurs.size();
                }
            }
        }
        System.out.println();
        System.out.println("eps : " + meilleurEps);
        System.out.println("minPts : " + meilleurMinPts);
        System.out.println("taille palette : " + meilleureTaillePalette);
        System.out.println("similarite : " + meilleurSim);
    }
}
