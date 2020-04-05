import java.sql.SQLException;
import java.util.Date;

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
    public void deleteCar() throws SQLException {carMethods.deleteCar();}
    public void editCar() throws SQLException {carMethods.editCar();}
    public void displayAvailableCarsWithinDateRange(Date startDate, Date endDate) throws SQLException {carMethods.displayAvailableCarsWithinDateRange(startDate,endDate);}
    public void makeUnavailable() throws SQLException {carMethods.makeUnavailable();}
    public void makeAvailable() throws SQLException {carMethods.makeAvailable();}
    //public void sendMail(String recipient, String myMessage) throws Exception { renterMethods.sendMail(recipient, myMessage); }
}
