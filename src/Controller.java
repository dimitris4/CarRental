import java.sql.SQLException;
import java.text.ParseException;

public class Controller {
    private static CarMethods carMethods;
    private static RenterMethods renterMethods;
    private static ContractMethods contractMethods;

    public Controller() throws SQLException, ParseException {
        carMethods = new CarMethods();
        renterMethods = new RenterMethods();
        contractMethods = new ContractMethods();
    }

    // car methods
    public void displayCars() throws SQLException {carMethods.displayCars(null);}
    public void addCar() throws SQLException {carMethods.addCar();}
    public void deleteCar() throws SQLException, ParseException {carMethods.deleteCar();}
    public void editCar() throws SQLException, ParseException {carMethods.editCar();}
    public void makeUnavailable() throws SQLException {carMethods.makeUnavailable();}
    public void makeAvailable() throws SQLException {carMethods.makeAvailable();}

    // renter methods
    public void addRenter() { renterMethods.add(); }
    public void editRenter() throws SQLException { renterMethods.update(); }
    public void deleteRenter() throws SQLException { renterMethods.remove(); }
    public void displayRenters() { renterMethods.displayRenters(); }

    // contract methods
    public void addContract() throws Exception { contractMethods.addContract(); }
    public void endContract() throws SQLException { contractMethods.endContract(); }
    public void deleteContract() throws SQLException { contractMethods.deleteContract(); }
    public void displayActiveContracts() { contractMethods.displayActiveContracts(); }
    public void displayOldContracts() { contractMethods.displayOldContracts(); }
    public void searchByStartDate() { contractMethods.searchContractsByStartDate(); }
    public void searchByEndDate() { contractMethods.searchContractsByEndDate(); }
    public void searchByRegNumber() throws SQLException { contractMethods.searchContractsByRegNo(); }
}
