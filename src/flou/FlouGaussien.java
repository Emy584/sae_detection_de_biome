package flou;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FlouGaussien implements FlouInterface {

    // Constante /!\ Taille filtre doit être impaire
    final int TAILLE_FILTRE = 7;
    final double SIGMA = 1;

    @Override
    public void setFlou(String cheminSource, String cheminDestination) {
        try {
            // On charge l'image
            File fichierSource = new File(cheminSource);
            BufferedImage imageSource = ImageIO.read(fichierSource);

            // On verifie que l'image a correctement chargés
            if (imageSource == null) {
                System.out.println("Erreur : impossible de lire l'image");
                return;
            }

            // Création du filtre Gaussien
            double[][] filtre = this.calculFiltreGauss(this.TAILLE_FILTRE, this.SIGMA);

            // Création d'une nv image vide avec les mêmes dimensions
            int largeur = imageSource.getWidth();
            int longeur = imageSource.getHeight();

            BufferedImage nvImage = new BufferedImage(largeur, longeur, BufferedImage.TYPE_3BYTE_BGR);

            // On parcours chaque piexel de l'image
            for (int x = 0; x < largeur; x++) {
                for (int y = 0; y < longeur; y++) {

                    Color newCol = convolution(x, y, largeur, longeur, filtre, imageSource);

                    nvImage.setRGB(x, y, newCol.getRGB());
                }
            }

            // Sauvegarde du résultat
            File fichierDest = new File(cheminDestination);
            ImageIO.write(nvImage, "png", fichierDest);
            System.out.println("Copie pixel par pixel terminée");

        } catch (IOException e) {
            System.err.println("Erreur lors de la copie : " + e.getMessage());
        }
    }

    // Fonction pour appliqué la formule de Gauss
    public double formuleDeGauss(double x, double y, double sigma) {

        //Calcul 1er facteur de la multiplication
        double res = 2 * Math.PI * Math.pow(sigma, 2);
        res = 1 / res;

        //Calcul 2eme facteur de la multiplication
        double exp = (x * x) + (y * y);
        exp = exp / (2 * Math.pow(sigma, 2));
        exp = 0 - exp;
        exp = Math.exp(exp);

        return res * exp;
    }

    // Fonction qui calcul le filtre Gaussien à partir de la taille et du sigma
    public double[][] calculFiltreGauss(int largeurFiltre, double sigma) {

        double[][] filtre = new double[largeurFiltre][largeurFiltre];
        double somme = 0;

        int rayon = (largeurFiltre - 1) / 2;

        for (int i = 0; i < largeurFiltre; i++) {
            for (int j = 0; j < largeurFiltre; j++) {
                filtre[i][j] = formuleDeGauss(Math.abs(rayon - i), Math.abs(rayon - j), sigma);

                somme += filtre[i][j];
            }
        }

        // Normalisation pour que la somme fasse 1
        for (int i = 0; i < largeurFiltre; i++) {
            for (int j = 0; j < largeurFiltre; j++) {

                filtre[i][j] = filtre[i][j] / somme;

            }
        }
        return filtre;
    }

    // Fonction qui caclcul la convolution de la couleur
    private Color convolution(int x, int y, int largeur, int longeur, double[][] filtre, BufferedImage imageSource) {
        // On calcul le rayon (util apres)
        int rayon = (TAILLE_FILTRE - 1) / 2;

        // Tableau de la nouvelle couleur (avec les valeurs RGB des voisins accumulé)
        int[] newTabCol = {0, 0, 0};

        // On parcours tout les pixels autour du point
        for (int i = -rayon; i <= rayon; i++) {
            for (int j = -rayon; j <= rayon; j++) {

                // On recupère les coordonées X Y du pixel que l'on va accumulé
                int voisinX = x + i;
                int voisinY = y + j;

                //Verification si le pixel n'est pas en dehors de l'image
                // + Verification si le filtre multiplie par 0 on coupe direct
                if (voisinX >= 0 && voisinX < largeur && voisinY >= 0 && voisinY < longeur && filtre[i + rayon][j + rayon] != 0) {

                    //On récupère la couleur du pixel à accumulé
                    int couleur = imageSource.getRGB(voisinX, voisinY);
                    int[] tabCol = OutilCouleur.getTabColor(couleur);

                    // On parcours les 3 couleurs
                    for (int k = 0; k < 3; k++) {
                        newTabCol[k] += (int) (tabCol[k] * filtre[i + rayon][j + rayon]);
                    }
                }
            }
        }
        return new Color(newTabCol[0], newTabCol[1], newTabCol[2]);
    }
}
