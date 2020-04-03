import java.sql.*;
import java.util.Calendar;
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


    public void remove() {

    }

    public void updateDriverLicenceNumber(){
    }

    public void updateTelephone() {

    }

    public void updateAddress() {

    }

    public void searchByDriverLicenseNumber(String driverLicenseNumber) {

    }

    public void searchByRenterLastName(String lastName) {
    }

    /*public void sendMail(String recipient, String myMessage) {
        public static void sendMail(String recipient, String myMessage) throws Exception{
            System.out.println("Preparing to send contract to client...");
            Properties properties = new Properties();

            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            String myAccountEmail = "kailuacarrental@gmail.com";
            String password = "kailua1234";

            Session session = Session.getInstance(properties, new Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(myAccountEmail, password);
                }
            });

            Message message = prepareMessage(session, myAccountEmail, recipient, myMessage);
            Transport.send(message);
            System.out.println("Contract sent!");
        }

        private static Message prepareMessage(Session session, String myAccountEmail, String recipient, String myMessage) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(myAccountEmail));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient) );
                message.setSubject("Kailua Car Rental Contract");
                message.setText(myMessage);
                return message;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }*/
}

