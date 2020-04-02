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

    // i was testing the connection to the database...
    /*
    public void add() {

        try {
            // 1. get a connection to database
            Connection myConn = getConnection("jdbc:mysql://localhost:3306/kailua", "dimk", "dimk1234!");

            // 2. create a statement
            Statement myStmt = myConn.createStatement();


            // 3. execute SQL query
            ResultSet myRs = myStmt.executeQuery("select * from brand");


            // 4. process the result set
            while (myRs.next()) {
                System.out.println(myRs.getString("name"));
            }

        } catch (SQLException exc) {
            exc.printStackTrace();
        }

    }*/



    public void add() {

        // prompt for the user input
        Scanner console = new Scanner(System.in);
        System.out.print("First name: ");
        String fname = console.next();
        while(!fname.matches("[a-zA-Z_]+")){
            System.out.print("Invalid First Name. Try Again: ");
            fname = console.next();
        }
        System.out.print("Last name: ");
        String lname = console.next();
        while(!lname.matches("[a-zA-Z_]+")){
            System.out.println("Invalid Last Name. Try Again: ");
            lname = console.next();
        }
        console.nextLine();
        System.out.print("Phone number (start with country code): ");
        String phone = console.nextLine();
        while(!phone.matches("[0-9]+")){
            System.out.print("Invalid Phone number. Try Again: ");
            phone = console.nextLine();
        }
        System.out.print("E-mail: ");
        String email = console.next();
        System.out.print("Driver Licence Number: ");
        String licence = console.next();
        System.out.print("Driver since (please type the date) yyyy-mm-dd: ");
        String sinceDate = console.next();
        while(!sinceDate.matches("(^(\\d){4}-(\\d){2}-(\\d){2}$)")){
            System.out.print("Invalid date. Try Again: ");
            sinceDate = console.next();
        }
        java.sql.Date sDate = Date.valueOf(sinceDate);
        console.nextLine();

        System.out.println("\n**** CLIENT ADDRESS ****\n");
        System.out.println("Enter Street Name: ");
        String street = console.nextLine();
        while(!street.matches("[a-zA-Z_]+")){
            System.out.println("Invalid Street Name. Try Again: ");
            street = console.nextLine();
        }
        System.out.print("Enter Street Number: ");
        int building = console.nextInt();
        System.out.print("Floor: ");
        int floor = console.nextInt();
        System.out.println("Door (th/tv/mf/-): ");
        String door = console.next();
        while(!door.matches("(^(th)?|(tv)?|(mf)?|(-)?(\\s)?$)")){
            System.out.println("Invalid Input. Try Again: ");
            door = console.next();
        }
        System.out.print("Zip code: ");
        String zip = console.next();
        while(!zip.matches("[0-9]+")){
            System.out.print("Invalid Input. Try Again: ");
            zip= console.next();
        }
        console.nextLine();
        System.out.println("Enter City: ");
        String city = console.nextLine();
        while(!city.matches("[a-zA-Z_]+(\\s)?([a-zA-Z_]+)?")){
            System.out.println("Invalid City. Try Again: ");
            city = console.nextLine();
        }
        System.out.println("Enter Country: ");
        String country = console.next();
        while(!country.matches("[a-zA-Z_]+(\\s)?([a-zA-Z_]+)?")){
            System.out.println("Invalid Country. Try Again: ");
            country = console.next();
        }



        try {
            //get a connection to database
            Connection myConn = getConnection(url, user, password);

            //create insert statements
            String queryCity = "INSERT INTO city (city_name) VALUES (?)";
            String queryZip = "INSERT INTO zip (zip_code, cityID) VALUES (?, ?)";
            String queryAddress = "INSERT INTO address (street, building, floor, door, zip_code) VALUES (?, ?, ?, ?, ?);";
            String queryPhone = "INSERT INTO phone_numbers (mobile_phone_number)" + " VALUES (?)";
            String queryRenter = "INSERT INTO renter (first_name, last_name, mobile_phone_number, email, driver_license_number, since_data, addressID)" + " VALUES (?, ?, ?, ?, ?, ?, ?)";

            //create insert PreparedStatement
            PreparedStatement preparedStmt = myConn.prepareStatement(queryCity);
            preparedStmt.setString (1, city);
            preparedStmt.execute();


            preparedStmt = myConn.prepareStatement(queryZip);
            preparedStmt.setString (1, zip);
            preparedStmt.setInt    (2, 1);
            preparedStmt.execute();


            preparedStmt = myConn.prepareStatement(queryAddress);
            preparedStmt.setString (1, street);
            preparedStmt.setInt (2, building);
            preparedStmt.setInt (3, floor);
            preparedStmt.setString (4, door);
            preparedStmt.setString (5, zip);
            preparedStmt.execute();


            preparedStmt = myConn.prepareStatement(queryPhone);
            preparedStmt.setString (1, phone);
            preparedStmt.execute();


            preparedStmt = myConn.prepareStatement(queryRenter);
            preparedStmt.setString (1, fname);
            preparedStmt.setString (2, lname);
            preparedStmt.setString (3, phone);
            preparedStmt.setString (4, email);
            preparedStmt.setString (5, licence);
            preparedStmt.setDate   (6, sDate);
            preparedStmt.setInt    (7, 1);
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

    public void displayRenters() {

        try {

            Connection myConn = getConnection(url, user, password);

            Statement myStmt = myConn.createStatement();

            String sql = "SELECT first_name, last_name, mobile_phone_number, email, driver_license_number, since_data, " +
                    "CONCAT(street, \" \", building, \" \", floor, \" \", door, \" \", zip_code)\n" +
                    "FROM renter JOIN address USING (addressID)";

            ResultSet rs = myStmt.executeQuery(sql);

            if (rs != null) {

                System.out.printf("%-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", "First Name", "Last Name",
                        "Mobile Phone", "Email", "Driver License", "Since", "Address");

                for (int i = 0; i < 210; i++) {

                    System.out.print("-");

                }

                System.out.println();

                while (rs.next()) {

                    System.out.printf("%-25s %-25s %-25s %-25s %-25s %-25s %-25s\n", rs.getString(1),
                            rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getString(5), rs.getString(6), rs.getString(7));

                }
            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
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

