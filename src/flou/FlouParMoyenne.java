package flou;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FlouParMoyenne implements FlouInterface {

    private int taille_filtre = 9;

    /**
     *
     * @param cheminSource chemin de l'image à flouter
     * @param cheminDestination chemin de l'image floutée
     * @param tailleFiltre taille de la grille
     */
    @Override
    public void setFlou(String cheminSource, String cheminDestination, int tailleFiltre) {
        try {
            // On charge l'image
            File fichierSource = new File(cheminSource);
            BufferedImage imageSource = ImageIO.read(fichierSource);

            if (imageSource == null) {
                System.out.println("Erreur : impossible de lire l'image");
                return;
            }

            System.out.println("Image chargée : " + fichierSource.getName());

            // création d'une nouvelle image vide avec les mêmes dimensions
            int largeur = imageSource.getWidth();
            int longeur = imageSource.getHeight();
            BufferedImage nouvelleImage = new BufferedImage(largeur, longeur, BufferedImage.TYPE_INT_ARGB);

            if (tailleFiltre > 0) {
                this.taille_filtre = tailleFiltre;
            } else {
                System.out.println("Taille de filtre incorect, application de la valeur par défaut");
            }

            System.out.println("Floutage avec un filtre de taille " + this.taille_filtre);

            int taille = this.taille_filtre / 2;
            int diviseur = this.taille_filtre * this.taille_filtre;

            System.out.println("Création de la nouvelle image ...");

            // On parcourt chaque pixel
            for (int x = taille; x < largeur - taille; x++) {
                for (int y = taille; y < longeur - taille; y++) {

                    int sommeRouge = 0;
                    int sommeVert = 0;
                    int sommeBleu = 0;

                    // On parcourt la grille tailleFiltre * tailleFiltre autour du pixel (x, y)
                    for (int i = -taille; i <= taille; i++) {
                        for (int j = -taille; j <= taille; j++) {

                            // Gestion des bords
                            int voisinX = Math.min(largeur - 1, Math.max(0, x + i));
                            int voisinY = Math.min(longeur - 1, Math.max(0, y + j));

                            int rgbVoisin = imageSource.getRGB(voisinX, voisinY);

                            // On récupère rouge vert bleu
                            int[] rgb = OutilCouleur.getTabColor(rgbVoisin);

                            // On ajoute pour les sommes
                            sommeRouge += rgb[0];
                            sommeVert += rgb[1];
                            sommeBleu += rgb[2];

                        }
                    }
                    // On calcule la moyenne pour chaque pixel (division par tailleFiltre² cases)
                    int moyRouge = sommeRouge / diviseur;
                    int moyVert = sommeVert / diviseur;
                    int moyBleu = sommeBleu / diviseur;

                    // Calcul de la nouvelle couleur floutée du pixel
                    int nouvelleCouleur = OutilCouleur.getColor(moyRouge, moyVert, moyBleu);

                    // on applique le pixel flou sur la nouvelle image
                    nouvelleImage.setRGB(x, y, nouvelleCouleur);

                }
            }

            // Sauvegarde le résultat
            File fichierDest = new File(cheminDestination);
            ImageIO.write(nouvelleImage, "png", fichierDest);
            System.out.println("Floutage de l'image réussi : " + fichierDest.getName());
        } catch (IOException e) {
            System.err.println("Erreur lors du floutage : " + e.getMessage());
        }

    }

}