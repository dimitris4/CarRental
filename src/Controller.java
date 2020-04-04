import java.sql.SQLException;
import java.util.HashSet;

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
    public void displayActiveContracts() {contractMethods.displayActiveContracts();}
    public void displayOldContracts() {contractMethods.displayOldContracts();}
    public void searchContractByStartDate() {contractMethods.searchContractsByStartDate();}
    public void searchContractByEndDate() {contractMethods.searchContractsByEndDate();}
    public void searchContractsByRegNo() {contractMethods.searchContractsByRegNo();}
    public void deleteContract() throws SQLException {contractMethods.deleteContract();}
    public void addContract() throws Exception {contractMethods.addContract();}
    public HashSet<String> displayCars(String condition) throws SQLException{return carMethods.displayCars(condition); }
    public void displayRenters() {renterMethods.displayRenters();}
    public void endContract() throws SQLException{contractMethods.endContract();}
    public void sendMail(String recipient, String myMessage) throws Exception { contractMethods.sendMail(recipient, myMessage); }
    public void makeUnavailable2(String regNo) throws SQLException {carMethods.makeUnavailable2(regNo);}
}
