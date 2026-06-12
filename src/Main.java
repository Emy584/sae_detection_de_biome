import flou.FlouParMoyenne;

public class Main {
  public static void main(String[] args) {
  
    FlouParMoyenne flouMoy = new FlouParMoyenne();
    flouMoy.setFlou("./img/originale.png", "./img/originaleFlouParMoyenne3.png", 3);

    flouMoy.setFlou("./img/originale.png", "./img/originaleFlouParMoyenne5.png", 5);
    
    
  }




}
