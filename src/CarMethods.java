import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.*;
import java.util.Date;

import static java.sql.DriverManager.getConnection;
import static java.sql.DriverManager.registerDriver;
import static java.util.Calendar.*;

public class CarMethods {

    private static Database database = Database.instance;
    private static Set<String> cars= new HashSet<>();
    private static Set<String> brands= new HashSet<>();
    private static Set<String> models= new HashSet<>();
    private static Set<String> fuels= new HashSet<>();
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Integer>months = new ArrayList<>(Arrays.asList(31,28,31,30,31,30,31,31,30,31,30,31));

    public CarMethods() throws SQLException {//populate hashsets and arraylists form DB
        ArrayList<Object> db = connect();
        Connection myConn = (Connection) db.get(0);
        Statement myStmt = (Statement) db.get(1);
        ResultSet myRs = myStmt.executeQuery("SELECT registration_number FROM Car");
        while(myRs.next()) { //populate hashset of registration_numbers for primary key tests
            cars.add(myRs.getString("registration_number"));
        }
        myRs = myStmt.executeQuery("SELECT name FROM Brand");
        while(myRs.next()) { //populate hashset of brands for primary key tests
            brands.add(myRs.getString("name"));
        }
        myRs = myStmt.executeQuery("SELECT name FROM Model");
        while(myRs.next()) { //populate hashset of models for primary key tests
            models.add(myRs.getString("name"));
        }
        myRs = myStmt.executeQuery("SELECT fuel_type FROM Fuel");
        while(myRs.next()) { //populate hashset of fuel types for primary key tests
            fuels.add(myRs.getString("fuel_type"));
        }
        closeConnection(myConn,myStmt,myRs);
    }

    public void initiateCarList() throws SQLException {
        ArrayList<Object> db = connect();
        Connection myConn = (Connection) db.get(0);
        Statement myStmt = (Statement) db.get(1);
        ResultSet myRs = myStmt.executeQuery("SELECT c.registration_number, c.first_registration, c.odometer, f.fuel_type, m.name, b.name, rt.name, rt.description, c.is_available " +
                ", c.fuelID, c.modelID, c.brandID, c.rental_typeID FROM Car c JOIN " +
                "Brand b ON c.brandID = b.brandID JOIN Model m ON c.modelID = m.modelID JOIN FUEL f ON c.fuelID = f.fuelID " +
                "JOIN rental_types rt ON c.rental_typeID = rt.rental_typeID");
        while(myRs.next()) { //populate arraylist of car information
            database.getCarList().add(new CarInformation (myRs.getString(1),myRs.getString(2),myRs.getInt(3),myRs.getString(4),myRs.getString(5),
                    myRs.getString(6),myRs.getString(7),myRs.getString(8),myRs.getInt(9),myRs.getInt(10),myRs.getInt(11),
                    myRs.getInt(12),myRs.getInt(13)));
        }
    }

    public String setRegistrationNumber(){
        System.out.print("Registration number: ");
        String registration_number = scanner.next();
        while(cars.contains(registration_number) || !registration_number.matches("[a-zA-Z0-9]+")){
            System.out.println("Invalid registration number. Please try again: ");
            registration_number = scanner.next();
        }
        return registration_number;
    }

    public String setFirstRegDate(){
        System.out.println("First registration date: ");
        System.out.print("Year: ");
        int year = scanner.nextInt();
        //first car created in 1885 (i think) so it can't be less than 1885
        // can't be higher than current year
        while(year<1885 || year>Calendar.getInstance().get(YEAR)){
            System.out.println("Invalid year value. Please insert value between 1885 -" + Calendar.getInstance().get(YEAR) + ": ");
            year = scanner.nextInt();
        }
        System.out.print("Month: ");
        int month = scanner.nextInt();
        while(month<0 || month>12){
            System.out.println("Invalid month value. Please insert value between 1 - 12: ");
            month = scanner.nextInt();
        }
        System.out.print("Day: ");
        int day = scanner.nextInt();
        while(dateCheck(year,month,day)){
            int leap=0;
            if (year%4==0 && month==2){
                leap++;
            }
            System.out.println("Invalid month value. Please insert value between 1 - " + (months.get(month-1)+leap) + ": ");
            day = scanner.nextInt();
        }
        return year + "-" + month + "-" + day;
    }

