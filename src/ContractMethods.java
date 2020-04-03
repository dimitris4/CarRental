import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

import static java.sql.DriverManager.getConnection;

public class ContractMethods {
    private static Database database = new Database();

    public void initiateContractList() throws SQLException {
        Connection myConn = dbConnect();
        Statement myStmt = myConn.createStatement();
        String query = "SELECT * " +
                       "FROM Contract co " +
                       "JOIN Car c ON co.car_registration_number = c.registration_number " +
                       "JOIN Renter r ON r.renterid = co.renterid " +
                       "JOIN Brand b ON b.brandID = c.brandID " +
                       "JOIN Model m ON m.modelID = c.modelID " +
                       "JOIN Rental_types rt ON rt.rental_typeID = c.rental_typeID " +
                       "WHERE b.brandID = m.brandID";
        ResultSet myRs = myStmt.executeQuery(query);
        while (myRs.next()) {
            Contract c1 = new Contract();
            Car car = new Car();
            Renter renter = new Renter();
            CarType type = new CarType();
            Model model = new Model();
            Brand brand = new Brand();

            //Parsing brand info into the car object
            brand.setBrandID(myRs.getInt("b.brandID"));
            brand.setName(myRs.getString("b.name"));
            car.setBrand(brand);

            //Parsing model info into the car object
            model.setModelID(myRs.getInt("m.modelID"));
            model.setName(myRs.getString("m.name"));
            car.setModel(model);

            //Parsing rental type info into the car object
            type.setName(myRs.getString("rt.name"));
            type.setDescription(myRs.getString("rt.description"));
            car.setType(type);

            //Parsing car info into the car object
            car.setRegistration_number(myRs.getString("c.registration_number"));
            car.setFirst_registration(myRs.getDate("c.first_registration"));
            car.setOdometer(myRs.getInt("c.odometer"));

            //Parsing renter info into the renter object
            renter.setFirst_name(myRs.getString("r.first_name"));
            renter.setLast_name(myRs.getString("r.last_name"));
            renter.setDriverLicenseNumber(myRs.getString("r.driver_license_number"));


            c1.setRenter(renter);
            c1.setContractID(myRs.getInt("co.contractID"));
            c1.setCar(car);
            Date start = myRs.getDate("co.start_time");
            c1.setStartDate(start);
            Date end = myRs.getDate("co.end_time");
            c1.setEndDate(end);
            database.getContracts().add(c1);
        }
        myConn.close();
    }

    public void displayActiveContracts(){

        System.out.printf("%-20s%-20s%-40s%-60s%-20s%-20s%-20s\n", "Contract ID", "Registration No", "Brand/Model", "Renter's Name", "Driver License No", "Start Date", "End Date");
        System.out.println("*******************************************************************************************************************************************************************************************");

        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());

        for (int i = 0; i < database.getContracts().size(); i++){
                if(database.getContracts().get(i).getEndDate().compareTo(date)>=0){
                    System.out.println(database.getContracts().get(i));
                }
        }
        System.out.println("===========================================================================================================================================================================================");
    }

    public void displayOldContracts() {
        System.out.printf("%-20s%-20s%-40s%-60s%-20s%-20s%-20s\n", "Contract ID", "Registration No", "Brand/Model", "Renter's Name", "Driver License No", "Start Date", "End Date");
        System.out.println("*******************************************************************************************************************************///////////////////////////////////*************************");

        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());

        for (int i = 0; i < database.getContracts().size(); i++){
            if(database.getContracts().get(i).getEndDate().compareTo(date)<0){
                System.out.println(database.getContracts().get(i));
            }
        }
        System.out.println("===========================================================================================================================================================================================");
    }

    public void searchContractsByStartDate() {

    }

    public void searchContractsByEndDate() {

    }

    public void searchContractsByCarRegistrationNumber() {

    }

    public void searchContractsByRenterDriverLicense() {
    }

    public Connection dbConnect(){
        Connection myConn = null;
        try {
            // 1. get a connection to database
            myConn = getConnection("jdbc:mysql://localhost:3306/kailua", "dimk", "dimk1234!");
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return myConn;
    }



}
