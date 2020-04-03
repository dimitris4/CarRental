import java.sql.*;
import java.util.Date;
import java.util.Scanner;

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

    public void displayRenterTable() {
        try {
            // 1. get a connection to database
            Connection myConn = getConnection(url, user, password);
            // 2. create a statement
            Statement myStmt = myConn.createStatement();
            // 3. execute SQL query
            ResultSet myRs = myStmt.executeQuery("select * from renter");
            System.out.println();
            // 4. process the result set
            while (myRs.next()) {
                System.out.printf("%-15s %-25s %-25s %-25s %-25s %-25s %-25s\n", myRs.getString(1),
                    myRs.getString(2), myRs.getString(3), myRs.getString(4),
                    myRs.getString(5), myRs.getString(6), myRs.getString(7));
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }


    public void add() {

        // prompt for the user input
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
        System.out.print("Home Phone (start with country code): ");
        String homePhone = console.nextLine();
        while(!homePhone.matches("[0-9]+")){
            System.out.print("Invalid Phone number. Try Again: ");
            homePhone = console.nextLine();
        }
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

        System.out.print("Zip code: ");
        String zip_code = console.next();
        while(!zip_code.matches("[0-9]+")){
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

        try {
            //get a connection to database
            Connection myConn = getConnection(url, user, password);

            //create insert statements
            String queryCountry = "INSERT INTO country (name) " +
                                  "VALUES (?)";

            String queryZip = "INSERT INTO zip (zip, city, countryID) " +
                              "VALUES (?, ?, LAST_INSERT_ID())";

            String queryAddress = "INSERT INTO address (street, building, floor, door, zipID) " +
                                  "VALUES (?, ?, ?, ?, LAST_INSERT_ID())";

            String queryRenter = "INSERT INTO renter (first_name, last_name, email, driver_license_number, since_data, " +
                                 "addressID)" + " VALUES (?, ?, ?, ?, ?, LAST_INSERT_ID())";

            String queryPhoneNumbers = "INSERT INTO phone_numbers (renterID, mobile_phone_number, home_phone_number) " +
                    "VALUES (LAST_INSERT_ID(), ?, ?)";

            //create insert PreparedStatement
            PreparedStatement preparedStmt;

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

    /*public void remove() {

        try {

            Connection myConn = getConnection(url, user, password);

            if (!displayRentersRemove(myConn)) {

                return;

            } else {

                PreparedStatement pst1 = null;
                PreparedStatement pst2 = null;
                PreparedStatement pst3 = null;

                String sql1 = "DELETE FROM contract WHERE renterID = ?";
                String sql2 = "DELETE FROM phone_numbers"
                String sql3 = "DELETE FROM renter WHERE renterID = ?";

                System.out.print("Select the renter id: ");

                int choice = input.nextInt();

                try {

                    pst1 = myConn.prepareStatement(sql1);

                    pst2 = myConn.prepareStatement(sql2);

                    pst1.setInt(1, choice);

                    pst2.setInt(1, choice);

                    int rowsAffected1 = pst1.executeUpdate();

                    int rowsAffected2 = pst2.executeUpdate();

                    System.out.println("Rows affected: " + rowsAffected1);

                    System.out.println("Rows affected: " + rowsAffected2);

                } catch (SQLException e) {

                    e.printStackTrace();

                }

                System.out.println("Delete complete.");

                pst1.close();

                pst2.close();

                myConn.close();

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public boolean displayRentersRemove(Connection myConn) {

        try {

            Statement myStmt = myConn.createStatement();

            String sql = "SELECT renterID, first_name, last_name, mobile_phone_number, email, driver_license_number, since_data, CONCAT(street, building, floor, door, zip, city, country.name)\n" +
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
            PreparedStatement updateAddress = null;

            displayRentersUpdate(myConn);

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

                        displayRentersUpdate(myConn);

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

                        displayRentersUpdate(myConn);

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

                        myConn.close();

                        System.out.println("Update complete.");

                        //displayRentersUpdate(myConn);

                    } catch (SQLException e) {

                        e.printStackTrace();

                    }
                    break;

                case 4:

                    try {

                        String updateAddressString = "UPDATE address SET street = ?, building = ?, floor = ?, door = ?, " +
                                "zip_code = ?, city = ?, country = ?  " +
                                "WHERE addressID =  (SELECT addressID FROM renter WHERE renterID = ?)";

                        System.out.print("Enter Street Name: ");
                        String street = input.nextLine();
                        while(!street.matches("[a-zA-Z_]+")){
                            System.out.println("Invalid Street Name. Try Again: ");
                            street = input.nextLine();
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

                        updateAddress = myConn.prepareStatement(updateAddressString);

                        updateAddress.setString(1, street);

                        updateAddress.setInt(2, building);

                        updateAddress.setInt(3, floor);

                        updateAddress.setString(4, door);

                        updateAddress.setString(5, zip_code);

                        updateAddress.setString(6, city);

                        updateAddress.setString(7, country);

                        updateMobilePhone.executeUpdate();

                        myConn.close();

                        System.out.println("Update complete.");

                        //displayRentersUpdate(myConn);

                    } catch (SQLException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }


    private void displayRentersUpdate(Connection myConn) {

        try {

            Statement myStmt = myConn.createStatement();

            String sql = "SELECT renterID, first_name, last_name, mobile_phone_number, home_phone_number, email, " +
                                 "driver_license_number, since_data, CONCAT(street, ' ', building, ' ', floor, ' ', door, ' ', zip_code)" +
                         "FROM renter JOIN address USING (addressID)" +
                                     "JOIN phone_numbers USING (renterID)";

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



    public void searchByDriverLicenseNumber(String driverLicenseNumber) {
    }

    public void searchByRenterLastName(String lastName) {
    }

    public void displayPhoneTable() {
        try {
            // 1. get a connection to database
            Connection myConn = getConnection(url, user, password);
            // 2. create a statement
            Statement myStmt = myConn.createStatement();
            // 3. execute SQL query
            ResultSet myRs = myStmt.executeQuery("select * from phone_numbers");
            System.out.println();
            // 4. process the result set
            while (myRs.next()) {

                System.out.printf("%-15s %-25s\n", myRs.getString(1),
                        myRs.getString(2));

            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }*/

}

