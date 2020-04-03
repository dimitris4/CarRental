import java.sql.*;
import java.util.*;

import static java.sql.DriverManager.getConnection;

public class CarMethods {

    private static Set<String> cars= new HashSet<>();
    private static Set<String> brands= new HashSet<>();
    private static Set<String> models= new HashSet<>();
    private static Set<String> fuels= new HashSet<>();
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Integer>months = new ArrayList<>(Arrays.asList(31,28,31,30,31,30,31,31,30,31,30,31));
    private static ArrayList<CarInformation> carList = new ArrayList<>();

    public CarMethods() throws SQLException {//populate hashsets
        ArrayList<Object> db = connect();
        Connection myConn = (Connection) db.get(0);
        Statement myStmt = (Statement) db.get(1);
        ResultSet myRs = myStmt.executeQuery("SELECT registration_number FROM Car");
        while(myRs.next()) {
            cars.add(myRs.getString("registration_number"));
        }
        myRs = myStmt.executeQuery("SELECT name FROM Brand");
        while(myRs.next()) {
            brands.add(myRs.getString("name"));
        }
        myRs = myStmt.executeQuery("SELECT name FROM Model");
        while(myRs.next()) {
            models.add(myRs.getString("name"));
        }
        myRs = myStmt.executeQuery("SELECT fuel_type FROM Fuel");
        while(myRs.next()) {
            fuels.add(myRs.getString("fuel_type"));
        }
        myRs = myStmt.executeQuery("SELECT c.registration_number, c.first_registration, c.odometer, f.fuel_type, m.name, b.name, rt.name, rt.description, c.is_available " +
                ", c.fuelID, c.modelID, c.brandID, c.rental_typeID FROM Car c JOIN " +
                "Brand b ON c.brandID = b.brandID JOIN Model m ON c.modelID = m.modelID JOIN FUEL f ON c.fuelID = f.fuelID " +
                "JOIN rental_types rt ON c.rental_typeID = rt.rental_typeID");
        while(myRs.next()) {
            carList.add(new CarInformation (myRs.getString(1),myRs.getString(2),myRs.getInt(3),myRs.getString(4),myRs.getString(5),
                    myRs.getString(6),myRs.getString(7),myRs.getString(8),myRs.getInt(9),myRs.getInt(10),myRs.getInt(11),
                    myRs.getInt(12),myRs.getInt(13)));
        }
        closeConnection(myConn,myStmt,myRs);
    }

    public String setRegistrationNumber(){
        System.out.println("Registration number: ");
        String registration_number = scanner.next();
        while(cars.contains(registration_number) && !registration_number.matches("[a-zA-Z0-9]{10}")){
            System.out.println("Invalid registration number. Please try again: ");
            registration_number = scanner.next();
        }
        return registration_number;
    }

    public String setFirstRegDate(){
        System.out.println("First registration date: ");
        System.out.println("Year: ");
        int year = scanner.nextInt();
        //first car created in 1885 (i think) so it can't be less than 1885
        // can't be higher than current year
        while(year<1885 || year>Calendar.getInstance().get(Calendar.YEAR)){
            System.out.println("Invalid year value. Please insert value between 1885 -" + Calendar.getInstance().get(Calendar.YEAR) + ": ");
            year = scanner.nextInt();
        }
        System.out.println("Month: ");
        int month = scanner.nextInt();
        while(month<0 || month>12){
            System.out.println("Invalid month value. Please insert value between 1 - 12: ");
            month = scanner.nextInt();
        }
        System.out.println("Day: ");
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
        System.out.println("Odometer (amount of km): ");
        int odometer = scanner.nextInt();
        while(odometer<0){
            System.out.println("Invalid value. Please try again: ");
            odometer = scanner.nextInt();
        }
        return odometer;
    }

