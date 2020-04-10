import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

import static java.sql.DriverManager.getConnection;

public class Database {

    static final public Database instance = new Database();
    String url = "jdbc:mysql://localhost:3306/kailua";
    String user = "dimk";
    String password = "dimk1234!";
    private static ArrayList<CarInformation> carList = new ArrayList<>();

    public ArrayList<Renter> loadRenters() {
        ArrayList<Renter> renters = new ArrayList<>();
        try {
            Connection myConn = getConnection(url, user, password);
            Statement myStmt = myConn.createStatement();
            String sql = "SELECT renterID, first_name, last_name, mobile_phone_number, home_phone_number, email,\n" +
                    "\t   driver_license_number, since_data, street, building, floor, \n" +
                    "       door, zip, city, country.name\n" +
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
                String street = rs.getString(9);
                int building = rs.getInt(10);
                int floor = rs.getInt(11);
                String door = rs.getString(12);
                String zip = rs.getString(13);
                String city = rs.getString(14);
                String country = rs.getString(15);
                Address address = new Address(street, building, floor, door, zip, city, country);
                Telephone telephone = new Telephone(mobile_phone_number, home_phone_number);
                Renter renter = new Renter(renterID, first_name, last_name, telephone, address, email, driver_license_number, since_data);
                renters.add(renter);
            }
            myConn.close();
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return renters;
    }

    public ArrayList<CarInformation> getCarList(){return carList;}

