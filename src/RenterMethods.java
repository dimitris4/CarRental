import java.sql.*;
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


    public void remove() {

        try {

            Connection myConn = getConnection(url, user, password);

            if (!displayRentersRemove(myConn)) {

                return;

            } else {

                PreparedStatement pst1 = null;
                PreparedStatement pst2 = null;

                String sql1 = "DELETE FROM contract WHERE renterID = ?";
                String sql2 = "DELETE FROM renter WHERE renterID = ?";

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

            String sql = "SELECT renterID, first_name, last_name, mobile_phone_number, email, driver_license_number, since_data, CONCAT(street, building, floor, door, zip_code)\n" +
                    "FROM renter JOIN address USING (addressID)\n" +
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
            //PreparedStatement updateTelephone = null;
            //PreparedStatement updateAddress = null;

            displayRentersUpdate(myConn);

            System.out.print("\nSelect renter ID: ");
            int renter_id = input.nextInt();

            System.out.println("\n[1] Driver License Number     [2] Telephone     [3] Address");
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

                case 2:

                    /*try {

                        String updateTelephoneString = "";

                        updateTelephone = myConn.prepareStatement(updateTelephoneString);

                        updateTelephone.setInt(1, choice);

                        System.out.println("Update complete.");

                    } catch (SQLException e) {

                        e.printStackTrace();

                    }*/

                case 3:

                    /*try {

                        String updateAddressString = "";

                        updateAddress = myConn.prepareStatement(updateAddressString);

                        updateAddress.setInt(1, );

                        System.out.println("Update complete.");

                    } catch (SQLException e) {

                        e.printStackTrace();

                    }*/

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
                                     "JOIN phone_numbers USING (mobile_phone_number)";

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
}

