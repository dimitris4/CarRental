import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;
import java.text.SimpleDateFormat;

import static java.sql.DriverManager.getConnection;

public class Database {

    String url = "jdbc:mysql://localhost:3306/kailua";
    String user = "dimk";
    String password = "dimk1234!";
    Connection myConn = getConnection(url, user, password);


    // @Ilias: move them to the contract methods
    private static ArrayList<Contract> contracts;
    public Database() throws SQLException {
        contracts = new ArrayList<Contract>();
    }
    public ArrayList<Contract> getContracts() {
        return contracts;
    }
    public void setContracts(ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }



    public ArrayList<Renter> loadRenters() {
        ArrayList<Renter> renters = new ArrayList<>();
        try {
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
            while (rs.next()) {
                int renterID = Integer.parseInt(rs.getString(1));
                String first_name = rs.getString(2);
                String last_name = rs.getString(3);
                String mobile_phone_number = rs.getString(4);
                String home_phone_number = rs.getString(5);
                String email = rs.getString(6);
                String driver_license_number = rs.getString(7);
                java.util.Date since_data = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(8));
                String full_address = rs.getString(9);
                String[] parts = full_address.split(" ");
                String street = parts[0];
                int building = Integer.parseInt(parts[1]);
                int floor = Integer.parseInt(parts[2]);
                String door = parts[3];
                String zip = parts[4];
                String city = parts[5];
                String country = parts[6];
                Address address = new Address(street, building, floor, door, zip, city, country);
                Telephone telephone = new Telephone(mobile_phone_number, home_phone_number);
                Renter renter = new Renter(renterID, first_name, last_name, telephone, address, email, driver_license_number, since_data);
                renters.add(renter);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return renters;
    }

    public void addRenter(String country, String zip_code, String city, String street, int building, int floor, String door,
                          String fname, String lname, String email, String licence, Date sinceDate, String mobilePhone,
                          String homePhone) {
        try {
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

            System.out.println("\n\nThe entry has been recorded.");

            //close connection
            myConn.close();

        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    public void removeRenter(int renterID) {
        try {
            PreparedStatement pst1 = null;
            PreparedStatement pst2 = null;
            PreparedStatement pst3 = null;

            String sql1 = "DELETE FROM contract WHERE renterID = ?";
            String sql2 = "DELETE FROM phone_numbers WHERE renterID = ?";
            String sql3 = "DELETE FROM renter WHERE renterID = ?";

            try {

                pst1 = myConn.prepareStatement(sql1);

                pst2 = myConn.prepareStatement(sql2);

                pst3 = myConn.prepareStatement(sql3);

                pst1.setInt(1, renterID);

                pst2.setInt(1, renterID);

                pst3.setInt(1, renterID);

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

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    public ArrayList<Integer> findRemovableRenters() {

        ArrayList<Integer> result = new ArrayList<>();

        try {

            Statement myStmt = myConn.createStatement();

            String sql = "SELECT renterID\n" +
                    "FROM renter JOIN address USING (addressID)\n" +
                    "JOIN phone_numbers USING (renterID)" +
                    "JOIN zip USING (zipID)" +
                    "JOIN country USING (countryID)" +
                    "WHERE renterID NOT IN (SELECT renterID FROM contract WHERE CURRENT_DATE() BETWEEN contract.start_time AND contract.end_time)";

            ResultSet rs = myStmt.executeQuery(sql);

            if (!rs.next()) {

                return result;

            } else {

                while (rs.next()) {
                    int renterID = Integer.parseInt(rs.getString(1));
                    result.add(renterID);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }

    public int checkRenterID() throws SQLException {

        Statement maxRenterID = myConn.createStatement();

        String maxRenterIDString = "SELECT MAX(renterID) FROM renter;";

        ResultSet rs = maxRenterID.executeQuery(maxRenterIDString);

        int i = 0;

        while (rs.next()) {

            i = Integer.parseInt(rs.getString(1));

        }
        //System.out.println(i);
        return i;
    }

    public void updateLicense(String newDriverLicenseNumber, int renter_id) {

        try {

            PreparedStatement updateDriverLicenseNumber = null;

            String updateDriverLicenseString = "UPDATE renter\n" +
                        "SET driver_license_number = ?\n" +
                        "WHERE renterID = ?;";

            updateDriverLicenseNumber = myConn.prepareStatement(updateDriverLicenseString);

            updateDriverLicenseNumber.setString(1, newDriverLicenseNumber);

            updateDriverLicenseNumber.setInt(2, renter_id);

            updateDriverLicenseNumber.executeUpdate();

            System.out.println("Update complete.");

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    public void updateMobilePhone(String newMobilePhone, int renter_id) {
        try {
            PreparedStatement updateMobilePhone = null;
            String updateMobilePhoneString = "UPDATE phone_numbers \n" +
                    "SET mobile_phone_number = ?" +
                    "where renterID = ?";
            updateMobilePhone = myConn.prepareStatement(updateMobilePhoneString);

            updateMobilePhone.setString(1, newMobilePhone);

            updateMobilePhone.setInt(2, renter_id);

            updateMobilePhone.executeUpdate();

            System.out.println("Update complete.");

            myConn.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    public void updateHomePhone(String newHomePhone, int renter_id) {
        try {
            PreparedStatement updateHomePhone = null;

            String updateHomePhoneString = "UPDATE phone_numbers \n" +
                    "SET home_phone_number = ?" +
                    "where renterID = ?";
            updateHomePhone = myConn.prepareStatement(updateHomePhoneString);

            updateHomePhone.setString(1, newHomePhone);

            updateHomePhone.setInt(2, renter_id);

            updateHomePhone.executeUpdate();

            System.out.println("Update complete.");

            myConn.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    public void updateAddress(String street, int building, int floor, String door, int renter_id,
                              String zip_code, String city, String country) throws SQLException {

        PreparedStatement safeUpdate = null;

        String safeUpadateString = "SET SQL_SAFE_UPDATES = 0";

        safeUpdate = myConn.prepareStatement(safeUpadateString);

        safeUpdate.execute();

        PreparedStatement updateAddress = null;
        PreparedStatement updateZip = null;
        PreparedStatement updateCountry = null;

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

        myConn.close();
    }

    public HashSet<Integer> populateZipIdHashSet() {

        HashSet<Integer> zipIDs = new HashSet<Integer>();

        try {
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("SELECT zipID FROM zip");;
            while (myRs.next()) {
            zipIDs.add(myRs.getInt("zipID"));
            }
        } catch (SQLException e) {
        e.printStackTrace();
        }

        return zipIDs;

    }

    public HashSet<String> populateZipHashSet() {

        HashSet<String> zips = new HashSet<>();

        try {
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("SELECT zip FROM zip");;
            while(myRs.next()) {
                zips.add(myRs.getString("zip"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return zips;

    }

    public HashSet<String> populateCountriesHashSet() {

        HashSet<String> countries = new HashSet<>();

        try {
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("SELECT name FROM country");
            while(myRs.next()) {
                countries.add(myRs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countries;

    }

    public void displayZip() {
        Statement myStmt = null;
        try {
            myStmt = myConn.createStatement();
            String sql = "SELECT zipID, zip, city, country.name\n" +
                    "FROM zip\n" +
                    "JOIN country USING (countryID)\n" +
                    "ORDER BY zip;";

            ResultSet rs = myStmt.executeQuery(sql);

            System.out.println();

            System.out.printf("%-15s %-15s %-25s %-25s\n", "Zip ID", "Zip Code", "City", "Country");

            for (int i = 0; i < 65; i++) {
                System.out.print("-");
            }

            System.out.println();

            while (rs.next()) {
                System.out.printf("%-15s %-15s %-25s %-25s\n", rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // insert renter with known zip code (tables affected: renter, phone_numbers, address)
    public void addRenter(String fname, String lname, String mobilePhone, String homePhone, String email,
                          String licence, Date sqlDate, String street, int building, int floor, String door,
                          int zipID) {

        try {

            String query = "INSERT INTO address (street, building, floor, door, zipID) " +
                    "VALUES (?, ?, ?, ?, ?)";

            String queryRenter = "INSERT INTO renter (first_name, last_name, email, driver_license_number, since_data, " +
                    "addressID)" + " VALUES (?, ?, ?, ?, ?, LAST_INSERT_ID())";

            String queryPhoneNumbers = "INSERT INTO phone_numbers (renterID, mobile_phone_number, home_phone_number) " +
                    "VALUES (LAST_INSERT_ID(), ?, ?)";

            PreparedStatement preparedStmt;  //create insert statements
            preparedStmt = myConn.prepareStatement(query);
            preparedStmt.setString(1, street);
            preparedStmt.setInt(2, building);
            preparedStmt.setInt(3, floor);
            preparedStmt.setString(4, door);
            preparedStmt.setInt(5, zipID);
            preparedStmt.execute();

            preparedStmt = myConn.prepareStatement(queryRenter);
            preparedStmt.setString (1, fname);
            preparedStmt.setString (2, lname);
            preparedStmt.setString (3, email);
            preparedStmt.setString (4, licence);
            preparedStmt.setDate (5, sqlDate);
            preparedStmt.execute();

            preparedStmt = myConn.prepareStatement(queryPhoneNumbers);
            preparedStmt.setString (1, mobilePhone);
            preparedStmt.setString(2, homePhone);
            preparedStmt.execute();

            System.out.println("Added!!!!");

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }



    public void addRenter(String zip_code, String city, String street, int building, int floor, String door,
                          String fname, String lname, String email, String licence, Date sinceDate,
                          String mobilePhone, String homePhone, int countryID) {

        try {

            String queryZip = "INSERT INTO zip (zip, city, countryID) " +
                    "VALUES (?, ?, ?)";

            String queryAddress = "INSERT INTO address (street, building, floor, door, zipID) " +
                    "VALUES (?, ?, ?, ?, LAST_INSERT_ID())";

            String queryRenter = "INSERT INTO renter (first_name, last_name, email, driver_license_number, since_data, " +
                    "addressID)" + " VALUES (?, ?, ?, ?, ?, LAST_INSERT_ID())";

            String queryPhoneNumbers = "INSERT INTO phone_numbers (renterID, mobile_phone_number, home_phone_number) " +
                    "VALUES (LAST_INSERT_ID(), ?, ?)";

            PreparedStatement preparedStmt;  //create insert statements

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

            preparedStmt = myConn.prepareStatement(queryRenter);
            preparedStmt.setString (1, fname);
            preparedStmt.setString (2, lname);
            preparedStmt.setString (3, email);
            preparedStmt.setString (4, licence);
            preparedStmt.setDate (5, sinceDate);
            preparedStmt.execute();

            preparedStmt = myConn.prepareStatement(queryPhoneNumbers);
            preparedStmt.setString (1, mobilePhone);
            preparedStmt.setString(2, homePhone);
            preparedStmt.execute();

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }



    public int getCountryID(String country) {

        int countryID = 0;

        try {

            PreparedStatement myStmt = (PreparedStatement) myConn.createStatement();

            String sql = "SELECT countryID\n" +
                    "FROM country\n" +
                    "WHERE country.name = \"" + country + "\"\n" +
                    "LIMIT 1;";

            ResultSet rs = myStmt.executeQuery(sql);
            while (rs.next()) {
                countryID = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countryID;
    }

}