    public int setOdometer(){
        System.out.print("Odometer (amount of km): ");
        int odometer = scanner.nextInt();
        while(odometer<0){
            System.out.print("Invalid value. Please try again: ");
            odometer = scanner.nextInt();
        }
        return odometer;
    }

    public int setBrand(Connection myConn) throws SQLException {
        int amount;
        amount = displayBrand();
        System.out.println("Enter 0 to add new option");
        System.out.print("Brand (ID number): ");
        int brand = scanner.nextInt();
        while(brand<0 || brand>amount){
            System.out.print("Invalid value. Please try again: ");
            brand = scanner.nextInt();
        }
        if (brand==0){
            System.out.print("Brand name: ");
            String brandName = scanner.next();
            while (brandName.length()>12){
                System.out.print("Wrong input. Try again (max length 12): ");
                brandName = scanner.next();
            }
            if (!brands.contains(brandName)){ //insert new brand name if doesn't exist
                String query = "INSERT INTO Brand (name) VALUES (?)";
                PreparedStatement pstmt = myConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, brandName);
                pstmt.executeUpdate();
                ResultSet myRs = pstmt.getGeneratedKeys();
                if (myRs.next()){
                    brand = myRs.getInt(1);
                }
                brands.add(brandName);
            } else { //retrieve ID if brand name exists
                Statement myStmt = myConn.createStatement();
                ResultSet myRs = myStmt.executeQuery("SELECT brandID FROM Brand WHERE name = '" + brandName + "'");
                if (myRs.next()) {
                    brand = myRs.getInt(1);
                }
            }
        }
        return brand;
    }

    public int setModel(Connection myConn, int brand) throws SQLException {
        HashSet<Integer>brandModels = displayModel(brand);
        System.out.println("Enter 0 to add new option");
        System.out.print("Model (ID number): ");
        int model = scanner.nextInt();
        while(!brandModels.contains(model) && model!=0){
            System.out.println("Invalid value. Please try again: ");
            model = scanner.nextInt();
        }
        if (model==0){
            System.out.print("Model name: ");
            String modelName = scanner.next();
            while (modelName.length()>12){
                System.out.print("Wrong input. Try again (max length 12): ");
                modelName = scanner.next();
            }
            if (!models.contains(modelName)) { //add new model name if it doesnt exist
                String query = "INSERT INTO Model (name,brandID) VALUES (?,?)";
                PreparedStatement pstmt = myConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, modelName);
                pstmt.setInt(2, brand);
                pstmt.executeUpdate();
                ResultSet myRs = pstmt.getGeneratedKeys();
                if (myRs.next()){
                    model = myRs.getInt(1);
                }
                models.add(modelName);
            } else { //retrieve ID of model name if exists
                Statement myStmt = myConn.createStatement();
                ResultSet myRs = myStmt.executeQuery("SELECT modelID FROM Model WHERE name = '" + modelName + "'");
                if (myRs.next()) {
                    model = myRs.getInt(1);
                }
            }
        }
        return model;
    }

    public int setFuel(Connection myConn) throws SQLException {
        int amount = displayFuel();
        System.out.println("Enter 0 to add new option");
        System.out.print("Fuel (ID number): ");
        int fuel = scanner.nextInt();
        while(fuel<0 && fuel>amount){
            System.out.print("Invalid value. Please try again: ");
            fuel = scanner.nextInt();
        }
        if (fuel==0){
            System.out.print("Fuel type: ");
            String fuelType = scanner.next();
            while (fuelType.length()>11){
                System.out.print("Wrong input. Try again (max length 11): ");
                fuelType = scanner.next();
            }
            if (!fuels.contains(fuelType)){ //add new fuel type if it doesnt exist
                String query = "INSERT INTO Fuel (fuel_type) VALUES (?)";
                PreparedStatement pstmt = myConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, fuelType);
                pstmt.executeUpdate();
                ResultSet myRs = pstmt.getGeneratedKeys();
                if (myRs.next()){
                    fuel = myRs.getInt(1);
                }
                fuels.add(fuelType);
            }  else { //retrieve ID if fuel type if exists
                Statement myStmt = myConn.createStatement();
                ResultSet myRs = myStmt.executeQuery("SELECT fuelID FROM Fuel WHERE fuel_type = '" + fuelType + "'");
                if (myRs.next()) {
                    fuel = myRs.getInt(1);
                }
            }
        }
        return fuel;
    }

    public int setType() throws SQLException {
        displayType();
        System.out.print("Type (ID number): ");
        int type = scanner.nextInt();
        while(type<0 || type>3){
            System.out.print("Invalid value. Please try again: ");
            type = scanner.nextInt();
        }
        return type;
    }

    public void addCar() throws SQLException {
        ResultSet myRs = null;
        ArrayList<Object> db = connect();
        Connection myConn = (Connection) db.get(0);
        Statement myStmt = (Statement) db.get(1);
        CarInformation car = new CarInformation();
        car.setRegistration_number(setRegistrationNumber());
        car.setFirst_registration(setFirstRegDate());
        car.setOdometer(setOdometer());
        car.setBrandID(setBrand(myConn));
        car.setModelID(setModel(myConn, car.getBrandID()));
        car.setFuelID(setFuel(myConn));
        car.setTypeID(setType());
        if (confirmation("add")==1){
            String query = "INSERT INTO Car VALUES ('" + car.getRegistration_number() + "', '" + car.getFirst_registration() + "', '"
                    + car.getOdometer() + "', '" + car.getFuelID() + "', '" + car.getModelID() + "', '" + car.getBrandID() + "', '" + car.getTypeID() + "', " + 1 + ")";
            update(query);
            cars.add(car.getRegistration_number());
            database.getCarList().add(car);
            System.out.println("Car has been added.");
        } else {
            System.out.println("Addition cancelled.");
        }
        System.out.println("Returning to the menu.");
        closeConnection(myConn,myStmt, myRs);
    }

    public boolean dateCheck(int year, int month, int day){
        return (day<0 || ( month==2 && year%4==0 && day>29) || (((month==2 && year%4!=0) || (month!=2)) && day>months.get(month-1)));
    }

    public int confirmation(String method){//will change it to fit with other menus later, just for functionality now
        System.out.println("Are you sure you want to " + method);
        System.out.println("1. Yes");
        System.out.println("2. No");
        int output = scanner.nextInt();
        while (output<1 || output>2) {
            System.out.print("Wrong input. Try again: ");
            output=scanner.nextInt();
        }
        return output;
    }

    public int displayFuel() throws SQLException { //return amount of entries if needed
        ArrayList<Object> db = connect();
        Connection myConn = (Connection) db.get(0);
        Statement myStmt = (Statement) db.get(1);
        ResultSet myRs = myStmt.executeQuery("SELECT * FROM Fuel");
        int count=0;
        if (myRs != null) {
            System.out.printf("%-25s %-25s\n", "Fuel ID", "Fuel Type");
            for (int i = 0; i < 210; i++) {
                System.out.print("-");
            }
            System.out.println();
            while (myRs.next()) {
                System.out.printf("%-25s %-25s\n", myRs.getString(1),myRs.getString(2));
                count++;

            }
        }
        closeConnection(myConn,myStmt,myRs);
        return count;
    }

    public int displayBrand() throws SQLException {//return amount of entries if needed
        ArrayList<Object> db = connect();
        Connection myConn = (Connection) db.get(0);
        Statement myStmt = (Statement) db.get(1);
        ResultSet myRs = myStmt.executeQuery("SELECT * FROM Brand");
        int count=0;
        if (myRs != null) {
            System.out.printf("%-25s %-25s\n", "Brand ID", "Brand Name");
            for (int i = 0; i < 210; i++) {
                System.out.print("-");
            }
            System.out.println();
            while (myRs.next()) {
                System.out.printf("%-25s %-25s\n", myRs.getString(1),myRs.getString(2));
                count++;
            }
        }
        closeConnection(myConn,myStmt,myRs);
        return count;
    }

    // if brandID -1, display all models
    public HashSet<Integer> displayModel(int brandID) throws SQLException { //return set of ids, used when choosing model that fits with the brand
        ArrayList<Object> db = connect();
        Connection myConn = (Connection) db.get(0);
        Statement myStmt = (Statement) db.get(1);
        ResultSet myRs;
        HashSet<Integer> models = new HashSet<>();
        if (brandID==-1) {
            myRs = myStmt.executeQuery("SELECT m.modelID, CONCAT(b.name, ' ', m.name) FROM Model m JOIN Brand b ON m.brandID=b.brandID");
        } else {
            myRs = myStmt.executeQuery("SELECT m.modelID, CONCAT(b.name, ' ', m.name) FROM Model m JOIN Brand b ON m.brandID=b.brandID WHERE m.brandID =" + brandID);
        }

        if (myRs != null) {
            System.out.printf("%-25s %-25s\n", "Model ID", "Model Name");
            for (int i = 0; i < 210; i++) {
                System.out.print("-");
            }
            System.out.println();
            while (myRs.next()) {
                System.out.printf("%-25s %-25s\n", myRs.getString(1),myRs.getString(2));
                models.add(myRs.getInt(1));
            }
        }
        closeConnection(myConn,myStmt,myRs);
        return models;
    }

    public void displayType() throws SQLException {
        ArrayList<Object> db = connect();
        Connection myConn = (Connection) db.get(0);
        Statement myStmt = (Statement) db.get(1);
        ResultSet myRs = myStmt.executeQuery("SELECT * FROM rental_types");
        if (myRs != null) {
            System.out.printf("%-25s %-25s %-25s\n", "Type ID", "Type Name", "Description");
            for (int i = 0; i < 210; i++) {
                System.out.print("-");
            }
            System.out.println();
            while (myRs.next()) {
                System.out.printf("%-25s %-25s %-25s\n", myRs.getString(1),myRs.getString(2),myRs.getString(3));

            }
        }
        closeConnection(myConn,myStmt,myRs);
    }

    public boolean inRNor0(String registration_number){
        return cars.contains(registration_number) || registration_number.equals("0");
    }

    public void deleteCar() throws SQLException, ParseException {
        String end = "2500-01-31";
        String start = Calendar.getInstance().get(YEAR) + "-" + Calendar.getInstance().get(MONTH) + "-" + Calendar.getInstance().get(DAY_OF_MONTH) ;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date endDate = dateFormat.parse(end);
        Date startDate = dateFormat.parse(start);
        HashSet <String> carsWoContract = displayAvailableCarsWithinDateRange(startDate,endDate); //cars without contract, from today till year 2500
        System.out.print("Insert registration number of a car you want to delete or 0 to go back: ");
        String registration_number = scanner.next();
        while (!(carsWoContract.contains(registration_number) || registration_number.equals("0"))){
            System.out.print("Wrong input. Enter 0 to go back or try again: ");
            registration_number = scanner.next();
        }
        if(carsWoContract.contains(registration_number) && confirmation("delete this car?")==1){
            update("DELETE FROM Car WHERE registration_number = '" + registration_number + "'");
            System.out.println("Car has been deleted from the database");
            for (int i=0;i< database.getCarList().size();i++) {
                if (database.getCarList().get(i).getRegistration_number().equals(registration_number)){
                    database.getCarList().remove(i);
                }
            }
            cars.remove(registration_number);
        } else {
            System.out.println("Deletion cancelled.");
        }
        System.out.println("Returning to the menu.");
    }

    public HashSet<String> displayCars(String condition) throws SQLException {
        HashSet<String> carRegNo = new HashSet<>();
        ArrayList<Object> db = connect();
        Connection myConn = (Connection) db.get(0);
        Statement myStmt = (Statement) db.get(1);
        String query = "SELECT c.registration_number, c.first_registration, c.odometer, f.fuel_type, CONCAT(b.name, ' '," +
                " m.name), r.name, r.description " +
                "FROM Car c JOIN Brand b ON c.brandID = b.brandID JOIN Model m ON c.modelID = m.modelID JOIN Fuel f " +
                "ON c.fuelID = f.fuelID JOIN rental_types r ON " +
                "c.rental_typeID = r.rental_typeID";
        if (condition!=null){
            query = query + " " + condition; //adds where clause for conditions
        }
        ResultSet myRs = myStmt.executeQuery(query);
        if (myRs != null) {
            System.out.printf("%-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", "Registration Number", "First Registration", "Odometer (km)",
                    "Fuel Type", "Model", "Rental Type", "Description");
            for (int i = 0; i < 210; i++) {
                System.out.print("-");
            }
            System.out.println();
            while (myRs.next()) {
                System.out.printf("%-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", myRs.getString(1),myRs.getString(2),
                        myRs.getString(3),myRs.getString(4),myRs.getString(5),myRs.getString(6),
                        myRs.getString(7));
                carRegNo.add(myRs.getString(1));
            }
        }
        closeConnection(myConn,myStmt,myRs);
        return carRegNo;
    }

    public HashSet<String> displayAvailableCarsWithinDateRange(Date startDate, Date endDate) throws SQLException {
        if (startDate.compareTo(endDate) > 0){ //reverse the order if start>end
            Date tempDate = startDate;
            startDate = endDate;
            endDate = tempDate;
        }

        String sDate = startDate.toString();
        String eDate = endDate.toString();

        HashSet<String> availableCars =new HashSet<>();
        HashSet<String> unavailableCars =new HashSet<>();
        ArrayList<Object> db = connect();
        Connection myConn = (Connection) db.get(0);
        Statement myStmt = (Statement) db.get(1);
        String query = "SELECT DISTINCT c.registration_number " + //selects all available cars within the date range
                // however as more contracts exist for 1 car, it might be added to the lsit even if it's not
                // available during that exact date
                "FROM Car c LEFT JOIN  contract co ON c.registration_number = co.car_registration_number " +
                "WHERE (NOT('" + eDate + "'>=co.start_time AND '" + sDate + "'<=co.end_time) || c.registration_number " +
                "NOT IN (SELECT car_registration_number FROM contract)) AND c.is_available = 1";
        ResultSet myRs = myStmt.executeQuery(query);
        while (myRs.next()) { availableCars.add(myRs.getString(1));}
        //System.out.println(availableCars.toString());
        String query2 = "SELECT co.car_registration_number " + //selects cars that are't available during selected date
                "FROM Car c JOIN  contract co ON c.registration_number = co.car_registration_number " +
                "WHERE ('" + eDate + "'>=co.start_time AND '" + sDate + "'<=co.end_time)";
        Statement myStmt2 = (Statement) db.get(1);
        ResultSet myRs2 = myStmt2.executeQuery(query2);
        //System.out.println(myRs2.getFetchSize());
        while (myRs2.next()) { unavailableCars.add(myRs2.getString(1));}
        //System.out.println(unavailableCars.toString());
        String carReg = "";
        availableCars.removeAll(unavailableCars); //substracts unavailable from available
        //System.out.println(availableCars.toString());
        for (String regNumber: availableCars) {
            carReg = carReg + "\"" + regNumber + " \",";
            //availableCars.add(myRs.getString(1));
        }
        if(carReg.length()!=0){
            carReg =carReg.substring(0,carReg.length()-1);
        }
        //System.out.println(carReg);
        displayCars("WHERE c.registration_number IN (" + carReg + ")");
        closeConnection(myConn,myStmt,myRs);
        return availableCars;
    }

    public void displayUnavailableCars() throws SQLException{
        displayCars("WHERE is_available = 0");
    }

    public void makeUnavailable() throws SQLException {
        HashSet<String> carRegNo = displayCars("WHERE is_available = 1");
        System.out.print("Insert registration number of a car you want to set as unavailable or 0 to go back: ");
        String registration_number = scanner.next();
        while (!inRNor0(registration_number)){
            System.out.println("Wrong input. Enter 0 to go back or try again: ");
            registration_number = scanner.next();
            System.out.println(registration_number);
        }
        if(cars.contains(registration_number) && confirmation("make this car unavailable?")==1){
            update("UPDATE Car SET is_available=0 WHERE registration_number = '" + registration_number + "'");
            System.out.println("Car has been made unavailable");
            for (int i=0;i< database.getCarList().size();i++) {
                if (database.getCarList().get(i).getRegistration_number().equals(registration_number)){
                    database.getCarList().get(i).setAvailable(false);
                }
            }
        } else {
            System.out.println("Operation Cancelled.");
        }
        System.out.println("Returning to the menu.");
    }

    public void makeAvailable() throws SQLException {
        HashSet<String> carRegNo = displayCars("WHERE is_available = 0");
        System.out.print("Insert registration number of a car you want to set as available or 0 to go back:: ");
        String registration_number = scanner.next();
        while (!inRNor0(registration_number)){
            System.out.print("Wrong input. Enter 0 to go back or try again: ");
            registration_number = scanner.next();
        }
        if(cars.contains(registration_number) && confirmation("make this car available?")==1){
            update("UPDATE Car SET is_available=1 WHERE registration_number = '" + registration_number + "'");
            System.out.println("Car has been made available");
            for (int i=0;i< database.getCarList().size();i++) {
                if (database.getCarList().get(i).getRegistration_number().equals(registration_number)){
                    database.getCarList().get(i).setAvailable(true);
                }
            }
        } else {
            System.out.println("Operation Cancelled.");
        }
        System.out.println("Returning to the menu.");
    }

    public void makeUnavailable(String registration_number) throws SQLException {
        if(confirmation("make this car unavailable?")==1){
            update("UPDATE Car SET is_available = 0 WHERE registration_number = '" + registration_number + "'");
            for (int i=0; i<database.getCarList().size(); i++) {
                if (database.getCarList().get(i).getRegistration_number().equals(registration_number)){
                    database.getCarList().get(i).setAvailable(false);
                }
            }
        }
    }

    //ID 1 - Luxury
    // ID 2 - Family
    // ID 3 - Sport

    public void displayLuxuryCars() throws SQLException {
        displayCars("WHERE c.rental_typeID = 1");
    }

    public void displayFamilyCars() throws SQLException {
        displayCars("WHERE c.rental_typeID = 2");
    }

    public void displaySportCars() throws SQLException {
        displayCars("WHERE c.rental_typeID = 3");
    }

    public void searchByRegistrationNumber(String registrationNumber) throws SQLException {
        displayCars("WHERE registration_number = " + registrationNumber);
    }

    public void editCar() throws SQLException, ParseException {
        displayCars(null);
        System.out.print("Enter Registration Number of a car you want to edit or 0 to go back: ");
        String registration_number = scanner.next();
        while (!inRNor0(registration_number)) {
            System.out.print("Wrong input. Enter 0 to go back or try again: ");
            registration_number = scanner.next();
        }
        String new_registration_number = "";
        if (!registration_number.equals("0")) {
            System.out.println("Insert new value for Registration Number: ");
            new_registration_number = setRegistrationNumber();

            //update("UPDATE Contract SET car_registration_number = '" + new_registration_number + "' WHERE car_registration_number = '" + registration_number + "'");
            update("UPDATE Car SET registration_number = '" + new_registration_number + "' WHERE registration_number = '" + registration_number + "'");
            //change registration number in contracts
            ContractMethods contractMethods = new ContractMethods();
            for (int i = 0; i < contractMethods.getContracts().size(); i++) {
                if (contractMethods.getContracts().get(i).getRegistrationNumber().equals(registration_number)) {
                    contractMethods.getContracts().get(i).setRegistrationNumber(new_registration_number);
                }
            }
            for (CarInformation cari : database.getCarList()) {
                if (cari.getRegistration_number().equals(registration_number)) {
                    cari.setRegistration_number(new_registration_number);
                }
            }
            cars.remove(registration_number);
            cars.add(new_registration_number);
        }
    }

    public ArrayList<Object> connect() {
        try {
            Connection myConn = getConnection("jdbc:mysql://localhost:3306/kailua?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "dimk", "dimk1234!");
            Statement myStmt = myConn.createStatement();
            return new ArrayList<>(Arrays.asList(myConn,myStmt));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(String query) {
        try {
            Connection myConn = getConnection("jdbc:mysql://localhost:3306/kailua?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "dimk", "dimk1234!");
            Statement myStmt = myConn.createStatement();
            myStmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(Connection myConn, Statement myStmt, ResultSet myRs) throws SQLException {
        if (myRs!=null) {
            myRs.close();
        }
        if (myStmt!=null) {
            myStmt.close();
        }
        if (myConn!=null) {
            myConn.close();
        }
    }
}