    public void addRenter(String country, String zip_code, String city, String street, int building, int floor, String door,
                          String fname, String lname, String email, String licence, Date sinceDate, String mobilePhone,
                          String homePhone) {
        try {
            Connection myConn = getConnection(url, user, password);

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
            System.out.println("\nThe renter has been added.");
            myConn.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    public void removeRenter(int renterID) {
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement pst1 = null;
            PreparedStatement pst2 = null;
            PreparedStatement pst3 = null;
            String sql1 = "DELETE FROM address WHERE addressID = (SELECT addressID FROM renter WHERE renterID = ?)";
            String sql2 = "DELETE FROM phone_numbers WHERE renterID = ?";
            String sql3 = "DELETE FROM renter WHERE renterID = ?";
            try {
                pst1 = myConn.prepareStatement(sql1);
                pst2 = myConn.prepareStatement(sql2);
                pst3 = myConn.prepareStatement(sql3);

                pst1.setInt(1, renterID);
                pst2.setInt(1, renterID);
                pst3.setInt(1, renterID);
                disableForeignKeyChecks(myConn);

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
            enableForeignKeyChecks(myConn);
            pst1.close();
            pst2.close();
            pst3.close();
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void disableForeignKeyChecks(Connection myConn) {
        try {
            PreparedStatement safeUpdate = null;
            String safeUpadateString = "SET FOREIGN_KEY_CHECKS=0;";
            safeUpdate = myConn.prepareStatement(safeUpadateString);
            safeUpdate.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void enableForeignKeyChecks(Connection myConn) {
        try {
            PreparedStatement safeUpdate = null;
            String safeUpadateString = "SET FOREIGN_KEY_CHECKS=1;";
            safeUpdate = myConn.prepareStatement(safeUpadateString);
            safeUpdate.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> findRemovableRenters() {
        ArrayList<Integer> result = new ArrayList<>();
        try {
            Connection myConn = getConnection(url, user, password);
            Statement myStmt = myConn.createStatement();

            String sql = "SELECT renterID\n" +
                    "FROM renter JOIN address USING (addressID)\n" +
                    "JOIN phone_numbers USING (renterID)" +
                    "JOIN zip USING (zipID)" +
                    "JOIN country USING (countryID)" +
                    "WHERE renterID NOT IN (SELECT renterID FROM contract WHERE CURRENT_DATE() BETWEEN contract.start_time AND contract.end_time)";

            ResultSet rs = myStmt.executeQuery(sql);
            while (rs.next()) {
                int renterID = Integer.parseInt(rs.getString(1));
                result.add(renterID);
            }
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateLicense(String newDriverLicenseNumber, int renter_id) {
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement updateDriverLicenseNumber = null;

            String updateDriverLicenseString = "UPDATE renter\n" +
                        "SET driver_license_number = ?\n" +
                        "WHERE renterID = ?;";

            updateDriverLicenseNumber = myConn.prepareStatement(updateDriverLicenseString);
            updateDriverLicenseNumber.setString(1, newDriverLicenseNumber);
            updateDriverLicenseNumber.setInt(2, renter_id);
            updateDriverLicenseNumber.executeUpdate();
            System.out.println("Update complete.");
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMobilePhone(String newMobilePhone, int renter_id) {
        try {
            Connection myConn = getConnection(url, user, password);
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
            Connection myConn = getConnection(url, user, password);
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

    public HashSet<String> loadZips() {
        HashSet<String> zips = new HashSet<>();
        try {
            Connection myConn = getConnection(url, user, password);
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("SELECT zip FROM zip");;
            while(myRs.next()) {
                zips.add(myRs.getString("zip"));
            }
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zips;
    }

    public HashSet<String> loadCountries() {
        HashSet<String> countries = new HashSet<>();
        try {
            Connection myConn = getConnection(url, user, password);
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("SELECT name FROM country");
            while(myRs.next()) {
                countries.add(myRs.getString("name"));
            }
            myRs.close();
            myStmt.close();
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }

    // insert renter with known zip code (tables affected: renter, phone_numbers, address)
    public void addRenter(String fname, String lname, String mobilePhone, String homePhone, String email,
                          String licence, Date sqlDate, String street, int building, int floor, String door,
                          int zipID) {
        try {
            Connection myConn = getConnection(url, user, password);

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
            System.out.println("\nThe renter has been added.");
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRenter(String zip_code, String city, String street, int building, int floor, String door,
                          String fname, String lname, String email, String licence, Date sinceDate,
                          String mobilePhone, String homePhone, int countryID) {
        try {
            Connection myConn = getConnection(url, user, password);

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
            System.out.println("\nThe renter has been added.");
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCountryID(String country) {
        int countryID = 0;
        try {
            Connection myConn = getConnection(url, user, password);
            Statement myStmt = myConn.createStatement();

            String sql = "SELECT countryID\n" +
                    "FROM country\n" +
                    "WHERE country.name = '" + country + "'\n" +
                    "LIMIT 1;";

            ResultSet rs = myStmt.executeQuery(sql);
            while (rs.next()) {
                countryID = rs.getInt(1);
            }
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryID;
    }

    public ArrayList<String> getCityCountryName(String zip_code) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            Connection myConn = getConnection(url, user, password);
            Statement myStmt = myConn.createStatement();
            String sql = "SELECT city, country.name\n" +
                    "FROM country\n" +
                    "JOIN zip USING (countryID)\n" +
                    "WHERE zip = '" + zip_code + "'\n" +
                    "LIMIT 1;";

            ResultSet rs = myStmt.executeQuery(sql);
            while (rs.next()) {
                result.add(rs.getString("city"));
                result.add(rs.getString("name"));
            }
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getZipID(String zip_code) {
        int result = 0;
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement getZipID = null;
            String sql = "SELECT zipID FROM zip WHERE zip = ?";
            getZipID = myConn.prepareStatement(sql);
            getZipID.setString(1, zip_code);
            ResultSet rs = getZipID.executeQuery();
            while (rs.next()) {
                result = rs.getInt("zipID");
            }
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Integer> getRenterIDs() throws SQLException {
        ArrayList<Integer> result = new ArrayList<Integer>();
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement getRenterID = null;
            String sql = "SELECT renterID FROM renter";
            getRenterID = myConn.prepareStatement(sql);
            ResultSet rs = getRenterID.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("renterID"));
            }
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // select zip code from the list : update the address table only
    public void updateAddress(String street, int building, int floor, String door, int zipID, int renter_id) throws SQLException {
        System.out.println("renter id : " + renter_id);
        System.out.println("zipID : " + zipID);
        Connection myConn = getConnection(url, user, password);
        PreparedStatement updateAddress = null;
        String updateAddressString = "UPDATE address SET street = ?, building = ?, floor = ?, door = ?, zipID = ?\n" +
                "WHERE addressID = (SELECT addressID FROM renter WHERE renterID = ?);";
        updateAddress = myConn.prepareStatement(updateAddressString);
        updateAddress.setString(1, street);
        updateAddress.setInt(2, building);
        updateAddress.setInt(3, floor);
        updateAddress.setString(4, door);
        updateAddress.setInt(5, zipID);
        updateAddress.setInt(6, renter_id);
        System.out.println("Update complete.");
        updateAddress.executeUpdate();
        myConn.close();
    }

    // update address, zip tables and country tables (unknown zip - unknown country)
    public void updateAddressZipCountry(String street, int building, int floor, String door, String zip_code,
                                        String city, int countryID, int renter_id) throws SQLException {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement safeUpdate = null;
            String safeUpadateString = "SET SQL_SAFE_UPDATES = 0";
            safeUpdate = myConn.prepareStatement(safeUpadateString);
            safeUpdate.execute();

            PreparedStatement updateAddress = null;
            PreparedStatement updateZip = null;
            String updateAddressString = "UPDATE address SET street = ?, building = ?, floor = ?, door = ?\n" +
                    "WHERE addressID = (SELECT addressID FROM renter WHERE renterID = ?);\n";

            String updateZipString = "UPDATE zip SET zip = ?, city = ?, countryID = ?\n" +
                    "WHERE zipID = \n" +
                    "\t(SELECT zipID FROM address JOIN renter USING (addressID) WHERE renterID = ?);";
            updateAddress = myConn.prepareStatement(updateAddressString);
            updateAddress.setString(1, street);
            updateAddress.setInt(2, building);
            updateAddress.setInt(3, floor);
            updateAddress.setString(4, door);
            updateAddress.setInt(5, renter_id);

            updateZip = myConn.prepareStatement(updateZipString);
            updateZip.setString(1, zip_code);
            updateZip.setString(2, city);
            updateZip.setInt(3, countryID);
            updateZip.setInt(4, renter_id);
            updateAddress.executeUpdate();
            updateZip.executeUpdate();
            System.out.println("Update complete.");
            myConn.close();
    }

    // update address and zip tables (unknown zip - known country)
    public void updateAddressZip(String street, int building, int floor, String door, String zip_code, String city,
                                 int country_id, int renter_id) throws SQLException {
        Connection myConn = getConnection(url, user, password);

        PreparedStatement updateAddress = null;
        PreparedStatement updateZip = null;
        String updateAddressString = "UPDATE address SET street = ?, building = ?, floor = ?, door = ?\n" +
                "WHERE addressID = (SELECT addressID FROM renter WHERE renterID = ?);\n";

        String updateZipString = "UPDATE zip SET zip = ?, city = ?, countryID = ?\n" +
                "WHERE zipID = \n" +
                "\t(SELECT zipID FROM address JOIN renter USING (addressID) WHERE renterID = ?);";
        updateAddress = myConn.prepareStatement(updateAddressString);
        updateAddress.setString(1, street);
        updateAddress.setInt(2, building);
        updateAddress.setInt(3, floor);
        updateAddress.setString(4, door);
        updateAddress.setInt(5, renter_id);

        updateZip = myConn.prepareStatement(updateZipString);
        updateZip.setString(1, zip_code);
        updateZip.setString(2, city);
        updateZip.setInt(3, country_id);
        updateZip.setInt(4, renter_id);

        updateAddress.executeUpdate();
        updateZip.executeUpdate();
        System.out.println("Update complete.");
        myConn.close();
    }

    public ArrayList<String> getCarRegistrationNumbers() throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement getRegistrationNumbers = null;
            String sql = "SELECT registration_number FROM car";
            getRegistrationNumbers = myConn.prepareStatement(sql);
            ResultSet rs = getRegistrationNumbers.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("registration_number"));
            }
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void displayAvailableCarsWithinDateRange(java.util.Date start_time, java.util.Date end_time) throws Exception {
        Connection myConn = getConnection(url, user, password);
        PreparedStatement preparedStatement = null;
        String sql = "select distinct rental_types.name, registration_number, brand.name, model.name, " +
                "                     first_registration, odometer, fuel.fuel_type\n" +
                     "from car left join contract ON car.registration_number = contract.car_registration_number\n" +
                     "\t\t inner join brand using (brandID)\n" +
                 "         inner join model using (modelID)\n" +
                     "\t\t inner join fuel using (fuelID)\n" +
                 "         inner join rental_types USING (rental_typeID)\n" +
                     "where car.is_available = 1 AND car.registration_number not in\n" +
                            "\t(select car.registration_number\n" +
                            "\t from car left join contract ON car.registration_number = contract.car_registration_number\n" +
                          "     where (start_time <= ? AND end_time >= ?) \n" +
                                    "\t\t\tOR (start_time >= ? AND end_time <= ?) \n" +
                                    "\t\t\tOR (start_time >= ? AND end_time >= ? AND start_time <= ?) \n" +
                                    "\t\t\tOR (start_time <= ? AND end_time <= ? AND end_time >= ?))\n" +
                      "order by rental_types.name;";
        preparedStatement = myConn.prepareStatement(sql);
        java.sql.Date startDate = new java.sql.Date(start_time.getTime());
        java.sql.Date endDate = new java.sql.Date(end_time.getTime());
        preparedStatement.setDate(1, startDate);
        preparedStatement.setDate(2, endDate);
        preparedStatement.setDate(3, startDate);
        preparedStatement.setDate(4, endDate);
        preparedStatement.setDate(5, startDate);
        preparedStatement.setDate(6, endDate);
        preparedStatement.setDate(7, endDate);
        preparedStatement.setDate(8, startDate);
        preparedStatement.setDate(9, endDate);
        preparedStatement.setDate(10, startDate);
        ResultSet myRs = preparedStatement.executeQuery();
        System.out.printf("%-15s %-25s %-25s %-25s %-25s %-25s %-25s\n", "Rental Type", "Registration Nr.", "Brand",
                            "Model", "First Registration Date", "Odometer", "Fuel Type");
        for (int i=0; i<160; i++) {
            System.out.print("*");
        }
        System.out.println();
        if (myRs.next() == false) {
            System.out.println("\nWe are sorry, there are no cars available on the given dates. :(");
            System.out.print("Enter [1] to try other dates or [0] to go back to menu: ");
            int i = Input.checkInt(0,1);
            if (i==1) {
                ContractMethods contractMethods = new ContractMethods();
                contractMethods.addContract();
            } else {
                Menus menus = new Menus();
                menus.contractMenu();
            }
        } else {
            do {
                System.out.printf("%-15s %-25s %-25s %-25s %-25s %-25s %-25s\n", myRs.getString(1),
                        myRs.getString(2), myRs.getString(3), myRs.getString(4),
                        myRs.getString(5), myRs.getString(6), myRs.getString(7));
            } while (myRs.next());
        }
        myConn.close();
    }

    public ArrayList<Integer> getContractIDs() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement getContractIDs = null;
            String sql = "SELECT contractID FROM contract";
            getContractIDs = myConn.prepareStatement(sql);
            ResultSet rs = getContractIDs.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("contractID"));
            }
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateContract(int actual_km, Date today, int contractID) {
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement updateContract = null;
            PreparedStatement updateCar = null;

            String updateContractString = "UPDATE contract SET end_time = ?, actual_km = ? WHERE contractID = ?";

            String updateCarString = "UPDATE car SET odometer = odometer + ?\n" +
                                     "WHERE car.registration_number =\n" +
                                    "(SELECT car_registration_number FROM contract WHERE contractID = ?)";

            PreparedStatement pst = myConn.prepareStatement(updateContractString);
            PreparedStatement pst2 = myConn.prepareStatement(updateCarString);
            pst.setDate(1, today);
            pst.setInt(2, actual_km);
            pst.setInt(3, contractID);

            pst2.setInt(1, actual_km);
            pst2.setInt(2, contractID);

            pst.executeUpdate();
            pst2.executeUpdate();
            System.out.println("Update complete.");
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeContract(int contractID) {
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement deleteContract = null;
            String sql = "DELETE FROM contract WHERE contractID = ?";
            try {
                deleteContract = myConn.prepareStatement(sql);
                deleteContract.setInt(1, contractID);
                int rowsAffected = deleteContract.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Delete complete.");
            deleteContract.close();
            myConn.close();
        } catch (SQLException e) {
           e.printStackTrace();
        }
    }

    public void addCountry(String country) {
        try {
            Connection myConn = getConnection(url, user, password);
            String queryCountry = "INSERT INTO country (name) " +
                    "VALUES (?)";
            //create insert PreparedStatement
            PreparedStatement addCountry;
            addCountry = myConn.prepareStatement(queryCountry);
            addCountry.setString(1, country);
            addCountry.execute();
            System.out.println("\nThe country has been added.");
            addCountry.close();
            myConn.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    public String getEmail(int renter_id) {
        String email = "";
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement getEmail = null;
            String sql = "SELECT email FROM renter WHERE renterID = ?";
            getEmail = myConn.prepareStatement(sql);
            getEmail.setInt(1, renter_id);
            ResultSet rs = getEmail.executeQuery();
            while (rs.next()) {
                email = rs.getString("email");
            }
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }

    public String getRenterFirstName(int renter_id) {
        String firstName = "";
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement getFirstName = null;
            String sql = "SELECT first_name FROM renter WHERE renterID = ?";
            getFirstName = myConn.prepareStatement(sql);
            getFirstName.setInt(1, renter_id);
            ResultSet rs = getFirstName.executeQuery();
            while (rs.next()) {
                firstName = rs.getString("first_name");
            }
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return firstName;
    }

    public String getRenterLastName(int renter_id) {
        String lastName = "";
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement getLastName = null;
            String sql = "SELECT last_name FROM renter WHERE renterID = ?";
            getLastName = myConn.prepareStatement(sql);
            getLastName.setInt(1, renter_id);
            ResultSet rs = getLastName.executeQuery();
            while (rs.next()) {
                lastName = rs.getString("last_name");
            }
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastName;
    }

    public String getRenterDriverLicense(int renter_id) {
        String driverLicense = "";
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement getDriverLicense = null;
            String sql = "SELECT driver_license_number FROM renter WHERE renterID = ?";
            getDriverLicense = myConn.prepareStatement(sql);
            getDriverLicense.setInt(1, renter_id);
            ResultSet rs = getDriverLicense.executeQuery();
            while (rs.next()) {
                driverLicense = rs.getString("driver_license_number");
            }
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driverLicense;
    }

    public void displayActiveContracts() {
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement statement = null;
            String sql = "SELECT contractID, renterID, first_name, last_name, car_registration_number, " +
                                    "start_time, end_time, max_km, actual_km\n" +
                                    "FROM contract LEFT JOIN renter USING (renterID)\n" +
                                    "WHERE start_time >= curdate();";

            statement = myConn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                System.out.printf("%-15s %-15s %-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), sdf.format(rs.getDate(6)),
                        sdf.format(rs.getDate(7)), rs.getString(8), rs.getString(9));
            }
            statement.close();
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayOldContracts() {
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement statement = null;
            String sql = "SELECT contractID, renterID, first_name, last_name, car_registration_number, " +
                    "start_time, end_time, max_km, actual_km\n" +
                    "FROM contract LEFT JOIN renter USING (renterID)\n" +
                    "WHERE end_time < curdate() || actual_km != 0;";

            statement = myConn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                System.out.printf("%-15s %-15s %-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), sdf.format(rs.getDate(6)), sdf.format(rs.getDate(7)),
                        rs.getString(8), rs.getString(9));
            }
            statement.close();
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayContractsByStartDate(Date date) {
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement statement = null;
            String sql = "SELECT contractID, renterID, first_name, last_name, car_registration_number, " +
                    "start_time, end_time, max_km, actual_km\n" +
                    "FROM contract LEFT JOIN renter USING (renterID)\n" +
                    "WHERE start_time = ?;";

            statement = myConn.prepareStatement(sql);
            statement.setDate(1, date);
            ResultSet rs = statement.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                System.out.printf("%-15s %-15s %-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), sdf.format(rs.getDate(6)), sdf.format(rs.getDate(7)),
                        rs.getString(8), rs.getString(9));
            }
            statement.close();
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayContractsByEndDate(Date date) {
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement statement = null;
            String sql = "SELECT contractID, renterID, first_name, last_name, car_registration_number, " +
                    "start_time, end_time, max_km, actual_km\n" +
                    "FROM contract LEFT JOIN renter USING (renterID)\n" +
                    "WHERE end_time = ?;";

            statement = myConn.prepareStatement(sql);
            statement.setDate(1, date);
            ResultSet rs = statement.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                System.out.printf("%-15s %-15s %-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), sdf.format(rs.getString(6)), sdf.format(rs.getString(7)),
                        rs.getString(8), rs.getString(9));
            }
            statement.close();
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayContractsByEndDate(String registrationNumber) {
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement statement = null;
            String sql = "SELECT contractID, renterID, first_name, last_name, car_registration_number, " +
                    "start_time, end_time, max_km, actual_km\n" +
                    "FROM contract LEFT JOIN renter USING (renterID)\n" +
                    "WHERE car_registration_number = ?";

            statement = myConn.prepareStatement(sql);
            statement.setString(1, registrationNumber);
            ResultSet rs = statement.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                System.out.printf("%-15s %-15s %-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), sdf.format(rs.getDate(6)), sdf.format(rs.getDate(7)),
                        rs.getString(8), rs.getString(9));
            }
            statement.close();
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayRenters() {
        try {
            Connection myConn = getConnection(url, user, password);
            PreparedStatement statement = null;
            String sql = "SELECT renterID, first_name, last_name, mobile_phone_number, home_phone_number, email,\n" +
                            "\t\tdriver_license_number, since_data, street, building, floor, \n" +
                           "     door, zip, city, country.name\n" +
                         "FROM renter INNER JOIN phone_numbers USING (renterID)\n" +
                                     "INNER JOIN address USING (addressID)\n" +
                                     "INNER JOIN zip USING (zipID)\n" +
                                     "INNER JOIN country USING (countryID);";
            statement = myConn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                System.out.printf("%-15s %-15s %-25s %-25s %-25s %-25s %-25s %-15s %-25s\n", rs.getString(1),
                        rs.getString(2).substring(0,1).toUpperCase() + rs.getString(2).substring(1).toLowerCase(),
                        rs.getString(3).substring(0,1).toUpperCase() + rs.getString(3).substring(1).toLowerCase(),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                        sdf.format(rs.getDate(8)), rs.getString(9).substring(0,1).toUpperCase() +
                        rs.getString(9).substring(1).toLowerCase() + rs.getString(10) + " " +
                        rs.getString(11) + rs.getString(12) + rs.getString(13) + " " +
                        rs.getString(14).substring(0,1).toUpperCase() + rs.getString(14).substring(1).toLowerCase() + " " +
                        rs.getString(15).substring(0,1).toUpperCase() + rs.getString(15).substring(1).toLowerCase());
            }
            statement.close();
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
