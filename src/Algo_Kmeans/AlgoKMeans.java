package Algo_Kmeans;

import Algo_DbScan.Pixel;
import Normes.NormeCouleurs;
import outils.OutilCouleur;
import outils.AlgoInterface;
import outils.Palette;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class AlgoKMeans implements AlgoInterface {

    // nb biomes
    private int nbGroupes;
    // tableau des centroides
    private double[][] centroidesFinaux;
    // norme utilisée
    private NormeCouleurs norme;

    public AlgoKMeans(int nbGroupes, NormeCouleurs norme) {
        this.nbGroupes = nbGroupes;
        this.norme = norme;
    }

    /**
     * méthode qui applique l'algorithme de Kmeans sur un tableau de pixel
     * @param tableauD le tableau qui contient les pixels à regrouper
     * @return un tableau contenant une liste de groupes
     */
    @Override
    public ArrayList<ArrayList<Pixel>> algorithmeClustering(Pixel[] tableauD) {

        // nombre total de pixels
        int nbObj = tableauD.length;

        // initialise les centroides
        double[][] centroides = initialiserCentroides(tableauD);
        // numéro du cluster auquel appartient le pixel i
        int[] groupes = new int[nbObj];
        // indique si une itération a modifié des affectations
        boolean changement = true;

        // mise en cache des couleurs
        boolean utiliseCIELAB = (norme instanceof Normes.NormeCIELAB);
        boolean utiliseRGB = (norme instanceof Normes.NormeRGB);
        boolean cheminRapide = utiliseCIELAB || utiliseRGB;

        double[][] cachePixels = null;

        // pré-calcul des couleurs des pixels
        if (cheminRapide) {
            cachePixels = new double[nbObj][3];
            System.out.println("Pré-calcul des couleurs en cache");

            for (int i = 0; i < nbObj; i++) {
                Color c = new Color(tableauD[i].c);

                if (utiliseCIELAB) {
                    // on stocke les valeurs L*a*b*
                    int[] lab = Normes.MainCIE94.rgb2lab(c.getRed(), c.getGreen(), c.getBlue());
                    cachePixels[i][0] = lab[0];
                    cachePixels[i][1] = lab[1];
                    cachePixels[i][2] = lab[2];
                } else {
                    // RGB classique
                    cachePixels[i][0] = c.getRed();
                    cachePixels[i][1] = c.getGreen();
                    cachePixels[i][2] = c.getBlue();
                }
            }
        }

        System.out.println("Début de la boucle K-Means");
        int numeroIteration = 1;

        // boucle principale
        while (changement) {
            changement = false;

            // cache des centroïdes pour cette itération
            double[][] cacheCentroides = null;

            if (cheminRapide) {
                cacheCentroides = new double[nbGroupes][3];
                for (int j = 0; j < nbGroupes; j++) {
                    // on force les valeurs RGB dans [0,255]
                    int r = Math.max(0, Math.min(255, (int) centroides[j][0]));
                    int g = Math.max(0, Math.min(255, (int) centroides[j][1]));
                    int b = Math.max(0, Math.min(255, (int) centroides[j][2]));

                    if (utiliseCIELAB) {
                        // on stocke les valeurs L*a*b*
                        int[] lab = Normes.MainCIE94.rgb2lab(r, g, b);
                        cacheCentroides[j][0] = lab[0];
                        cacheCentroides[j][1] = lab[1];
                        cacheCentroides[j][2] = lab[2];
                    } else {
                        // RGB classique
                        cacheCentroides[j][0] = r;
                        cacheCentroides[j][1] = g;
                        cacheCentroides[j][2] = b;
                    }
                }
            }

            // Boucle d'assignation
            for (int i = 0; i < nbObj; i++) {
                int meilleurGroupe = 0;
                double meilleurDistance = Double.MAX_VALUE;

                Color couleurPixelLente = null;

                // on doit reconstruire la couleur
                if (!cheminRapide) {
                    couleurPixelLente = new Color(tableauD[i].c);
                }

                // Comparaison avec chaque centroïde
                for (int j = 0; j < nbGroupes; j++) {
                    double distance;

                    if (cheminRapide) {
                        // distance euclidienne
                        double d0 = cachePixels[i][0] - cacheCentroides[j][0];
                        double d1 = cachePixels[i][1] - cacheCentroides[j][1];
                        double d2 = cachePixels[i][2] - cacheCentroides[j][2];

                        distance = (d0 * d0) + (d1 * d1) + (d2 * d2);
                    } else {
                        // norme autre que CIELAB
                        Color couleurCentroide = new Color(
                                Math.max(0, Math.min(255, (int) centroides[j][0])),
                                Math.max(0, Math.min(255, (int) centroides[j][1])),
                                Math.max(0, Math.min(255, (int) centroides[j][2]))
                        );
                        distance = norme.distanceCouleurs(couleurPixelLente, couleurCentroide);
                    }

                    // mise à jour du meilleur cluster
                    if (distance < meilleurDistance) {
                        meilleurDistance = distance;
                        meilleurGroupe = j;
                    }
                }

                // si pixel change de cluster, on continue une itération supplémentaire
                if (groupes[i] != meilleurGroupe) {
                    groupes[i] = meilleurGroupe;
                    changement = true;
                }
            }
            // recalcul des centroïdes après l'assignation
            centroides = recalculerCentroides(tableauD, groupes);
            numeroIteration++;
        }

        // Sauvegarde des centroïdes finaux
        centroidesFinaux = centroides;

        // on regroupe les pixels par groupe
        ArrayList<ArrayList<Pixel>> groupesDePixels = new ArrayList<>();
        for (int j = 0; j < nbGroupes; j++) {
            groupesDePixels.add(new ArrayList<>());
        }
        for (int i = 0; i < nbObj; i++) {
            groupesDePixels.get(groupes[i]).add(tableauD[i]);
        }

        return groupesDePixels;
    }

    /**
     * méthode d'initialisation des centroides
     * @param tableauD  le tableau qui contient les pixels à regrouper
     * @return un tableau 2D contenant les centroïdes sous forme [k][RGB]
     */
    public double[][] initialiserCentroides(Pixel[] tableauD) {
        // nbGroupes lignes, 3 colonnes (R,G,B)
        double[][] centroides = new double[nbGroupes][3];
        int nbObjets = tableauD.length;

        // pour chaque groupe, on choisit un pixel aléatoire comme centroïde initial
        for (int k = 0; k < nbGroupes; k++) {
            // sélection d'un index aléatoire dans les pixels
            int index = (int) (Math.random() * nbObjets);
            // on récupère la couleur du pixel choisi
            Color c = new Color(tableauD[index].c);

            // on initialise le centroïde avec les valeurs RGB du pixel
            centroides[k][0] = c.getRed();
            centroides[k][1] = c.getGreen();
            centroides[k][2] = c.getBlue();
        }
        return centroides;
    }

    /**
     * méthode de recalcul des centroides
     * @param tableauD  le tableau qui contient les pixels à regrouper
     * @return  un tableau 2D contenant les nouveaux centroïdes [k][RGB]
     */
    public double[][] recalculerCentroides(Pixel[] tableauD, int[] groupes) {
        // nouveau tableau des centroïdes
        double[][] newCentroides = new double[nbGroupes][3];
        // compte le nb de pixel appartenant à chaque groupe
        int[] compte = new int[nbGroupes];

        // accumulation des valeurs RGB pour chaque groupe
        for (int i = 0; i < tableauD.length; i++) {
            // groupe du pixel i
            int g = groupes[i];
            Color c = new Color(tableauD[i].c);

            // additionne les composantes RGB du pixel dans le centroïde correspondant
            newCentroides[g][0] += c.getRed();
            newCentroides[g][1] += c.getGreen();
            newCentroides[g][2] += c.getBlue();

            // incrémente le nombre de pixels dans ce groupe
            compte[g]++;
        }

        // moyenne des valeurs RGB pour obtenir les nouveaux centroïdes
        for (int k = 0; k < nbGroupes; k++) {
            // si le groupe contient au moins un pixel
            if (compte[k] > 0) {
                newCentroides[k][0] /= compte[k];
                newCentroides[k][1] /= compte[k];
                newCentroides[k][2] /= compte[k];
            }
            // sinon inchangé
        }
        return newCentroides;
    }

    /**
     * méthode qui fusionne les groupes K-means qui correspondent au même biome
     * @param groupes les groupes issus de algorithmeClustering
     * @param palette la palette utilisée pour identifier les biomes
     * @return une map nom de biome -> tous les pixels des groupes concernés
     */
    public Map<String, ArrayList<Pixel>> fusionnerParBiome(ArrayList<ArrayList<Pixel>> groupes, Palette palette) {
        // map ordonnée qui contiendra : NomBiome -> liste de pixels fusionnés
        Map<String, ArrayList<Pixel>> pixelsParBiome = new LinkedHashMap<>();

        //parcourt chaque cluster K-means
        for (int i = 0; i < groupes.size(); i++) {
            // récupère la couleur du centroïde final du cluster i
            int r = Math.max(0, Math.min(255, (int) centroidesFinaux[i][0]));
            int g = Math.max(0, Math.min(255, (int) centroidesFinaux[i][1]));
            int b = Math.max(0, Math.min(255, (int) centroidesFinaux[i][2]));
            Color c = new Color(r, g, b);

            // trouve le biome dont la couleur de référence est la plus proche du centroïde
            String nomBiome = palette.trouverBiomeLePlusProche(c, norme);

            // si le biome n'existe pas encore dans la map, on l'ajoute
            pixelsParBiome.putIfAbsent(nomBiome, new ArrayList<>());
            // on ajoute tous les pixels du cluster i dans le biome correspondant
            pixelsParBiome.get(nomBiome).addAll(groupes.get(i));
        }
        return pixelsParBiome;
    }

    /**
     * méthode qui applique K Means sur une image complète
     * @param cheminSource chemin de l'image d'entrée
     * @param cheminDestination chemin de l'image de sortie
     */
    public void algorithmeKMeans(String cheminSource, String cheminDestination) {
        try {
            // chargement de l'image source
            File fichierSource = new File(cheminSource);
            BufferedImage imageSource = ImageIO.read(fichierSource);

            if (imageSource == null) {
                System.out.println("Erreur : impossible de lire l'image");
                return;
            }

            int largeur = imageSource.getWidth();
            int longeur = imageSource.getHeight();

            // conversion de l'image en tableau de Pixels
            // chaque pixel devient un objet Pixel(x, y, couleur)
            Pixel[] tableauPixel = new Pixel[largeur * longeur];
            int index = 0;
            for (int x = 0; x < largeur; x++) {
                for (int y = 0; y < longeur; y++) {
                    tableauPixel[index] = new Pixel(x, y, imageSource.getRGB(x, y));
                    index++;
                }
            }

            // exécution de K Means
            System.out.println("Lancement du K-Means avec " + nbGroupes + " groupes...");
            ArrayList<ArrayList<Pixel>> groupes = algorithmeClustering(tableauPixel);

            // création de l'image
            BufferedImage nvImage = new BufferedImage(largeur, longeur, BufferedImage.TYPE_INT_ARGB);
            for (int g = 0; g < groupes.size(); g++) {
                // récup la couleur du centroide final
                int r = Math.max(0, Math.min(255, (int) centroidesFinaux[g][0]));
                int gg = Math.max(0, Math.min(255, (int) centroidesFinaux[g][1]));
                int b = Math.max(0, Math.min(255, (int) centroidesFinaux[g][2]));
                int newRgb = OutilCouleur.getColor(r, gg, b);

                // applique cette couleur à tous les pixels du groupe
                for (Pixel p : groupes.get(g)) {
                    nvImage.setRGB(p.x, p.y, newRgb);
                }
            }

            // fusion des doublons de biomes car plusieurs groupes K-means peuvent correspondre au même biome
            Palette palette = new Palette();
            Map<String, ArrayList<Pixel>> pixelsParBiomeUnique = fusionnerParBiome(groupes, palette);

            for (String nomBiome : pixelsParBiomeUnique.keySet()) {
                System.out.println("Biome fusionné : " + nomBiome + " (" + pixelsParBiomeUnique.get(nomBiome).size() + " pixels)");
            }

            // génération du fond éclairci
            double pourcentageEclair = 75.0;
            BufferedImage imageEclaircie = new BufferedImage(largeur, longeur, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < largeur; x++) {
                for (int y = 0; y < longeur; y++) {
                    int numRgbBase = imageSource.getRGB(x, y);
                    imageEclaircie.setRGB(x, y, OutilCouleur.eclaircirPixel(numRgbBase, pourcentageEclair));
                }
            }

            // génération d'une seule image par biome complet
            for (String nomBiome : pixelsParBiomeUnique.keySet()) {
                // image du biome
                BufferedImage imageBiome = new BufferedImage(largeur, longeur, BufferedImage.TYPE_INT_ARGB);
                imageBiome.getGraphics().drawImage(imageEclaircie, 0, 0, null);

                ArrayList<Pixel> pixelsDuBiome = pixelsParBiomeUnique.get(nomBiome);
                for (Pixel p : pixelsDuBiome) {
                    imageBiome.setRGB(p.x, p.y, imageSource.getRGB(p.x, p.y));
                }

                // nom du fichier de sortie
                String nomFichier = cheminDestination.replace(".jpg", "_" + nomBiome + ".jpg");
                File fichierDestBiome = new File(nomFichier);
                ImageIO.write(imageBiome, "png", fichierDestBiome);

                System.out.println("Image du biome complet " + nomBiome + " sauvegardée : " + nomFichier);
            }

            // sauvegarde  de l'image
            File fichierDest = new File(cheminDestination);
            ImageIO.write(nvImage, "png", fichierDest);
            System.out.println("Algo terminé");

        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}