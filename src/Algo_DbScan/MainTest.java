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
            image = ImageIO.read(new File("./img/fleur100*100.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HashSet<Integer> pts = new HashSet<>();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pts.add(image.getRGB(x, y));
            }
        }
        Copieur.optimiserParam(image, pts);
        Copieur.copierImage(image, 100, 1, pts);
    }

}
