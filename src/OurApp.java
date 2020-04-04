public class OurApp {

    private static Controller controller;

    public static void main(String[] args) throws Exception {


        controller = new Controller();

        //creating a controller object
        controller = new Controller();
        //Our first login menu is called
        new Start();
    }

    //a getter method returning the controller object
    public static Controller getController() {
        return controller;
    }

}
