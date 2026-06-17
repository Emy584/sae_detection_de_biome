package Algo_Kmeans;

import Normes.NormeCIELAB;
import Normes.NormeRGB;

public class MainTest {

    public static void main(String[] args) {
    AlgoKMeans algo = new AlgoKMeans(30, new NormeCIELAB());
        algo.algorithmeKMeans("./img/imageGauss.png", "./img/Planete1AlgoKMeans.jpg");
    }

}
