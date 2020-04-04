import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public class RenterMethods {

    Scanner input = new Scanner(System.in);
    Database database = new Database();
    ArrayList<Renter> renters = database.loadRenters();

    public RenterMethods() throws SQLException {
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

        String email = Input.checkEmail();

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
        while(!Input.isCountryName(country)){
            System.out.print("Invalid Country. Try Again: ");
            country = console.next();
        }

        database.addRenter(country, zip_code, city, street, building, floor, door, fname, lname, email, licence,
                           sinceDate, mobilePhone, homePhone);
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

                System.out.print("Enter Country: ");
                String country = input.next();
                while(!Input.isCountryName(country)) {
                    System.out.println("Invalid country name. Try again: ");
                    country = input.next();
                }
                database.updateAddress(street, building, floor, door, renter_id, zip_code, city, country);
                //displayRenters();
                break;
        }
        //displayRenters();

    }

}

