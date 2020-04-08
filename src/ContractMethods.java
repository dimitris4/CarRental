//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static java.sql.DriverManager.getConnection;

public class ContractMethods {

    private static Database database = Database.instance;
    private static ArrayList<Contract> contracts;
    private static Scanner input = new Scanner(System.in);


    public ContractMethods() throws SQLException, ParseException {
        contracts = initiateContractList();
    }

    public ArrayList<Contract> initiateContractList() throws SQLException, ParseException {
        Connection myConn = dbConnect();
        Statement myStmt = myConn.createStatement();
        String query = "SELECT * " +
                       "FROM contract ";

        ResultSet myRs = myStmt.executeQuery(query);
        ArrayList<Contract> contracts = new ArrayList<>();
        while (myRs.next()) {
            int contractID = myRs.getInt("contractID");
            int renterID = myRs.getInt("renterID");
            String registrationNumber = myRs.getString("car_registration_number");
            java.util.Date start_time = new SimpleDateFormat("yyyy-MM-dd").parse(myRs.getString("start_time"));
            java.util.Date end_time = new SimpleDateFormat("yyyy-MM-dd").parse(myRs.getString("end_time"));
            int max_km = myRs.getInt("max_km");
            int actual_km = myRs.getInt("actual_km");
            contracts.add(new Contract(contractID, renterID, registrationNumber, start_time, end_time, max_km, actual_km));
        }
        myConn.close();
        return contracts;
    }

    public void addContract() throws Exception {
        RenterMethods renterMethods = new RenterMethods();
        renterMethods.displayRenters();

        System.out.print("\nSelect renter ID: ");
        int renter_id = Input.checkInt(1,999999999);
        while (!database.getRenterIDs().contains(renter_id)) {
            System.out.print("Invalid ID. Try again: ");
            renter_id = Input.checkInt(1,999999999);
        }

        System.out.print("Enter start date: ");
        java.util.Date start_time = Input.insertDateWithoutTime();
        java.sql.Date sqlDate = new java.sql.Date(start_time.getTime());

        System.out.print("Enter start date: ");
        java.util.Date end_time = Input.insertDateWithoutTime();
        java.sql.Date sqlDate2 = new java.sql.Date(end_time.getTime());

        System.out.println("Available cars from " + formatDate(start_time) + " to " + formatDate(end_time) + ":\n");
        database.displayAvailableCarsWithinDateRange(start_time, end_time);

        System.out.print("Select car registration number: ");
        String registration_number = input.next();
        while (!database.getCarRegistrationNumbers().contains(registration_number)) {
            System.out.print("Wrong input. Try again: ");
            registration_number = input.next();
        }

        System.out.print("Enter max Km: ");
        int maxKm = Input.checkInt(1, 10000);

        int actualKm = 0;

        String sqlst = "INSERT INTO contract (renterID, car_registration_number, start_time, end_time, max_km, actual_km) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = dbConnect().prepareStatement(sqlst);
        pst.setInt(1, renter_id);
        pst.setString(2, registration_number);
        pst.setDate(3, sqlDate);
        pst.setDate(4, sqlDate2);
        pst.setInt(5, maxKm);
        pst.setInt(6, actualKm);
        pst.executeUpdate();

        Contract contract = new Contract();
        contract.setRenterID(renter_id);
        contract.setRegistrationNumber(registration_number);
        contract.setStartDate(start_time);
        contract.setEndDate(end_time);
        contract.setMaxKm(maxKm);
        contract.setActualKm(actualKm);
        contracts.add(contract);
    }


