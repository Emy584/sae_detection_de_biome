package Algo_DbScan;

import outils.OutilCouleur;
import outils.Palette;

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
        ArrayList<Integer> bruits = a.DBSCAN(pts, eps, minPts);
        System.out.println("Nombre de couleurs uniques : " + pts.size());
        System.out.println("Nombre de clusters : " + a.clusters.size());
        System.out.println("Nombre de bruits : " + bruits.size());
        System.out.println("Taille palette : " + a.getPalette().size());
        Palette p = new Palette(a.getPalette());
        BufferedImage image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                Color c = new Color(rgb);
                if (bruits.contains(c.getRGB())) {
                    image2.setRGB(x, y, Color.red.getRGB());
                } else {
                    image2.setRGB(x, y, p.getPlusProche(c).getRGB());
                }
            }
        }
        try {
            ImageIO.write(image2, "PNG", new File("./img/result/test.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void optimiserParam(BufferedImage image, HashSet<Integer> pts) {
        double[] epsTests = {1, 2, 3, 4, 5, 6,7,8,9,10,15,20,25,30,35};
        int[] minPtsTests = {1, 2, 3, 4, 5, 6, 7, 8, 9,  10};

        double meilleurEps = 0;
        int meilleurMinPts = 0;
        int meilleureTaillePalette = 0;
        int meilleurNbBruits = Integer.MAX_VALUE;
        double meilleurSim = 0 ;

        for (double eps : epsTests) {
            for (int minPts : minPtsTests) {

                AlgoDbScan algo = new AlgoDbScan();
                ArrayList<Integer> bruits = algo.DBSCAN(pts, eps, minPts);

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
                int nbBruits = bruits.size();


                if (nbBruits < meilleurNbBruits ||
                        (nbBruits == meilleurNbBruits && similarite > meilleurSim)) {

                    meilleurEps = eps;
                    meilleurMinPts = minPts;
                    meilleurSim = similarite;
                    meilleureTaillePalette = paletteCouleurs.size();
                    meilleurNbBruits = nbBruits;
                }
            }
        }
        System.out.println();
        System.out.println("eps : " + meilleurEps);
        System.out.println("minPts : " + meilleurMinPts);
        System.out.println("taille palette : " + meilleureTaillePalette);
        System.out.println("nb bruits : " + meilleurNbBruits);
        System.out.println("similarite : " + meilleurSim);
    }
}
