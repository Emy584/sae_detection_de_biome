package Algo_Kmeans;

import flou.OutilCouleur;
import outils.AlgoInterface;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Réflexion
 */
// on definit rouge bleu vert avec getTabColor
// on place nb groupe centroides aléatoires
// charge l'image
// pour longueur
// pour largeur
// on place chaque pixel dans un groupe (tab[]) en fonction de la distance de couleur la plus proche d'un centroide
// mise a jour des centroides avec moyenne des pixels des goupes
// boucle sur chaque groupe (tab) pour calcul moyenne
// les nbGroupe moyennes deviennent les new centroides
// on place chaque pixel dans un groupe (tab[]) en fonction de la distance de couleur la plus proche d'un centroide
// mise a jour des centroides avec moyenne des pixels des goupes
// boucle sur chaque groupe (tab) pour calcul moyenne
// les nbGroupe moyennes deviennent les new centroides

public class AlgoKMeans implements AlgoInterface {

    // nombre de groupe
    private int nbGroupes;
    private double[][] centroidesFinaux;


    public AlgoKMeans(int nbGroupes) {
        this.nbGroupes = nbGroupes;
    }

    /**
     * méthode qui applique l'algorithme de Kmeans
     * @param tableauD un tableau à deux dimensions avec l'index d'un pixel et [r,g,b]
     * @return un tableau de groupes
     */
    @Override
    public int[] algorithmeClustering(int[][] tableauD) {
        // on recup la longueur du tableau a deux dimensions
        // nb de pixels
        int nbObj = tableauD.length;
        // r, g et b
        int nbCar = tableauD[0].length;

        // initialise les centroides
        double[][] centroides = initialiserCentroides(tableauD);

        // création d'un tableau de groupe
        int[] groupes = new int[nbObj];

        // condition de boucle
        boolean changement = true;

        // boucle
        while (changement) {
            changement = false;

            for (int i = 0; i < nbObj; i++) {
                int meilleurGroupe = 0;
                double meilleurDistance = Double.MAX_VALUE;

                for (int j = 0; j < nbGroupes; j++) {
                    double distance = calculeDistance(tableauD[i], centroides[j]);

                    if (distance < meilleurDistance) {
                        meilleurDistance = distance;
                        meilleurGroupe = j;
                    }
                }
                if (groupes[i] != meilleurGroupe) {
                    groupes[i] = meilleurGroupe;
                    changement = true;
                }
            }
            centroides = recalculerCentroides(tableauD, groupes);
        }
        centroidesFinaux = centroides;
        return groupes;
    }

    /**
     * méthode permettant d'initialiser les centroides
     * @param tableauD le tableau de pixels
     * @return un tableau à deux dimensions avec les centroides
     */
    public double[][] initialiserCentroides(int[][] tableauD) {
        double[][] centroides = new double[nbGroupes][3];
        int nbObjets = tableauD.length;

        // Choix aléatoire
        for (int k = 0; k < nbGroupes; k++) {
            int index = (int) (Math.random() * nbObjets);

            centroides[k][0] = tableauD[index][0];
            centroides[k][1] = tableauD[index][1];
            centroides[k][2] = tableauD[index][2];
        }
        return centroides;
    }

    /**
     * méthode de calcul de la distance entre chaque rgb du tableau de pixels et du rgb de chaque centroide
     * @param pixelTab tableau de pixels
     * @param pixelCentroide tableau de centroides
     * @return la distance moyenne entre pixel et centroides
     */
    public double calculeDistance(int[] pixelTab, double[] pixelCentroide) {

        double varR = pixelTab[0] - pixelCentroide[0];
        double varG = pixelTab[1] - pixelCentroide[1];
        double varB = pixelTab[2] - pixelCentroide[2];

        // retourne la moyenne
        return Math.sqrt(Math.pow(varR, 2) + Math.pow(varG, 2) + Math.pow(varB, 2));
    }

    /**
     * méthode qui réinitialise les centroides
     * @param tableauD tableau de pixels
     * @param groupes tableau des groupes
     * @return retourne un tableau de centroides avec index et [r,g,b]
     */
    public double[][] recalculerCentroides(int[][] tableauD, int[] groupes) {
        // nouveau tableau de centroides
        double[][] newCentroides = new double[nbGroupes][3];
        // servira d'index
        int[] compte = new int[nbGroupes];

        // somme des pixels par groupe
        for (int i = 0; i < tableauD.length; i++) {
            int g = groupes[i];
            newCentroides[g][0] += tableauD[i][0];
            newCentroides[g][1] += tableauD[i][1];
            newCentroides[g][2] += tableauD[i][2];
            compte[g]++;
        }

        // moyenne des couleurs
        for (int k = 0; k < nbGroupes; k++) {
            if (compte[k] > 0) {
                newCentroides[k][0] /= compte[k];
                newCentroides[k][1] /= compte[k];
                newCentroides[k][2] /= compte[k];
            }
        }
        return newCentroides;
    }


    /**
     * méthode de résolution par K Means
     * @param cheminSource source de l'image
     * @param cheminDestination destination de l'image créée
     */
    public void algorithmeKMeans(String cheminSource, String cheminDestination) {
        try {
            // on charge l'image
            File fichierSource = new File(cheminSource);
            BufferedImage imageSource = ImageIO.read(fichierSource);

            if (imageSource == null) {
                System.out.println("Erreur : impossible de lire l'image");
                return;
            }

            // création d'une nv image vide avec les mêmes dimensions
            int largeur = imageSource.getWidth();
            int longeur = imageSource.getHeight();
            BufferedImage nvImage = new BufferedImage(largeur, longeur, BufferedImage.TYPE_INT_ARGB);

            int[] resultat = new int[nbGroupes];
            int[][] tableauPixel = new int[largeur*longeur][3];
            int index = 0;

            for (int x = 0; x < largeur; x++) {
                for (int y = 0; y < longeur; y++) {
                    // mettre l'index du pixel
                    // mettre le rgb du pixel
                    int numRgb = imageSource.getRGB(x, y);
                    int[] rgb = OutilCouleur.getTabColor(numRgb);

                    tableauPixel[index][0] = rgb[0];
                    tableauPixel[index][1] = rgb[1];
                    tableauPixel[index][2] = rgb[2];

                    index++;
                }
            }
            // on calcul avec l'algo de KMeans
            resultat = algorithmeClustering(tableauPixel);

            index = 0;

            for (int x = 0; x < largeur; x++) {
                for (int y = 0; y < longeur; y++) {

                    int cluster = resultat[index];

                    int r = (int) centroidesFinaux[cluster][0];
                    int g = (int) centroidesFinaux[cluster][1];
                    int b = (int) centroidesFinaux[cluster][2];

                    int newRgb = OutilCouleur.getColor(r, g, b);
                    nvImage.setRGB(x, y, newRgb);

                    index++;
                }
            }

            // sauvegarde le résultat
            File fichierDest = new File(cheminDestination);
            ImageIO.write(nvImage, "png", fichierDest);
            System.out.println(" Algo terminée");

        } catch (IOException e) {
            System.err.println("Erreur lors de la copie : " + e.getMessage());
        }
    }
}