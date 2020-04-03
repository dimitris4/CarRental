import java.sql.*;
//import java.sql.Date;
import java.util.*;
import java.util.Date;

//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public class RenterMethods {

    String url = "jdbc:mysql://localhost:3306/kailua";
    String user = "dimk";
    String password = "dimk1234!";
    Scanner input = new Scanner(System.in);
    private static Set<Integer> zipIDs= new HashSet<>();
    private static Set<String> zips= new HashSet<>();
    private static Set<String> countries= new HashSet<>();


    public RenterMethods() {//populate hashsets

        try {

            Connection myConn = getConnection(url, user, password);
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("SELECT zipID FROM zip");;
            while(myRs.next()) {
                zipIDs.add(myRs.getInt("zipID"));
            }
            myRs = myStmt.executeQuery("SELECT zip FROM zip");;
            while(myRs.next()) {
                zips.add(myRs.getString("zip"));
            }
            myRs = myStmt.executeQuery("SELECT name FROM country");
            while(myRs.next()) {
                countries.add(myRs.getString("name"));
            }
            myConn.close();
            myRs.close();
            myStmt.close();


        } catch (SQLException e) {

            e.printStackTrace();

        }
    }


    public void displayRenters() {

        try {

            Connection myConn = getConnection(url, user, password);

            Statement myStmt = myConn.createStatement();

            String sql = "SELECT renterID, first_name, last_name, mobile_phone_number, home_phone_number, email,\n" +
                    "\t   driver_license_number, since_data, CONCAT(street, ' ', building, ' ', floor, ' ', \n" +
                    "       door, ' ', zip, ' ', city, ' ', country.name)\n" +
                    "FROM renter \n" +
                    "\tJOIN address USING (addressID)\n" +
                    "    JOIN zip USING (zipID)\n" +
                    "    JOIN country USING (countryID)\n" +
                    "    JOIN phone_numbers USING (renterID);";

            ResultSet rs = myStmt.executeQuery(sql);

            System.out.printf("%-15s %-25s %-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", "Renter ID", "First Name", "Last Name",
                    "Mobile Phone", "Home Phone", "Email", "Driver License", "Since", "Address");

            for (int i = 0; i < 215; i++) {

                System.out.print("-");

            }

            System.out.println();

            while (rs.next()) {

                System.out.printf("%-15s %-25s %-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7),
                        rs.getString(8), rs.getString(9));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public void displayZip() {

        try {

            Connection myConn = getConnection(url, user, password);

            Statement myStmt = myConn.createStatement();

            String sql = "SELECT zipID, zip, city, country.name\n" +
                    "FROM zip\n" +
                    "JOIN country USING (countryID)\n" +
                    "ORDER BY zip;";

            ResultSet rs = myStmt.executeQuery(sql);

            System.out.printf("%-15s %-15s %-25s %-25s\n", "Zip ID", "Zip Code", "City", "Country");

            for (int i = 0; i < 65; i++) {

                System.out.print("-");

            }

            System.out.println();

            while (rs.next()) {

                System.out.printf("%-15s %-15s %-25s %-25s\n", rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getString(4));

            }

            System.out.println();


        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public void add() {

        try {
            //get a connection to database
            Connection myConn = getConnection(url, user, password);

            //Statement myStmt = myConn.createStatement();

            //prompt for the user input
            Scanner console = new Scanner(System.in);
            System.out.print("First name: ");
            String fname = console.next();
            while (!fname.matches("[a-zA-Z_]+")) {
                System.out.print("Invalid First Name. Try Again: ");
                fname = console.next();
            }
            System.out.print("Last name: ");
            String lname = console.next();
            while(!lname.matches("[a-zA-Z_]+")){
                System.out.println("Invalid Last Name. Try Again: ");
                lname = console.next();
            }
            System.out.print("Mobile Phone (start with country code): ");
            String mobilePhone = console.next();
            while(!mobilePhone.matches("[0-9]+")){
                System.out.print("Invalid Phone number. Try Again: ");
                mobilePhone = console.next();
            }
            console.nextLine();
            System.out.print("Home Phone (start with country code) or [0] to skip: ");
            String homePhone = console.nextLine();
            while(!homePhone.matches("[0-9]+")){
                System.out.print("Invalid Phone number. Try Again: ");
                homePhone = console.nextLine();
            }
            if (homePhone.equals("0")){ homePhone = null; }
            System.out.print("E-mail: ");
            String email = console.nextLine();

            System.out.print("Driver Licence Number: ");
            String licence = console.nextLine();

            System.out.print("Driver since (please type the date) dd-mm-yyyy: ");
            Date sinceDate = Input.insertDateWithoutTime();

            System.out.println("\n**** CLIENT ADDRESS ****\n");
            System.out.print("Street Name: ");
            String street = console.next();
            while (!street.matches("[a-zA-Z_]+")) {
                System.out.print("Invalid Street Name. Try Again: ");
                street = console.next();
            }

            System.out.print("Street Number: ");
            int building = Input.checkInt(1,5000);

            System.out.print("Floor: ");
            int floor = Input.checkInt(0,200);

            System.out.print("Door (th/tv/mf/-): ");
            String door = console.next();
            while(!door.matches("(^(th)?|(tv)?|(mf)?|(-)?(\\s)?$)")){
                System.out.println("Invalid Input. Try Again: ");
                door = console.next();
            }

            displayZip();
            System.out.println("\n[1] Select ZIP from the list     [2] Insert new one");
            System.out.print("Select the option: ");
            int field = input.nextInt();
            String zip_code = "";
            int zipID;

            switch (field) {

                case 1:
                    System.out.print("Type selected zip code ID: ");
                    zipID = console.nextInt();
                    while (!(zipIDs.contains(zipID))) {
                        System.out.print("Invalid Input. Try Again: ");
                        zipID = console.nextInt();
                    }
                    String query = "INSERT INTO address (street, building, floor, door, zipID) " +
                            "VALUES (?, ?, ?, ?, ?)";

                    //create insert statements

                    PreparedStatement preparedStmt;
                    preparedStmt = myConn.prepareStatement(query);
                    preparedStmt.setString (1, street);
                    preparedStmt.setInt (2, building);
                    preparedStmt.setInt (3, floor);
                    preparedStmt.setString (4, door);
                    preparedStmt.setInt (5, zipID);
                    preparedStmt.execute();
                    break;
                case 2:
                    System.out.print("Type new zip code: ");
                    zip_code = console.next();
                    while (!zip_code.matches("[0-9]+")) {
                        System.out.print("Invalid Input. Try Again: ");
                        zip_code = console.next();
                    }
                    console.nextLine();
                    System.out.print("City: ");
                    String city = console.nextLine();
                    while(!city.matches("[a-zA-Z_]+(\\s)?([a-zA-Z_]+)?")){
                        System.out.println("Invalid City. Try Again: ");
                        city = console.nextLine();
                    }

                    System.out.print("Country: ");
                    String country = console.next();
                    while(!country.matches("[a-zA-Z_]+(\\s)?([a-zA-Z_]+)?")){
                        System.out.println("Invalid Country. Try Again: ");
                        country = console.next();
                    }
                    String queryCountry = "INSERT INTO country (name) " +
                            "VALUES (?)";

                    String queryZip = "INSERT INTO zip (zip, city, countryID) " +
                            "VALUES (?, ?, LAST_INSERT_ID())";

                    String queryAddress = "INSERT INTO address (street, building, floor, door, zipID) " +
                            "VALUES (?, ?, ?, ?, LAST_INSERT_ID())";

                    //create insert statements

                    preparedStmt = myConn.prepareStatement(queryCountry);
                    preparedStmt.setString(1, country);
                    preparedStmt.execute();

                    preparedStmt = myConn.prepareStatement(queryZip);
                    preparedStmt.setString(1, zip_code);
                    preparedStmt.setString(2, city);
                    preparedStmt.execute();

                    preparedStmt = myConn.prepareStatement(queryAddress);
                    preparedStmt.setString (1, street);
                    preparedStmt.setInt (2, building);
                    preparedStmt.setInt (3, floor);
                    preparedStmt.setString (4, door);
                    preparedStmt.execute();

                    break;
                default:
                    break;

            }

            //create insert statements

            PreparedStatement preparedStmt;

            String queryRenter = "INSERT INTO renter (first_name, last_name, email, driver_license_number, since_data, " +
                                 "addressID)" + " VALUES (?, ?, ?, ?, ?, LAST_INSERT_ID())";

            String queryPhoneNumbers = "INSERT INTO phone_numbers (renterID, mobile_phone_number, home_phone_number) " +
                    "VALUES (LAST_INSERT_ID(), ?, ?)";


            preparedStmt = myConn.prepareStatement(queryRenter);
            preparedStmt.setString (1, fname);
            preparedStmt.setString (2, lname);
            preparedStmt.setString (3, email);
            preparedStmt.setString (4, licence);
            java.sql.Date sDate = new java.sql.Date(sinceDate.getTime());
            preparedStmt.setDate (5, sDate);
            preparedStmt.execute();

            preparedStmt = myConn.prepareStatement(queryPhoneNumbers);
            preparedStmt.setString (1, mobilePhone);
            preparedStmt.setString(2, homePhone);
            preparedStmt.execute();

            //close connection
            myConn.close();

        } catch (SQLException exc) {
            exc.printStackTrace();
        }

    }

    public void remove() {

        try {

            Connection myConn = getConnection(url, user, password);

            if (!displayRentersRemove(myConn)) {

                return;

            } else {

                PreparedStatement pst1 = null;
                PreparedStatement pst2 = null;
                PreparedStatement pst3 = null;

                String sql1 = "DELETE FROM contract WHERE renterID = ?";
                String sql2 = "DELETE FROM phone_numbers WHERE renterID = ?";
                String sql3 = "DELETE FROM renter WHERE renterID = ?";

                System.out.print("Select renter id: ");

                int choice = input.nextInt();

                try {

                    pst1 = myConn.prepareStatement(sql1);

                    pst2 = myConn.prepareStatement(sql2);

                    pst3 = myConn.prepareStatement(sql3);

                    pst1.setInt(1, choice);

                    pst2.setInt(1, choice);

                    pst3.setInt(1, choice);

                    int rowsAffected1 = pst1.executeUpdate();

                    int rowsAffected2 = pst2.executeUpdate();

                    int rowsAffected3 = pst3.executeUpdate();

                    System.out.println("Rows affected: " + rowsAffected1);

                    System.out.println("Rows affected: " + rowsAffected2);

                    System.out.println("Rows affected: " + rowsAffected3);

                } catch (SQLException e) {

                    e.printStackTrace();

                }

                System.out.println("Delete complete.");

                pst1.close();

                pst2.close();

                pst3.close();

                myConn.close();

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public boolean displayRentersRemove(Connection myConn) {

        try {

            Statement myStmt = myConn.createStatement();

            String sql = "SELECT renterID, first_name, last_name, mobile_phone_number, email, driver_license_number, " +
                    "since_data, CONCAT(street, ' ', building, ' ', floor, ' ', door, ' ', zip, ' ', city, ' ',country.name)\n" +
                    "FROM renter JOIN address USING (addressID)\n" +
                    "JOIN phone_numbers USING (renterID)" +
                    "JOIN zip USING (zipID)" +
                    "JOIN country USING (countryID)" +
                    "WHERE renterID NOT IN (SELECT renterID FROM contract WHERE CURRENT_DATE() BETWEEN contract.start_time AND contract.end_time);";

            ResultSet rs = myStmt.executeQuery(sql);

            if (!rs.next()) {

                System.out.println("You cannot delete any renter, because they all have contracts at the moment.");

                return false;

            } else {

                System.out.printf("%-15s %-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", "Renter ID", "First Name", "Last Name",
                                 "Mobile Phone", "Email", "Driver License", "Since", "Address");

                for (int i = 0; i < 210; i++) {

                    System.out.print("-");

                }

                System.out.println();

                while (rs.next()) {

                    System.out.printf("%-15s %-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", rs.getString(1),
                            rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getString(5), rs.getString(6), rs.getString(7),
                            rs.getString(8));

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return true;
    }


    public void update() {

        try {

            Connection myConn = getConnection(url, user, password);

            PreparedStatement updateDriverLicenseNumber = null;
            PreparedStatement updateMobilePhone = null;
            PreparedStatement updateHomePhone = null;
            PreparedStatement safeUpdate = null;
            PreparedStatement updateAddress = null;
            PreparedStatement updateZip = null;
            PreparedStatement updateCountry = null;

            displayRenters();

            System.out.print("\nSelect renter ID: ");
            int renter_id = input.nextInt();

            System.out.println("\n[1] Driver License Number     [2] Mobile Phone     [3] Home Phone     [4] Address");
            System.out.print("Select the field you want to update: ");
            int field = input.nextInt();

            switch (field) {

                case 1:

                    try {

                        String updateDriverLicenseString = "UPDATE renter\n" +
                                                           "SET driver_license_number = ?\n" +
                                                           "WHERE renterID = ?;";

                        System.out.print("Enter new driver license number: ");

                        String newDriverLicenseNumber = input.next();

                        updateDriverLicenseNumber = myConn.prepareStatement(updateDriverLicenseString);

                        updateDriverLicenseNumber.setString(1, newDriverLicenseNumber);

                        updateDriverLicenseNumber.setInt(2, renter_id);

                        updateDriverLicenseNumber.executeUpdate();

                        System.out.println("Update complete.");

                        displayRenters();

                    } catch (SQLException e) {

                        e.printStackTrace();

                    }
                    break;

                case 2:

                    try {

                        String updateMobilePhoneString = "UPDATE phone_numbers \n" +
                                "SET mobile_phone_number = ?" +
                                "where renterID = ?";

                        System.out.print("Enter new mobile phone number: ");

                        String newMobilePhone = input.next();

                        updateMobilePhone = myConn.prepareStatement(updateMobilePhoneString);

                        updateMobilePhone.setString(1, newMobilePhone);

                        updateMobilePhone.setInt(2, renter_id);

                        updateMobilePhone.executeUpdate();

                        //myConn.close();

                        System.out.println("Update complete.");

                        displayRenters();

                    } catch (SQLException e) {

                        e.printStackTrace();

                    }
                    break;

                case 3:

                    try {

                        String updateHomePhoneString = "UPDATE phone_numbers \n" +
                                "SET home_phone_number = ?" +
                                "where renterID = ?";

                        System.out.print("Enter new home phone number: ");

                        String newHomePhone = input.next();

                        updateHomePhone = myConn.prepareStatement(updateHomePhoneString);

                        updateHomePhone.setString(1, newHomePhone);

                        updateHomePhone.setInt(2, renter_id);

                        updateHomePhone.executeUpdate();

                        //myConn.close();

                        System.out.println("Update complete.");

                        displayRenters();

                    } catch (SQLException e) {

                        e.printStackTrace();

                    }

                    break;

                case 4:

                    try {

                        System.out.print("Street Name: ");
                        String street = input.next();
                        while(!street.matches("[a-zA-Z_]+")){
                            System.out.print("Invalid Street Name. Try Again: ");
                            street = input.next();
                        }

                        System.out.print("Enter Street Number: ");
                        int building = Input.checkInt(1,5000);

                        System.out.print("Floor: ");
                        int floor = Input.checkInt(0,200);

                        System.out.print("Door (th/tv/mf/-): ");
                        String door = input.next();
                        while(!door.matches("(^(th)?|(tv)?|(mf)?|(-)?(\\s)?$)")){
                            System.out.println("Invalid Input. Try Again: ");
                            door = input.next();
                        }

                        System.out.print("Zip code: ");
                        String zip_code = input.next();
                        while(!zip_code.matches("[0-9]+")){
                            System.out.print("Invalid Input. Try Again: ");
                            zip_code = input.next();
                        }

                        System.out.print("Enter City: ");
                        String city = input.nextLine();
                        while(!city.matches("[a-zA-Z_]+(\\s)?([a-zA-Z_]+)?")){
                            System.out.println("Invalid City. Try Again: ");
                            city = input.nextLine();
                        }

                        System.out.print("Enter Country: ");
                        String country = input.next();
                        while(!country.matches("[a-zA-Z_]+(\\s)?([a-zA-Z_]+)?")) {
                            System.out.println("Invalid Country. Try Again: ");
                            country = input.next();
                        }

                        String safeUpadateString = "SET SQL_SAFE_UPDATES = 0";

                        safeUpdate = myConn.prepareStatement(safeUpadateString);

                        safeUpdate.execute();

                        String updateAddressString = "UPDATE address SET street = ?, building = ?, floor = ?, door = ?" +
                                "WHERE addressID IN (SELECT addressID FROM renter WHERE renterID = ?)";

                        String updateZipString = "UPDATE zip SET zip = ?, city = ?" +
                                "WHERE zipID IN " +
                                "(SELECT zipID FROM address JOIN renter WHERE renterID = ?)";

                        String updateCountryString = "UPDATE country SET country.name = ?" +
                                "WHERE countryID IN " +
                                "(SELECT countryID FROM zip JOIN address JOIN renter WHERE renterID = ?)";

                        updateAddress = myConn.prepareStatement(updateAddressString);

                        updateAddress.setString(1, street);

                        updateAddress.setInt(2, building);

                        updateAddress.setInt(3, floor);

                        updateAddress.setString(4, door);

                        updateAddress.setInt(5, renter_id);

                        updateZip = myConn.prepareStatement(updateZipString);

                        updateZip.setString(1, zip_code);

                        updateZip.setString(2, city);

                        updateZip.setInt(3, renter_id);

                        updateCountry = myConn.prepareStatement(updateCountryString);

                        updateCountry.setString(1, country);

                        updateCountry.setInt(2, renter_id);

                        updateAddress.executeUpdate();

                        updateZip.executeUpdate();

                        updateCountry.executeUpdate();

                        System.out.println("Update complete.");

                        displayRenters();

                        myConn.close();

                    } catch (SQLException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

}

