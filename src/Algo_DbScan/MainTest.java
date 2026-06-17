package Algo_DbScan;

import javax.imageio.ImageIO;
import java.util.HashSet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainTest {



    public static void main (String[] args) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File("./img/Planete1(100*100).jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HashSet<Integer> pts = new HashSet<>();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pts.add(image.getRGB(x, y));
            }
        }
        //Copieur.optimiserParam(image, pts);
        Copieur.copierImage(image, 35, 1, pts);
    }

}
