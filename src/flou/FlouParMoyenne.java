package flou;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FlouParMoyenne implements FlouInterface {

  /**
   * 
   * @param cheminSource
   * @param cheminDestination
   * @param filtre taille de la grille
   */
  @Override
  public void setFlou(String cheminSource, String cheminDestination, int filtre) {
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
      BufferedImage nvImage = new BufferedImage(largeur, longeur, BufferedImage.TYPE_3BYTE_BGR);

      int taille = filtre / 2;

      // on parcourt chaque pixel
      for (int x = 1; x < largeur - 1; x++) {
        for (int y = 1; y < longeur - 1; y++) {
          
          int sommeRouge = 0;
          int sommeVert = 0;
          int sommeBleu = 0;
        
          // on parcourt la grille 3*3 autour du pixel (x, y)
          for (int i = -taille; i <= taille; i++) {
            for (int j = -taille; j <= taille; j++) {
              // on récupère le voisin 
              int rgbVoisin = imageSource.getRGB(x+i, y+j);

              // on récupère rouge vert bleu
              int[] rgb = OutilCouleur.getTabColor(rgbVoisin);

              // on ajoute pour la somme
              sommeRouge += rgb[0];
              sommeVert  += rgb[1];
              sommeBleu  += rgb[2];

            }
          }
          // On calcule la moyenne pour chaque pixel (division par filtre² cases)
          int moyRouge = sommeRouge / filtre*filtre;
          int moyVert  = sommeVert / filtre*filtre;
          int moyBleu  = sommeBleu / filtre*filtre;

          // calcul de la nouvelle couleur floutée du pixel
          int nouvelleCouleur = OutilCouleur.combineCouleur(moyRouge, moyVert, moyBleu);

          // on applique le pixel flou sur la nouvelle image
          nvImage.setRGB(x, y, nouvelleCouleur);
          
        }
      }

      // sauvegarde le résultat
      File fichierDest = new File(cheminDestination);
      ImageIO.write(nvImage, "png", fichierDest);
      System.out.println("Copie pixel par piwel terminée");

    } catch (IOException e) {
      System.err.println("Erreur lors de la copie : " + e.getMessage());
    }

  }
  
}
