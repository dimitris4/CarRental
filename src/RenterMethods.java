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

    // i was testing the connection to the database...
    public void add() {

        try {
            // 1. get a connection to database
            Connection myConn = getConnection("jdbc:mysql://localhost:3306/ap", "root", "periergos77AS");

            // 2. create a statement
            Statement myStmt = myConn.createStatement();

            // 3. execute SQL query
            ResultSet myRs = myStmt.executeQuery("select * from invoices");

            // 4. process the result set
            while (myRs.next()) {
                System.out.println(myRs.getString("invoice_id"));
            }

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

