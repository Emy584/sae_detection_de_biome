package Algo_DbScan;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        try {
            BufferedImage image = ImageIO.read(new File("./img/image.jpg"));

            ArrayList<Pixel> pts = new ArrayList<>();

            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pts.add(new Pixel(x, y, image.getRGB(x, y)));
                }
            }

            double eps = 10;
            int minPts = 5;

            Copieur.copierImage(image, eps, minPts, pts);

            System.out.println("Traitement terminé.");

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }
}