    public Connection dbConnect(){
        Connection myConn = null;
        try {
            //get a connection to database
            myConn = getConnection("jdbc:mysql://localhost:3306/kailua", "dimk", "dimk1234!");
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return myConn;
    }

    public String formatDate(java.util.Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }


    public void displayActiveContracts(){
        System.out.println("ACTIVE CONTRACTS");
        System.out.println(String.format("%-25s %-25s %-25s %-25s %-25s", "Contract ID", "Start Date", "End Date",
                "Max Km", "Actual Km"));
        for (int i=0; i<125; i++) {
            System.out.print("*");
        }
        System.out.println();
        for (Contract contract : contracts) {
            if(contract.getEndDate().compareTo(new java.sql.Date(new java.util.Date().getTime())) >= 0){
                System.out.println(contract);
            }
        }
        for (int i=0; i<125; i++) {
            System.out.print("=");
        }
        System.out.println();
    }


    public void displayOldContracts() {
        System.out.println("OLD CONTRACTS");
        System.out.println(String.format("%-25s %-25s %-25s %-25s %-25s", "Contract ID", "Start Date", "End Date",
                                        "Max Km", "Actual Km"));
        for (int i=0; i<125; i++) {
            System.out.print("*");
        }
        System.out.println();
        for (Contract contract : contracts) {
            if(contract.getEndDate().compareTo(new java.sql.Date(new java.util.Date().getTime())) < 0){
                System.out.println(contract);
            }
        }
        for (int i=0; i<125; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    public void searchContractsByStartDate() {
        System.out.print("Enter the start date: ");
        Date date = Input.insertDateWithoutTime();
        System.out.println("SEARCH RESULTS");
        System.out.println(String.format("%-25s %-25s %-25s %-25s %-25s", "Contract ID", "Start Date", "End Date",
                                            "Max Km", "Actual Km"));
        for (int i=0; i<125; i++) {
            System.out.print("*");
        }
        System.out.println();
        for (Contract contract : contracts) {
            if(contract.getStartDate().compareTo(date) == 0){
                System.out.println(contract);
            }
        }
        for (int i=0; i<125; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    public void searchContractsByEndDate() {
        System.out.print("Enter the end date: ");
        Date date = Input.insertDateWithoutTime();
        System.out.println("SEARCH RESULTS");
        System.out.println(String.format("%-25s %-25s %-25s %-25s %-25s", "Contract ID", "Start Date", "End Date",
                "Max Km", "Actual Km"));
        for (int i=0; i<125; i++) {
            System.out.print("*");
        }
        System.out.println();
        for (Contract contract : contracts) {
            if(contract.getEndDate().compareTo(date) == 0){
                System.out.println(contract);
            }
        }
        for (int i=0; i<125; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    public void searchContractsByRegNo() throws SQLException {
        System.out.print("Enter the car's registration number: ");
        String string = input.next();
        while (!database.getCarRegistrationNumbers().contains(string)) {
            System.out.print("Invalid registration number. Try again: ");
            string = input.next();
        }
        System.out.println("SEARCH RESULTS");
        System.out.println(String.format("%-25s %-25s %-25s %-25s %-25s", "Contract ID", "Start Date", "End Date",
                "Max Km", "Actual Km"));
        for (int i=0; i<125; i++) {
            System.out.print("*");
        }
        System.out.println();
        for (Contract contract : contracts) {
            if(contract.getRegistrationNumber().equals(string)){
                System.out.println(contract);
            }
        }
        for (int i=0; i<125; i++) {
            System.out.print("=");
        }
        System.out.println();
    }


    public void endContract() throws SQLException {
        displayActiveContracts();

        System.out.print("Select contract ID: ");
        int contractID = Input.checkInt(1, 999999999);
        while (!database.getContractIDs().contains(contractID)) {
            System.out.print("Invalid input. Try again: ");
            contractID = Input.checkInt(1, 999999999);
        }

        for(Contract contract : contracts) {
            if (contract.getContractID() == contractID) {

                System.out.print("Enter actual km: ");
                int actual_km = Input.checkInt(0, 999999999);

                Date today = new Date();
                contract.setEndDate(today);
                java.sql.Date sqlDate = new java.sql.Date(today.getTime());

                database.updateContract(actual_km, sqlDate, contractID);
            }
        }
    }


    public void deleteContract() throws SQLException {
        displayOldContracts();
        System.out.println("You can only delete old contracts! Please select contract ID:");
        int contractID = Input.checkInt(1, 999999999);
        while (!database.getContractIDs().contains(contractID)) {
            System.out.print("Invalid contract ID. Try again: ");
            contractID = Input.checkInt(1, 999999999);
        }
        for (Contract contract : contracts) {
            if (contract.getContractID() == contractID && contract.getEndDate().compareTo(new Date()) < 0) {
                database.removeContract(contractID);
                contracts.remove(contract);
                return;
            }
        }
        System.out.println("\nThe selected contract cannot be deleted.");
    }



    /*public static void sendMail(String recipient, String myMessage) throws Exception{
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
            message.setSubject("CAR RENTAL AGREEMENT");
            message.setText(myMessage);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


*/

}
