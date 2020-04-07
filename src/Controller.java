import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
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
    public void displayActiveContracts() throws SQLException {contractMethods.displayActiveContracts();}
    public void displayOldContracts() throws SQLException {contractMethods.displayOldContracts();}
    public void initiateCarList() throws SQLException {carMethods.initiateCarList();}
    public void displayCars() throws SQLException {carMethods.displayCars(null);}
    public void addCar() throws SQLException {carMethods.addCar();}
    public void deleteCar() throws SQLException, ParseException {carMethods.deleteCar();}
    public void editCar() throws SQLException {carMethods.editCar();}
    public HashSet<String> displayAvailableCarsWithinDateRange(Date startDate, Date endDate) throws SQLException { return carMethods.displayAvailableCarsWithinDateRange(startDate,endDate); }
    public void makeUnavailable() throws SQLException {carMethods.makeUnavailable();}
    public void makeAvailable() throws SQLException {carMethods.makeAvailable();}
    public void fillRenterIDs() throws SQLException {contractMethods.fillRenterIDs();}

    public void makeUnavailable(String registration_number) throws SQLException {
        carMethods.makeUnavailable(registration_number);
    }

    public void sendMail(String recipient, String myMessage) throws Exception { contractMethods.sendMail(recipient, myMessage); }

    public void searchContractByStartDate() {contractMethods.searchContractsByStartDate();}
    public void searchContractByEndDate() {contractMethods.searchContractsByEndDate();}
    public void searchContractsByRegNo() {contractMethods.searchContractsByRegNo();}
    public void deleteContract() throws SQLException {contractMethods.deleteContract();}
    public void addContract() throws Exception {contractMethods.addContract();}
    public void endContract() throws SQLException{contractMethods.endContract();}


    //public void sendMail(String recipient, String myMessage) throws Exception { renterMethods.sendMail(recipient, myMessage); }
}
