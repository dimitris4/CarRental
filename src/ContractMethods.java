import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;

import static java.sql.DriverManager.getConnection;

public class ContractMethods {
    private static Database database = Database.instance;

    public void initiateContractList() throws SQLException {
        Connection myConn = dbConnect();
        Statement myStmt = myConn.createStatement();
        String query = "SELECT * " +
                       "FROM Contract co " +
                       "JOIN Car c ON co.car_registration_number = c.registration_number " +
                       "JOIN Renter r ON r.renterid = co.renterid " +
                       "JOIN Brand b ON b.brandID = c.brandID " +
                       "JOIN Model m ON b.brandID = m.brandID " +
                       "JOIN Rental_types rt ON rt.rental_typeID = c.rental_typeID ";

        ResultSet myRs = myStmt.executeQuery(query);
        while (myRs.next()) {
            Contract c1 = new Contract();
            Car car = new Car();
            Renter renter = new Renter();
            CarType type = new CarType();
            Model model = new Model();
            Brand brand = new Brand();

            //Parsing brand info into the car object
            brand.setBrandID(myRs.getInt("b.brandID"));
            brand.setName(myRs.getString("b.name"));
            car.setBrand(brand);

            //Parsing model info into the car object
            model.setModelID(myRs.getInt("m.modelID"));
            model.setName(myRs.getString("m.name"));
            car.setModel(model);

            //Parsing rental type info into the car object
            type.setName(myRs.getString("rt.name"));
            type.setDescription(myRs.getString("rt.description"));
            car.setType(type);

            //Parsing car info into the car object
            car.setRegistration_number(myRs.getString("c.registration_number"));
            car.setFirst_registration(myRs.getDate("c.first_registration"));
            car.setOdometer(myRs.getInt("c.odometer"));

            //Parsing renter info into the renter object
            renter.setFirst_name(myRs.getString("r.first_name"));
            renter.setLast_name(myRs.getString("r.last_name"));
            renter.setDriverLicenseNumber(myRs.getString("r.driver_license_number"));
            renter.setEmail(myRs.getString("r.email"));


            c1.setRenter(renter);
            c1.setContractID(myRs.getInt("co.contractID"));
            c1.setCar(car);
            Date start = myRs.getDate("co.start_time");
            c1.setStartDate(start);
            Date end = myRs.getDate("co.end_time");
            c1.setEndDate(end);
            database.getContracts().add(c1);
        }
        myConn.close();
    }

    public void displayActiveContracts(){

        System.out.printf("%-20s%-20s%-20s%-40s%-40s%-20s%-20s%-20s\n", "Contract ID","Rental Type", "Registration No", "Brand/Model", "Renter's Name", "Driver License No", "Start Date", "End Date");
        System.out.println("*******************************************************************************************************************************************************************************************");

        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());

