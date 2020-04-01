public class Controller {

    private static CarMethods carMethods;
    private static RenterMethods renterMethods;
    private static ContractMethods contractMethods;

    public Controller() {
        carMethods = new CarMethods();
        renterMethods = new RenterMethods();
        contractMethods = new ContractMethods();
    }

    //public void sendMail(String recipient, String myMessage) throws Exception { renterMethods.sendMail(recipient, myMessage); }
}
