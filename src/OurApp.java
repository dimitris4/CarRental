public class OurApp {

    private static Controller controller;

    public static void main(String[] args) throws Exception {

        controller = new Controller();

        new Start();
    }

    //a getter method returning the controller object
    public static Controller getController() {
        return controller;
    }

}