        for (int i = 0; i < database.getContracts().size(); i++){
                if(database.getContracts().get(i).getEndDate().compareTo(date)>=0){
                    System.out.println(database.getContracts().get(i));
                }
        }
        System.out.println("===========================================================================================================================================================================================");
    }

    public void displayOldContracts() {
        System.out.printf("%-20s%-20s%-20s%-40s%-40s%-20s%-20s%-20s\n", "Contract ID","Rental Type", "Registration No", "Brand/Model", "Renter's Name", "Driver License No", "Start Date", "End Date");
        System.out.println("********************************************************************************************************************************************************************************************");

        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());

        for (int i = 0; i < database.getContracts().size(); i++){
            if(database.getContracts().get(i).getEndDate().compareTo(date)<0){
                System.out.println(database.getContracts().get(i));
            }
        }
        System.out.println("===========================================================================================================================================================================================");
    }

    public void searchContractsByStartDate() {

        Scanner input = new Scanner(System.in);
        System.out.println("Enter the start date of the contracts you want to see:");
        String myDate = input.next();
        Date date = Date.valueOf(myDate);
        System.out.printf("%-20s%-20s%-20s%-40s%-40s%-20s%-20s%-20s\n", "Contract ID","Rental Type", "Registration No", "Brand/Model", "Renter's Name", "Driver License No", "Start Date", "End Date");
        System.out.println("********************************************************************************************************************************************************************************************");

        for (int i = 0; i < database.getContracts().size(); i++){
            if(database.getContracts().get(i).getStartDate().compareTo(date)==0){
                System.out.println(database.getContracts().get(i));
            }
        }
        System.out.println("===========================================================================================================================================================================================");
    }

    public void searchContractsByEndDate() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the end date of the contracts you want to see:");
        String myDate = input.next();
        Date date = Date.valueOf(myDate);
        System.out.printf("%-20s%-20s%-20s%-40s%-40s%-20s%-20s%-20s\n", "Contract ID","Rental Type", "Registration No", "Brand/Model", "Renter's Name", "Driver License No", "Start Date", "End Date");
        System.out.println("********************************************************************************************************************************************************************************************");
        for (int i = 0; i < database.getContracts().size(); i++){
            if(database.getContracts().get(i).getEndDate().compareTo(date)==0){
                System.out.println(database.getContracts().get(i));
            }
        }
        System.out.println("===========================================================================================================================================================================================");
    }

    public void searchContractsByRegNo() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the registration number you want to find:");
        String regNo = input.next();

        System.out.printf("%-20s%-20s%-20s%-40s%-40s%-20s%-20s%-20s\n", "Contract ID","Rental Type", "Registration No", "Brand/Model", "Renter's Name", "Driver License No", "Start Date", "End Date");
        System.out.println("********************************************************************************************************************************************************************************************");
        boolean isFound = false;
        for (int i = 0; i < database.getContracts().size(); i++){
            if(database.getContracts().get(i).getCar().getRegistration_number().equalsIgnoreCase(regNo)){
                System.out.println(database.getContracts().get(i));
                isFound = true;
            }
        }
        if(!isFound){
            System.out.println("NO RECORDS FOUND");
        }
        System.out.println("===========================================================================================================================================================================================");
    }

    public void addContract() throws Exception {
        Scanner input = new Scanner(System.in);

        OurApp.getController().displayRenters();
        System.out.print("Enter renter ID: ");
        int id = input.nextInt();
        boolean isFound = false;
        while (!isFound) {
            isFound = database.getRenterIDs().contains(id);
            if (!isFound) {
                System.out.println("Wrong ID. Try again:");
                id = input.nextInt();
            }
        }

        HashSet<String> carRegNo = OurApp.getController().displayCars(" WHERE is_available = 1");
        System.out.println("Insert registration number of a car you want to set as unavailable: ");
        String registration_number = input.next();
        while (!carRegNo.contains(registration_number) || registration_number.equals(0)) {
            System.out.println("Wrong input. Enter 0 to go back or try again: ");
            registration_number = input.next();
        }

        System.out.print("Enter start date: ");
        String startDate = input.next();
        Date stDate = Date.valueOf(startDate);

        System.out.print("Enter end date: ");
        String endDate = input.next();
        Date eDate = Date.valueOf(endDate);

        System.out.print("Enter max Km: ");
        int maxKm = Input.checkInt(1, 500);

        int actualKm = 0;

        String sqlst = "INSERT INTO contract (contractID, renterID, car_registration_number, start_time, end_time, max_km, actual_km) VALUES (LAST_INSERT_ID(), ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = dbConnect().prepareStatement(sqlst);
        pst.setInt(1, id);
        pst.setString(2, registration_number);
        pst.setDate(3, stDate);
        pst.setDate(4, eDate);
        pst.setInt(5, maxKm);
        pst.setInt(6, actualKm);
        pst.executeUpdate();
        database.getContracts().clear();
        OurApp.getController().makeUnavailable2(registration_number);
        OurApp.getController().initiateContractList();

        for (Contract contract : database.getContracts()) {
            if (contract.getCar().getRegistration_number().equals(registration_number) && contract.getStartDate().equals(stDate)) {

                String myMessage = "This agreement is hereby made between " + contract.getRenter().getFirst_name() + " \n" +
                        contract.getRenter().getLast_name() + " (hereafter referred to as \"the Renter\") and {John Kailua} (hereafter referred to as \"the Owner\"). \n" +
                        "The Owner hereby agrees to rent the following vehicle to the Renter: {" + contract.getCar().getBrand().getName() + " " + contract.getCar().getModel().getName() + ", License Plate #" + contract.getCar().getRegistration_number() + "\n" +
                        "The Renter will rent the car from " + contract.getStartDate() + " to " + contract.getEndDate() + ".\n" +
                        "The Renter agrees to return the vehicle in its current condition (minus normal road wear-and-tear) to the Owner on the return date.\n" +
                        "The Renter understands that the vehicle is for use only in Copenhagen and cannot be taken to other locations.\n" +
                        "The Renter swears and attests that {he/she} has a legal, valid license to drive this type of vehicle in Denmark, and that there are no outstanding warrants against said license. \n" +
                        "The Renter's driver's license is: {" + contract.getRenter().getDriverLicenseNumber() + "}. The Renter further swears and attests that {he/she} has insurance that will cover the operation of this vehicle.\n" +
                        "The Renter agrees not to allow any other person to drive the vehicle, except for authorized drivers listed and approved here. \n" +
                        "The Renter agrees to use the vehicle only for routine, legal purposes (personal or business). The Renter further agrees to follow all city, state, county, and government rules and restrictions regarding use and operation of the vehicle.\n" +
                        "The Renter agrees to hold harmless, indemnify, and release the Owner for any damages, injuries, property loss, or death caused while the Renter operates this vehicle. The Renter will be held accountable for any damages or cleaning fees incurred while renting the vehicle.\n" +
                        "The Renter has had the opportunity to inspect the vehicle before the renting term begins and confirms that it is in good operable condition.\n" +
                        "The Owner swears and attests that the vehicle is in good working order and has no liens or encumbrances.\n" +
                        "                                                                                                        \n" +
                        "         " + contract.getRenter().getFirst_name() + " " + contract.getRenter().getLast_name() + "\n" +
                        "            (Renter Signature)                                                                          \n" +
                        "                                                                                                        \n" +
                        "         John Kailua                                                                                        \n" +
                        "     (Owner Signature)                                                                                   \n" +
                        "                                                                                                         \n" +
                        "      " + contract.getStartDate() + "                                                 \n" +
                        "    (Date)                                                                                              \n";
                OurApp.getController().sendMail(contract.getRenter().getEmail(), myMessage);
                break;

            }
        }
    }

    public void endContract() throws SQLException{
        OurApp.getController().displayActiveContracts();
        System.out.println("Select the contract you want to end:");
        Scanner input = new Scanner(System.in);
        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        int id = Input.checkInt(1, 5000);
        boolean isFound = false;
        while(!isFound){
            for(int i=0; i < database.getContracts().size(); i++) {
                if(database.getContracts().get(i).getContractID() == id && database.getContracts().get(i).getEndDate().compareTo(date)>= 0){
                    isFound = true;
                    // Get today as a Calendar
                    Calendar today = Calendar.getInstance();
                    // Subtract 1 day
                    today.add(Calendar.DATE, -1);
                    // Make an SQL Date out of that
                    java.sql.Date yesterday = new java.sql.Date(today.getTimeInMillis());
                    database.getContracts().get(i).setEndDate(yesterday);
                    System.out.println("Please enter actual kilometres:");
                    int actualKms = Input.checkInt(1, 1000);
                    int odometer = database.getContracts().get(i).getCar().getOdometer();
                    int totalKms = actualKms + odometer;
                    System.out.println("Total kms For this Car:" + totalKms);
                    String sqlst1 = "UPDATE contract SET end_time = ?, actual_km = ? WHERE contractID = ?";
                    String sqlst2 = "UPDATE car c, contract co SET c.is_available = 1, c.odometer = ? WHERE co.car_registration_number = c.registration_number AND co.contractID = ?";

                    PreparedStatement pst = dbConnect().prepareStatement(sqlst1);
                    PreparedStatement pst2 = dbConnect().prepareStatement(sqlst2);

                    pst.setDate(1, yesterday);
                    pst.setInt(2, actualKms);
                    pst.setInt(3, id);

                    pst2.setInt(1, totalKms);
                    pst2.setInt(2, id);

                    pst.executeUpdate();
                    pst2.executeUpdate();
                    dbConnect().close();
                }
            }
            if(!isFound){
                System.out.println("Wrong input. Contract not found. Try again:");
                id = Input.checkInt(1, 5000);
            }
        }
    }

    public void deleteContract() throws SQLException {
        displayOldContracts();
        System.out.println("You can only delete inactive contracts! Please select contract ID:");
        Scanner input = new Scanner(System.in);
        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        int id = Input.checkInt(1, 5000);
        boolean isFound = false;
        while (!isFound) {
            final int myID = id;
            //Lambda expression returns true if found
            isFound = database.getContracts().removeIf(n -> (n.getContractID() == myID) && (n.getEndDate().compareTo(date) < 0));
            if (isFound) {
                String delCont = "DELETE FROM contract WHERE renterID = ?";
                PreparedStatement pst = dbConnect().prepareStatement(delCont);
                pst.setInt(1, id);
                pst.executeUpdate();
                System.out.println("Delete complete.");
                dbConnect().close();
            } else {
                System.out.println("No such contract found! Try again:");
                id = Input.checkInt(1, 5000);
            }
        }
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
            @Override
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


}
