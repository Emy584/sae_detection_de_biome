# SAE  Détection de biomes sur des exoplanètes - Juin 2026

## Groupe : RA-IL 1
**Composition du groupe :**
- DUMONT Nathan
- FOUSSE Emelyne
- GUERIN Léa
- HERMANN Taïno
- TOLKACHEVA Anastasia

Fonctionnement du projet : 
1) Choisir de flouter l'image par un flou moyen ou par un flou gaussien :
Dans le src/flou/MainTest.java : 

# Pour le flou Gaussien
fg.setFlou("../../img/image.jpg", "../../img/imageGauss.png", 0);
si on met une valeur invalide (en dessous de 1 ou un nombre pair), les valeurs suivantes sont appliquées par défaut :
taille du filtre : 25
sigma : 4

# Pour le flou moyen
fm.setFlou("../../img/image.jpg", "../../img/imageMoyenne.png", 0);
si on met une valeur invalide (en dessous de 1), la valeur suivante est appliquée par défaut :
taille du filtre : 9

2) Si on veut uniquement récupérer les biomes :
Dans le src/Algo_Kmeans/MainTest.java : 

On va choisir le nombre de centroïdes initiaux et la norme de couleur à appliquer (NormeRGB, NormeRedMean, NormeCIELAB, NormeCIE94)
AlgoKMeans algo = new AlgoKMeans(30, new NormeCIELAB());
Ensuite on va appliquer l'algorithme sur une image source (ici une image de Gauss floutée) vers une image de destination
algo.algorithmeKMeans("./img/imageGauss.png", "./img/Planete1AlgoKMeans.jpg");

3) Si on veut récupérer les biomes et les écosystèmes :
Dans le src/Algo_DbSca,/Main.java :
On va java Main.java

PS : Selon l'IDE, changer le chemin pour chercher l'image et la sauvegarder 

On va choisir le nombre de groupes à traiter puis le biome sur lesquel on applique DbScan. 
L'image est générée dans img/result.
