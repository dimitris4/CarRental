public class OurApp {

    private static Controller controller;

    public static void main(String[] args) throws Exception {

        RenterMethods rm = new RenterMethods();

        //rm.add();
        rm.update();

        //rm.displayRenterTable();
        //rm.displayPhoneTable();
        /*//creating a controller object
        controller = new Controller();
        //Our first login menu is called
        new Start();*/
    }

    //a getter method returning the controller object
    public static Controller getController() {
        return controller;
    }
}
