import flou.FlouParMoyenne;

public class Main {
  public static void main(String[] args) {
  
    FlouParMoyenne flouMoy = new FlouParMoyenne();
    flouMoy.setFlou("./img/Planete1.jpg", "./img/Planete1FlouParMoy3.jpg", 3);

    flouMoy.setFlou("./img/Planete1.jpg", "./img/Planete1FlouParMoy5.jpg", 5);
    
    
  }




}
