import java.sql.*;
import java.util.*;
import java.util.Date;

//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public class RenterMethods {

    Scanner input = new Scanner(System.in);
    Database database = new Database();
    ArrayList<Renter> renters = database.loadRenters();
    private static Set<String> zips= new HashSet<>();
    private static Set<String> countries= new HashSet<>();

    public RenterMethods() throws SQLException { //populate hashsets

        try {

            Connection myConn = database.myConn;
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("SELECT zip FROM zip");;
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

    /*public void loadRenters() throws SQLException{
        renters = database.loadRenters();
    }*/

    public void displayRenters() {
        System.out.printf("%-15s %-25s %-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", "Renter ID", "First Name", "Last Name",
                "Mobile Phone", "Home Phone", "Email", "Driver License", "Since", "Address");
        for (int i = 0; i < 215; i++) {
            System.out.print("-");
        }
        System.out.println();
        for (Renter renter : renters) {
            System.out.println(renter);
        }
    }

    public void displayZip() {
        List<String> list = new ArrayList<>();
        list.addAll(zips);
        Collections.sort(list);

        System.out.println("\nZip Codes in our Database");
        for (int i = 0; i < 25; i++) {
            System.out.print("-");
        }
        System.out.println();
        for (String zip : list) {
            System.out.println(zip);
        }
    }

    public void add() {

        try {

            //get a connection to database
            //Connection myConn = database.myConn;  // shows error
            String url = "jdbc:mysql://localhost:3306/kailua";
            String user = "dimk";
            String password = "dimk1234!";
            Connection myConn = getConnection(url, user, password);
            Statement myStmt = myConn.createStatement();

            Renter renter = new Renter();

            //prompt for the user input
            System.out.print("First name: ");
            String fname = input.next();
            while (!fname.matches("[a-zA-Z_]+")) {
                System.out.print("Invalid First Name. Try Again: ");
                fname = input.next();
            }
            renter.setFirst_name(fname.substring(0,1).toUpperCase() + fname.substring(1).toLowerCase());

            System.out.print("Last name: ");
            String lname = input.next();
            while(!lname.matches("[a-zA-Z_]+")){
                System.out.println("Invalid Last Name. Try Again: ");
                lname = input.next();
            }
            renter.setLast_name(lname.substring(0,1).toUpperCase() + lname.substring(1).toLowerCase());

            System.out.print("Mobile Phone (start with country code): ");
            String mobilePhone = input.next();
            while(!mobilePhone.matches("[0-9]{6,12}$")){
                System.out.print("Invalid Phone number (minimum 6 digits, maximum 12). Try Again: ");
                mobilePhone = input.next();
            }
            input.nextLine();

            System.out.print("Home Phone (start with country code) or [0] to skip: ");
            String homePhone = input.nextLine();
            while(!homePhone.equals("0") && !homePhone.matches("[0-9]{6,12}$")){
                System.out.print("Invalid Phone number. Try Again: ");
                homePhone = input.nextLine();
            }
            if (homePhone.equals("0")){ homePhone = null; }
            Telephone tel = new Telephone(mobilePhone, homePhone);
            renter.setTelephones(tel);

            renter.setEmail(Input.checkEmail());

            System.out.print("Driver Licence Number: ");
            renter.setDriverLicenseNumber(input.nextLine());

            renter.setSinceDate(Input.setDate());

            System.out.println("\n**** CLIENT ADDRESS ****\n");

            System.out.print("Street Name: ");
            String street = input.next();
            while (!street.matches("[a-zA-Z_]+")) {
                System.out.print("Invalid Street Name. Try Again: ");
                street = input.next();
            }
            street = (street.substring(0,1).toUpperCase() + street.substring(1).toLowerCase());

            System.out.print("Street Number: ");
            int building = Input.checkInt(1,5000);

            System.out.print("Floor: ");
            int floor = Input.checkInt(0,200);

            System.out.print("Door (th/tv/mf/-): ");
            String door = input.next();
            while(!door.matches("(^(th)?|(tv)?|(mf)?|(-)?(\\s)?$)")){
                System.out.println("Invalid Input. Try Again: ");
                door = input.next();
            }

            displayZip();
            System.out.println("\n[1] Select ZIP from the list     [2] Insert new one");
            System.out.print("Select the option: ");
            int field = Input.checkInt(1,2);
            String zip_code = "";
            int zipID = 0;
            String city = "";
            String country = "";

            switch (field) {

                case 1:

                    System.out.print("Type selected ZIP: ");
                    zip_code = input.next();
                    while (!zip_code.matches("[0-9]+") || !zips.contains(zip_code)) {
                        System.out.print("Invalid Input. Try Again: ");
                        zip_code = input.next();
                    }

                    String sql = "SELECT zipID, city, country.name\n" +
                            "FROM zip\n" +
                            "JOIN country USING (countryID)\n" +
                            "WHERE zip = \"" + zip_code + "\"\n" +
                            "LIMIT 1;";

                    ResultSet rs = myStmt.executeQuery(sql);
                    while (rs.next()) {
                        zipID = rs.getInt(1);
                        city = rs.getString(2);
                        country = rs.getString(3);
                    }

                    String query = "INSERT INTO address (street, building, floor, door, zipID) " +
                            "VALUES (?, ?, ?, ?, ?)";

                    PreparedStatement preparedStmt;  //create insert statements
                    preparedStmt = myConn.prepareStatement(query);
                    preparedStmt.setString(1, street);
                    preparedStmt.setInt(2, building);
                    preparedStmt.setInt(3, floor);
                    preparedStmt.setString(4, door);
                    preparedStmt.setInt(5, zipID);
                    preparedStmt.execute();
                    break;

                case 2:

                    System.out.print("Type new zip code: ");
                    zip_code = input.next();
                    while (!zip_code.matches("[0-9]+")) {
                        System.out.print("Invalid Input. Try Again: ");
                        zip_code = input.next();
                    }
                    input.nextLine();

                    System.out.print("City: ");
                    city = input.nextLine();
                    while (!city.matches("[a-zA-Z_]+(\\s)?([a-zA-Z_]+)?")) {
                        System.out.println("Invalid City. Try Again: ");
                        city = input.nextLine();
                    }

                    int countryID = 0;
                    country = Input.isCountryName();
                    if (!countries.contains(country)) {
                        String queryCountry = "INSERT INTO country (name) " +
                                "VALUES (?)";

                        String queryZip = "INSERT INTO zip (zip, city, countryID) " +
                                "VALUES (?, ?, LAST_INSERT_ID())";

                        String queryAddress = "INSERT INTO address (street, building, floor, door, zipID) " +
                                "VALUES (?, ?, ?, ?, LAST_INSERT_ID())";

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


                    } else {
                        sql = "SELECT countryID\n" +
                                "FROM country\n" +
                                "WHERE country.name = \"" + country + "\"\n" +
                                "LIMIT 1;";

                        rs = myStmt.executeQuery(sql);
                        while (rs.next()) {
                            countryID = rs.getInt(1);
                        }

                        String queryZip = "INSERT INTO zip (zip, city, countryID) " +
                                "VALUES (?, ?, ?)";

                        String queryAddress = "INSERT INTO address (street, building, floor, door, zipID) " +
                                "VALUES (?, ?, ?, ?, LAST_INSERT_ID())";

                        preparedStmt = myConn.prepareStatement(queryZip);
                        preparedStmt.setString(1, zip_code);
                        preparedStmt.setString(2, city);
                        preparedStmt.setInt(3, countryID);
                        preparedStmt.execute();

                        preparedStmt = myConn.prepareStatement(queryAddress);
                        preparedStmt.setString (1, street);
                        preparedStmt.setInt (2, building);
                        preparedStmt.setInt (3, floor);
                        preparedStmt.setString (4, door);
                        preparedStmt.execute();

                    }

                    break;

                default:
                    break;

            }

            Address address = new Address(street, building, floor, door, zip_code, city, country);
            renter.setAddresses(address);
            renters.add(renter);

            PreparedStatement preparedStmt;  //create left insert statements

            String queryRenter = "INSERT INTO renter (first_name, last_name, email, driver_license_number, since_data, " +
                    "addressID)" + " VALUES (?, ?, ?, ?, ?, LAST_INSERT_ID())";

            String queryPhoneNumbers = "INSERT INTO phone_numbers (renterID, mobile_phone_number, home_phone_number) " +
                    "VALUES (LAST_INSERT_ID(), ?, ?)";

            preparedStmt = myConn.prepareStatement(queryRenter);
            preparedStmt.setString (1, renter.getFirst_name());
            preparedStmt.setString (2, renter.getLast_name());
            preparedStmt.setString (3, renter.getEmail());
            preparedStmt.setString (4, renter.getDriverLicenseNumber());
            java.sql.Date sDate = new java.sql.Date(renter.getSinceDate().getTime());
            preparedStmt.setDate (5, sDate);
            preparedStmt.execute();

            preparedStmt = myConn.prepareStatement(queryPhoneNumbers);
            preparedStmt.setString (1, renter.getTelephone().getMobile_phone_number());
            preparedStmt.setString(2, renter.getTelephone().getHome_phone_number());
            preparedStmt.execute();

            //close connection
            myStmt.close();
            myConn.close();


        } catch (SQLException exc) {
            exc.printStackTrace();
        }

    }

    public void remove() throws SQLException {

        System.out.println("The program allows you to delete only renters who do not have any contract at the moment.");

        System.out.print("Select renter id: ");

        int renterID = Input.checkInt(1, database.checkRenterID());

        if (database.findRemovableRenters().size() == 0) {

            System.out.println("All renters have contracts. You cannot delete any.");

            return;

        }

        if (database.findRemovableRenters().contains(renterID)) {

            database.removeRenter(renterID);

        } else {

            System.out.println("The renter you selected cannot be deleted.");

        }

    }



    public void update() throws SQLException {
        displayRenters();
        System.out.print("\nSelect renter ID: ");
        int renter_id = Input.checkInt(1, database.checkRenterID());
        System.out.println("\n[1] Driver License Number     [2] Mobile Phone     [3] Home Phone     [4] Address");
        System.out.print("Select the field you want to update: ");
        int field = Input.checkInt(1,4);
        switch (field) {
            case 1:
                System.out.print("Enter new driver license number: ");
                String newDriverLicenseNumber = input.next();
                database.updateLicense(newDriverLicenseNumber, renter_id);
                //displayRenters();
                break;
            case 2:
                System.out.print("Enter new mobile phone number: ");
                String newMobilePhone = input.next();
                database.updateMobilePhone(newMobilePhone, renter_id);
                //displayRenters();
                break;
            case 3:
                System.out.print("Enter new home phone number: ");
                String newHomePhone = input.next();
                database.updateHomePhone(newHomePhone, renter_id);
                //displayRenters();
                break;
            case 4:
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

                String country = Input.isCountryName();

                //System.out.print("Enter Country: ");
                //String country = input.next();
                //while(!Input.isCountryName(country)) {
                //    System.out.println("Invalid country name. Try again: ");
                //    country = input.next();
                //}
                database.updateAddress(street, building, floor, door, renter_id, zip_code, city, country);
                //displayRenters();
                break;
        }
        //displayRenters();

    }

}

