import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Set;

public class RenterMethods {

    private static Scanner input = new Scanner(System.in);
    private static Database database = Database.instance;
    private static ArrayList<Renter> renters;
    private static Set<String> zips;
    private static Set<String> countries;

    // the list and sets are initialized
    public RenterMethods() throws SQLException {
        renters = database.loadRenters();
        zips = database.loadZips();
        countries = database.loadCountries();
    }

    public void displayRenters() {
        System.out.println();
        System.out.println("RENTERS");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-25s\n", "Renter ID", "First Name", "Last Name",
                "Mobile Phone", "Home Phone", "Email", "Driver License", "Since", "Address (Street, Number, Floor, Door, Zip, City, Country)");
        for (int i=0; i<235; i++) {
            System.out.print("*");
        }
        System.out.println();
        database.displayRenters();
        for (int i=0; i<235; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    public void displayZip() {
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(zips);
        Collections.sort(list);

        System.out.println("\nZip Codes in our Database");
        for (int i = 0; i < 25; i++) {
            System.out.print("-");
        }
        System.out.println();
        for (String zip : list) {
            System.out.println(zip);
        }
    }

    public void add() {
        Renter renter = new Renter();
        //prompt for the user input
        System.out.print("First name: ");   // first name can be only one word
        String fname = input.next();
        while (!fname.matches("[a-zA-Z_]+")) {
            System.out.print("Invalid First Name. Try Again: ");
            fname = input.next();
        }
        fname = Input.capitalizeWord(fname);  // converts the first letter to uppercase
        renter.setFirst_name(fname);

        System.out.print("Last name: ");
        String lname = input.next();
        while(!lname.matches("[a-zA-Z_]+")) {  // last name can be only one word
            System.out.println("Invalid Last Name. Try Again: ");
            lname = input.next();
        }
        lname = Input.capitalizeWord(lname);
        renter.setLast_name(lname);

        System.out.print("Mobile Phone (start with country code): ");
        String mobilePhone = input.next();
        while(!mobilePhone.matches("[0-9]{6,12}$")){
            System.out.print("Invalid Phone number (minimum 6 digits, maximum 12). Try Again: ");
            mobilePhone = input.next();
        }
        input.nextLine();

        System.out.print("Home Phone (start with country code) or [0] to skip: ");
        String homePhone = input.next();
        while(!homePhone.equals("0") && !homePhone.matches("[0-9]{6,12}$")){
            System.out.print("Invalid Phone number. Try Again: ");
            homePhone = input.next();
        }
        if (homePhone.equals("0")){ homePhone = null; }
        Telephone tel = new Telephone(mobilePhone, homePhone);
        renter.setTelephones(tel);

        String email = "";
        email = Input.checkEmail();
        renter.setEmail(email);

        System.out.print("Driver Licence Number: ");
        String licence = "";
        licence = input.next();
        renter.setDriverLicenseNumber(licence);

        // the object gets java Date and the database gets sql Date type
        java.util.Date sinceDate = Input.setDate();
        renter.setSinceDate(sinceDate);
        java.sql.Date sqlDate = new java.sql.Date(sinceDate.getTime());

        System.out.println("\n**** CLIENT ADDRESS ****\n");
        System.out.print("Street Name: ");
        String street = input.next();
        while (!street.matches("[a-zA-Z_]+")) {
            System.out.print("Invalid Street Name. Try Again: ");
            street = input.next();
        }
        street = Input.capitalizeWord(street);

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

        displayZip();
        System.out.println("\n[1] Select ZIP from the list     [2] Insert new one");
        System.out.print("Select the option: ");
        int field = Input.checkInt(1,2);
        String zip_code = "";
        String city = "";
        String country = "";

        switch (field) {

            case 1: // known zip code...

                System.out.print("Type selected ZIP: ");
                zip_code = input.next();
                while (!zip_code.matches("[0-9]+") || !zips.contains(zip_code)) {
                    System.out.print("Invalid Input. Try Again: ");
                    zip_code = input.next();
                }

                int zipID = database.getZipID(zip_code);

                ArrayList<String> arrayList = database.getCityCountryName(zip_code);
                city = arrayList.get(0);
                country = arrayList.get(1);

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
                city = input.nextLine();
                while (!city.matches("[a-zA-Z_]+(\\s)?([a-zA-Z_]+)?")) {
                    System.out.println("Invalid City. Try Again: ");
                    city = input.nextLine();
                }
                city = Input.capitalizeWord(city);
                country = Input.isCountryName();
                country = Input.capitalizeWord(country);

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
        Address address = new Address(street, building, floor, door, zip_code, city, country);
        renter.setAddresses(address);
        renters.add(renter);
    }


    public void remove() throws SQLException {
        displayRenters();
        System.out.println("\n*The program allows you to delete only renters who do not have any active contract at the moment.*");
        System.out.print("Select renter ID: ");
        int renter_id = Input.checkInt(1,999999999);
        ArrayList<Integer> a = database.getRenterIDs();
        while (!a.contains(renter_id)) {
            System.out.print("Invalid ID. Try again: ");
            renter_id = Input.checkInt(1,999999999);
        }

        if (database.findRemovableRenters().size() == 0) {
            System.out.println("All renters have contracts. You cannot delete any.");
            return;
        }

        if (database.findRemovableRenters().contains(renter_id)) {
            database.removeRenter(renter_id);
        } else {
            System.out.println("The renter you selected cannot be deleted.");
        }
    }

    public void update() throws SQLException {
        displayRenters();
        System.out.print("\nSelect renter ID: ");
        int renter_id = Input.checkInt(1,999999999);
        while (!database.getRenterIDs().contains(renter_id)) {
            System.out.print("Invalid ID. Try again: ");
            renter_id = Input.checkInt(1,999999999);
        }
        System.out.println("\n[1] Driver License Number     [2] Mobile Phone     [3] Home Phone     [4] Address");
        System.out.print("Select the field you want to update: ");
        int field = Input.checkInt(1,4);
        switch (field) {
            case 1:
                System.out.print("Enter new driver license number: ");
                String newDriverLicenseNumber = input.next();
                database.updateLicense(newDriverLicenseNumber, renter_id);
                break;
            case 2:
                System.out.print("Enter new mobile phone number: ");
                String newMobilePhone = input.next();
                database.updateMobilePhone(newMobilePhone, renter_id);
                break;
            case 3:
                System.out.print("Enter new home phone number: ");
                String newHomePhone = input.next();
                database.updateHomePhone(newHomePhone, renter_id);
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
                    System.out.print("Invalid input. Try Again: ");
                    door = input.next();
                }
                // update the address table

                displayZip();
                System.out.println("\n[1] Select ZIP from the list     [2] Insert new one");
                System.out.print("Select the option: ");
                int choice = Input.checkInt(1,2);
                String zip_code = "";
                String city = "";
                String country = "";

                if (choice == 1) {  // known zip code...
                    System.out.print("Type ZIP: ");
                    zip_code = input.next();
                    while (!zips.contains(zip_code)) {
                        System.out.print("Invalid Input. Try Again: ");
                        zip_code = input.next();
                    }
                    int zipID = database.getZipID(zip_code);
                    // update the address table only
                    database.updateAddress(street, building, floor, door, zipID, renter_id);
                } else {
                    // unknown zip code...
                    System.out.print("Type new zip code: ");
                    zip_code = input.next();
                    while (!zip_code.matches("[0-9]+")) {
                        System.out.print("Invalid Input. Try Again: ");
                        zip_code = input.next();
                    }
                    input.nextLine();
                    System.out.print("City: ");
                    city = input.nextLine();
                    while (!city.matches("[a-zA-Z_]+(\\s)?([a-zA-Z_]+)?")) {
                        System.out.println("Invalid City. Try Again: ");
                        city = input.nextLine();
                    }
                    country = Input.isCountryName();
                    if (!countries.contains(country)) { // if country is unknown
                        // insert new country to the countries table
                        database.addCountry(country);
                        // get country id
                        int countryID = database.getCountryID(country);
                        // insert renter with unknown zip code and unknown country (tables affected: renter, address, zip, country)
                        database.updateAddressZipCountry(street, building, floor, door, zip_code, city, countryID, renter_id);
                    } else { // if country is known, then we get country id
                        int countryID = database.getCountryID(country);
                        // insert renter with unknown zip code and known country (tables affected: renter, address, zip)
                        database.updateAddressZip(street, building, floor, door, zip_code, city, countryID, renter_id);
                    }
                }
            break;
        }
    }
}

