package HAC;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        double[][] matriceDistances = {
                {0.0, 1.0, 4.0, 5.0},
                {1.0, 0.0, 3.5, 4.8},
                {4.0, 3.5, 0.0, 2.5},
                {5.0, 4.8, 2.5, 0.0}
        };

        double[][] coordonneesPoints = {
                {1.0, 2.0}, // Point 0
                {1.5, 2.5}, // Point 1
                {5.0, 6.0}, // Point 2
                {6.0, 7.0}  // Point 3
        };

        System.out.println("Choix de l'algorithme");
        System.out.println("1: Single Linkage");
        System.out.println("2: Complete Linkage");
        System.out.println("3: Average Linkage");
        System.out.println("4: Centroid Linkage");
        System.out.print("choix: ");

        Scanner scanner = new Scanner(System.in);
        int choix = scanner.nextInt();

        HAC algorithmeHAC;

        if (choix == 4) {
            algorithmeHAC = new HAC(coordonneesPoints, choix);
        } else {
            algorithmeHAC = new HAC(matriceDistances, choix);
        }

        Cluster resultat = algorithmeHAC.calculer();

        scanner.close();

    }
}
