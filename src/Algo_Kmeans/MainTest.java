package Algo_Kmeans;

import outils.AlgoInterface;

public class MainTest {

    public static void main(String[] args) {
        AlgoKMeans algo = new AlgoKMeans(3);

        algo.algorithmeKMeans("./img/Planete1.jpg", "./img/Planete1AlgoKMeans.jpg");
    }

}
