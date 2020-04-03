import java.sql.SQLException;

public class Controller {

    private static CarMethods carMethods;
    private static RenterMethods renterMethods;
    private static ContractMethods contractMethods;

    public Controller() throws SQLException {
        carMethods = new CarMethods();
        renterMethods = new RenterMethods();
        contractMethods = new ContractMethods();
    }

    public void initiateContractList() throws SQLException {contractMethods.initiateContractList();}
    public void displayActiveContracts() throws SQLException {contractMethods.displayActiveContracts();}
    public void displayOldContracts() throws SQLException {contractMethods.displayOldContracts();}
    //public void sendMail(String recipient, String myMessage) throws Exception { renterMethods.sendMail(recipient, myMessage); }
}
