import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static java.sql.DriverManager.getConnection;

public class CarMethods {

    private static Set<String> cars= new HashSet<>();
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Integer>months = new ArrayList<>(Arrays.asList(31,28,31,30,31,30,31,31,30,31,30,31));

    public void addCar() throws SQLException {
        ResultSet myRs = getRs("SELECT registration_number FROM Car");
        while(myRs.next()) {
            cars.add(myRs.getString("registration_number"));
        }
        System.out.println("Registration number: ");
        String registration_number = scanner.next();
        while(cars.contains(registration_number) && !registration_number.matches("[a-zA-Z0-9]{10}")){
            System.out.println("Invalid registration number. Please try again: ");
            registration_number = scanner.next();
        }
        System.out.println("First registration date (YYYY-MM-DD): ");
        System.out.println("Year: ");
        int year = scanner.nextInt();
        //first car created in 1885 (i think) so it can't be less than 1885
        // can't be higher than current year
        while(year<1885 && year>Calendar.getInstance().get(Calendar.YEAR)){
            System.out.println("Invalid year value. Please insert value between 1885 -" + Calendar.getInstance().get(Calendar.YEAR) + ": ");
            year = scanner.nextInt();
        }
        System.out.println("Month: ");
        int month = scanner.nextInt();
        while(month<0 && year>12){
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
            System.out.println("Invalid month value. Please insert value between 1 - " + months.get(month-1)+leap + ": ");
            day = scanner.nextInt();
        }
        System.out.println("Odometer (amount of km): ");
        int odometer = scanner.nextInt();
        while(odometer<0){
            System.out.println("Invalid value. Please try again: ");
            odometer = scanner.nextInt();
        }
        int amount;
        amount = displayBrand();
        System.out.println("Brand (ID number): ");
        int brand = scanner.nextInt();
        while(brand<0 && brand>amount){
            System.out.println("Invalid value. Please try again: ");
            brand = scanner.nextInt();
        }
        HashSet<Integer>models = displayModel(brand);
        System.out.println("Model (ID number): ");
        int model = scanner.nextInt();
        while(!models.contains(model)){
            System.out.println("Invalid value. Please try again: ");
            model = scanner.nextInt();
        }
        amount = displayFuel();
        System.out.println("Fuel (ID number): ");
        int fuel = scanner.nextInt();
        while(fuel<0 && fuel>amount){
            System.out.println("Invalid value. Please try again: ");
            fuel = scanner.nextInt();
        }
        displayType();
        System.out.println("Type (ID number): ");
        int type = scanner.nextInt();
        while(type<0 && type>3){
            System.out.println("Invalid value. Please try again: ");
            type = scanner.nextInt();
        }
        if (confirmation("add")==1){
            //are we doing the available part?
            String query = "INSERT INTO Car VALUES ('" + registration_number + "', '" + year + "-" + month + "-" + day + "', '" + odometer + "', '" + fuel + "', '" + model + "', '" + brand + "', '" + type + "', '" + true + ")";
            update(query);
        }
    }

    public boolean dateCheck(int year, int month, int day){
        return (day<0 && ((month!=2 || (month==2 && year%4==0) && day>months.get(month-1)) || (month==2 && year%4!=2 && day>months.get(month-1)+1)));
    }

    public int confirmation(String method){//will change it to fit with other menus later, just for functionality now
        System.out.println("Are you sure you want to " + method + "this car?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int output = scanner.nextInt();
        while (output<1 && output>2) {
            System.out.println("Wrong input. Try again: ");
            output=scanner.nextInt();
        }
        return output;
    }

    public int displayFuel() throws SQLException { //return amount of entries if needed
        ResultSet myRs = getRs("SELECT * FROM Fuel");
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
        return count;
    }

    public int displayBrand() throws SQLException {//return amount of entries if needed
        ResultSet myRs = getRs("SELECT * FROM Brand");
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
        return count;
    }

    // if brandID -1, display all models
    public HashSet<Integer> displayModel(int brandID) throws SQLException { //return set of ids, used when choosing model that fits with the brand
        ResultSet myRs = null;
        HashSet<Integer> models = new HashSet<>();
        if (brandID!=-1) {
            myRs = getRs("SELECT m.modelID, CONCAT(m.name,\" \" ,b.name) FROM Model m JOIN Brand b ON m.brandID=b.brandID");
        } else {
            myRs = getRs("SELECT modelID, CONCAT(name,\" \" ,b.name) FROM Model JOIN Brand b ON brandID=" + brandID);
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
        return models;
    }

    public void displayType() throws SQLException {
        ResultSet myRs = getRs("SELECT * FROM rental_types");
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
    }

    public void remove() {
    }

    public void displayCars() throws SQLException {
        ResultSet myRs = getRs("SELECT c.registration_number, c.first_registration, c.odometer, f.fuel_type, CONCAT(b.name,\" \",m.name), r.name, r.description " +
                "FROM Car c JOIN Brand b ON c.brandID = b.brandID JOIN Model m ON c.modelID = m.modelID JOIN Fuel f ON c.fuelID = f.fuelID JOIN rental_types r ON" +
                "c.rental_typeID = r.rental_typeID");
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

            }
        }
    }

    public void displayAvailableCarsWithinDateRange() {
    }

    public void displayUnavailableCars() {
    }

    public void displayFamilyCars() {
    }

    public void displaySportCars() {

    }

    public void displayLuxuryCars() {

    }

    public void searchByRegistrationNumber(String registrationNumber) {
    }

    public ResultSet getRs(String query) {
        try {
            Connection myConn = getConnection("jdbc:mysql://localhost:3306/kailua", "dimk", "dimk1234!");
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery(query);
            myStmt.close();
            myConn.close();
            return myRs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(String query) {
        try {
            Connection myConn = getConnection("jdbc:mysql://localhost:3306/kailua", "dimk", "dimk1234!");
            Statement myStmt = myConn.createStatement();
            myStmt.executeUpdate(query);
            myStmt.close();
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

