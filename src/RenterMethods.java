import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    }

    /*public void remove() {

        try {

            Connection myConn = getConnection(url, user, password);

            //displayRenters(myConn);

            Statement myStmt = myConn.createStatement();

            String sql = "DELETE FROM renter WHERE renter_id = 1";

            int rowsAffected = myStmt.executeUpdate(sql);

            System.out.println("Rows affected: " + rowsAffected);

            System.out.println("Delete complete.");

            myStmt.close();

            myConn.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }*/

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

