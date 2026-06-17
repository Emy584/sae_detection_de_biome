package flou;

public class MainTest {
    public static void main(String[] args) {
        FlouParMoyenne fm =  new FlouParMoyenne();
        FlouGaussien fg = new FlouGaussien();

        fg.setFlou("../../img/image.jpg", "../../img/imageGauss.png", 0);
        fm.setFlou("../../img/image.jpg", "../../img/imageMoyenne.png", 0);
    }
}