    public int setBrand(Connection myConn) throws SQLException {
        int amount;
        amount = displayBrand();
        System.out.println("Enter 0 to add new option");
        System.out.println("Brand (ID number): ");
        int brand = scanner.nextInt();
        while(brand<0 && brand>amount){
            System.out.println("Invalid value. Please try again: ");
            brand = scanner.nextInt();
        }
        if (brand==0){
            System.out.println("Brand name: ");
            String brandName = scanner.next();
            while (brandName.length()>12){
                System.out.println("Wrong input. Try again (max length 12): ");
                brandName = scanner.next();
            }
            if (!brands.contains(brandName)){
                String query = "INSERT INTO Brand (name) VALUES (?)";
                PreparedStatement pstmt = myConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, brandName);
                pstmt.executeUpdate();
                ResultSet myRs = pstmt.getGeneratedKeys();
                if (myRs.next()){
                    brand = myRs.getInt(1);
                }
                brands.add(brandName);
            }
        }
        return brand;
    }

    public int setModel(Connection myConn, int brand) throws SQLException {
        HashSet<Integer>brandModels = displayModel(brand);
        System.out.println("Enter 0 to add new option");
        System.out.println("Model (ID number): ");
        int model = scanner.nextInt();
        while(!brandModels.contains(model) && model!=0){
            System.out.println("Invalid value. Please try again: ");
            model = scanner.nextInt();
        }
        if (model==0){
            System.out.println("Model name: ");
            String modelName = scanner.next();
            while (modelName.length()>12){
                System.out.println("Wrong input. Try again (max length 12): ");
                modelName = scanner.next();
            }
            if (!models.contains(modelName)) {
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
            }
        }
        return model;
    }

    public int setFuel(Connection myConn) throws SQLException {
        int amount = displayFuel();
        System.out.println("Enter 0 to add new option");
        System.out.println("Fuel (ID number): ");
        int fuel = scanner.nextInt();
        while(fuel<0 && fuel>amount){
            System.out.println("Invalid value. Please try again: ");
            fuel = scanner.nextInt();
        }
        if (fuel==0){
            System.out.println("Fuel type: ");
            String fuelType = scanner.next();
            while (fuelType.length()>11){
                System.out.println("Wrong input. Try again (max length 11): ");
                fuelType = scanner.next();
            }
            if (!brands.contains(fuelType)){
                String query = "INSERT INTO Fuel (fuel_type) VALUES (?)";
                PreparedStatement pstmt = myConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, fuelType);
                pstmt.executeUpdate();
                ResultSet myRs = pstmt.getGeneratedKeys();
                if (myRs.next()){
                    fuel = myRs.getInt(1);
                }
                fuels.add(fuelType);
            }
        }
        return fuel;
    }

    public int setType() throws SQLException {
        displayType();
        System.out.println("Type (ID number): ");
        int type = scanner.nextInt();
        while(type<0 || type>3){
            System.out.println("Invalid value. Please try again: ");
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
            carList.add(car);
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
        System.out.println("Are you sure you want to " + method + " this car?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int output = scanner.nextInt();
        while (output<1 || output>2) {
            System.out.println("Wrong input. Try again: ");
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
            myRs = myStmt.executeQuery("SELECT m.modelID, CONCAT(b.name,\" \" ,m.name) FROM Model m JOIN Brand b ON m.brandID=b.brandID");
        } else {
            myRs = myStmt.executeQuery("SELECT m.modelID, CONCAT(b.name,\" \" ,m.name) FROM Model m JOIN Brand b ON m.brandID=b.brandID WHERE m.brandID =" + brandID);
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

    public void deleteCar() throws SQLException{
        displayCars(null);
        System.out.println("Insert registration number of a car you want to delete: ");
        String registration_number = scanner.next();
        while (!cars.contains(registration_number) || registration_number.equals("0")){
            System.out.println("Wrong input. Enter 0 to go back or try again: ");
            registration_number = scanner.next();
        }
        if(confirmation("delete")==1){
            update("DELETE FROM Car WHERE registration_number = '" + registration_number + "'");
            System.out.println("Car has been deleted from the database");
            for (int i=0;i< carList.size();i++) {
                if (carList.get(i).getRegistration_number().equals(registration_number)){
                    carList.remove(i);
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
        String query = "SELECT c.registration_number, c.first_registration, c.odometer, f.fuel_type, CONCAT(b.name,\" \",m.name), r.name, r.description " +
                "FROM Car c JOIN Brand b ON c.brandID = b.brandID JOIN Model m ON c.modelID = m.modelID JOIN Fuel f ON c.fuelID = f.fuelID JOIN rental_types r ON " +
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

    public void displayAvailableCarsWithinDateRange() {
    }

    public void displayUnavailableCars() throws SQLException{
        displayCars("WHERE isAvailable = 0");
    }

    public void makeUnavailable() throws SQLException {
        HashSet<String> carRegNo = displayCars("WHERE isAvailable = 1");
        System.out.println("Insert registration number of a car you want to set as unavailable: ");
        String registration_number = scanner.next();
        while (!carRegNo.contains(registration_number) || registration_number.equals("0")){
            System.out.println("Wrong input. Enter 0 to go back or try again: ");
            registration_number = scanner.next();
        }
        if(confirmation("make unavailable")==1){
            update("UPDATE Car SET isAvailable=0 WHERE car_registration_number = '" + registration_number + "'");
            System.out.println("Car has been made unavailable");
            for (int i=0;i< carList.size();i++) {
                if (carList.get(i).getRegistration_number().equals(registration_number)){
                    carList.get(i).setAvailable(false);
                }
            }
        } else {
            System.out.println("Operation Cancelled.");
        }
        System.out.println("Returning to the menu.");

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

    public void editCar() throws SQLException {
        displayCars(null);
        System.out.println("Enter Registration Number of a car you want to edit or 0 to go back: ");
        String registration_number = scanner.next();
        while (!cars.contains(registration_number) || registration_number.equals("0")) {
            System.out.println("Wrong input. Enter 0 to go back or try again: ");
            registration_number = scanner.next();
        }
        String new_registration_number = "";
        if (!registration_number.equals("0")) {
            System.out.println("Insert new value for Registration Number: ");
            new_registration_number = setRegistrationNumber();
        }
        update("UPDATE Car SET registration_number = '" + new_registration_number + "' WHERE registration_number = '" + registration_number + "'");
        update("UPDATE Contract SET car_registration_number = '" + new_registration_number + "' WHERE car_registration_number = '" + registration_number + "'");
        for (CarInformation cari: carList) {
            if (cari.getRegistration_number().equals(registration_number)){
                cari.setRegistration_number(new_registration_number);
            }
        }
        cars.remove(registration_number);
        cars.add(new_registration_number);
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

