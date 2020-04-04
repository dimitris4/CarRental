import java.sql.*;
import java.sql.Date;
import java.time.Instant;
import java.util.*;

import static java.sql.DriverManager.getConnection;


public class RenterMethods {

    Scanner input = new Scanner(System.in);
    Database database = new Database();
    ArrayList<Renter> renters = new ArrayList<>();
    private static Set<Integer> zipIDs= new HashSet<>();
    private static Set<String> zips= new HashSet<>();
    private static Set<String> countries= new HashSet<>();


    // the list and sets are initialized
    public RenterMethods() throws SQLException {
        renters = database.loadRenters();
        zipIDs = database.populateZipIdHashSet();
        zips = database.populateZipHashSet();
        countries = database.populateCountriesHashSet();
    }


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

        System.out.print("First name: ");
        String fname = input.next();
        while (!fname.matches("[a-zA-Z_]+")) {
            System.out.print("Invalid First Name. Try Again: ");
            fname = input.next();
        }
        fname = (fname.substring(0,1).toUpperCase() + fname.substring(1).toLowerCase());

        System.out.print("Last name: ");
        String lname = input.next();
        while(!lname.matches("[a-zA-Z_]+")){
            System.out.println("Invalid Last Name. Try Again: ");
            lname = input.next();
        }
        lname = (lname.substring(0,1).toUpperCase() + lname.substring(1).toLowerCase());

        System.out.print("Mobile Phone (start with country code): ");
        String mobilePhone = input.next();
        while(!mobilePhone.matches("[0-9]{6,12}$")){
            System.out.print("Invalid Phone number (minimum 6 digits, maximum 12). Try Again: ");
            mobilePhone = input.next();
        }
        input.nextLine();

        System.out.print("Home Phone (start with country code) or [0] to skip: ");
        String homePhone = input.nextLine();
        while(!homePhone.equals("0") && !homePhone.matches("[0-9]{6,12}$")){
            System.out.print("Invalid Phone number. Try Again: ");
            homePhone = input.nextLine();
        }
        if (homePhone.equals("0")){ homePhone = null; }

        String email = Input.checkEmail();

        System.out.print("Driver Licence Number: ");
        String licence = input.nextLine();

        //System.out.print("Driver since (please type the date) dd-mm-yyyy: ");
        java.util.Date sinceDate = Input.setDate();
        java.sql.Date sqlDate = new java.sql.Date(sinceDate.getTime());

        System.out.println("\n**** CLIENT ADDRESS ****\n");

        System.out.print("Street Name: ");
        String street = input.next();
        while (!street.matches("[a-zA-Z_]+")) {
            System.out.print("Invalid Street Name. Try Again: ");
            street = input.next();
        }

        street = (street.substring(0,1).toUpperCase() + street.substring(1).toLowerCase());

        System.out.print("Street Number: ");
        int building = Input.checkInt(1,5000);

        System.out.print("Floor: ");
        int floor = Input.checkInt(0,200);

        System.out.print("Door (th/tv/mf/-): ");
        String door = input.next();
        while(!door.matches("(^(th)?|(tv)?|(mf)?|(-)?(\\s)?$)")){
            System.out.println("Invalid Input. Try Again: ");
            door = input.next();
        }

        database.displayZip();
        System.out.println("\n[1] Select ZIP from the list     [2] Insert new one");
        System.out.print("Select the option: ");
        int field = Input.checkInt(1,2);
        String zip_code = "";
        int zipID;

        switch (field) {

            case 1: // known zip code

                System.out.print("Type selected ZIP code ID: ");
                zipID = Input.checkInt(1,5000);
                while (!(zipIDs.contains(zipID))) {
                    System.out.print("Invalid Input. Try Again: ");
                    zipID = Input.checkInt(1,5000);
                }

                // insert renter with known zip code (tables affected: renter, phone_numbers, address)
                database.addRenter(fname, lname, mobilePhone, homePhone, email, licence, sqlDate, street, building,
                                   floor, door, zipID);
                break;

            case 2: // unknown zip code...

                System.out.print("Type new zip code: ");
                zip_code = input.next();
                while (!zip_code.matches("[0-9]+")) {
                    System.out.print("Invalid Input. Try Again: ");
                    zip_code = input.next();
                }

                input.nextLine();

                System.out.print("City: ");
                String city = input.nextLine();
                while (!city.matches("[a-zA-Z_]+(\\s)?([a-zA-Z_]+)?")) {
                    System.out.println("Invalid City. Try Again: ");
                    city = input.nextLine();
                }

                String country = Input.isCountryName();

                if (!countries.contains(country)) { // if country is unknown

                    // insert renter with unknown zip code and unknown country (tables affected: renter, phone_numbers, address, zip, country)
                    database.addRenter(country, zip_code, city, street, building, floor, door, fname, lname, email,
                                       licence, sqlDate, mobilePhone, homePhone);

                } else { // if country is known, then we get country id

                    int countryID = database.getCountryID(country);

                    // insert renter with unknown zip code and known country (tables affected: renter, phone_numbers, address, zip)
                    database.addRenter(zip_code, city, street, building, floor, door, fname, lname, email, licence,
                                       sqlDate, mobilePhone, homePhone, countryID);

                }

                break;

            default:

                break;

        }

    }  // end of add method



    public void remove() throws SQLException {

        System.out.println("The program allows you to delete only renters who do not have any contract at the moment.");

        System.out.print("Select renter id: ");

        int max = database.checkRenterID();

        int renterID = Input.checkInt(1, max);

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

                System.out.print("Street Number: ");
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

                System.out.print("City: ");
                String city = input.nextLine();
                while(!city.matches("[a-zA-Z_]+(\\s)?([a-zA-Z_]+)?")){
                    System.out.println("Invalid City. Try Again: ");
                    city = input.nextLine();
                }

                System.out.print("Country: ");
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

