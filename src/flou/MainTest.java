package flou;

public class MainTest {
    public static void main(String[] args) {
        FlouParMoyenne fm =  new FlouParMoyenne();
        FlouGaussien fg = new FlouGaussien();

        fg.setFlou("C:\\Users\\06tai\\Desktop\\sae_detection_de_biome\\src\\img\\image.jpg", "C:\\Users\\06tai\\Desktop\\sae_detection_de_biome\\src\\img\\imageGauss.png", 0);
        fm.setFlou("C:\\Users\\06tai\\Desktop\\sae_detection_de_biome\\src\\img\\image.jpg", "C:\\Users\\06tai\\Desktop\\sae_detection_de_biome\\src\\img\\imageMoyenne.png", 0);
    }
}
