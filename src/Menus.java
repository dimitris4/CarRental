import java.util.Scanner;

public class Menus {

    Scanner scanner = new Scanner(System.in);

    public void mainMenu() throws Exception {

        while (true) {

            System.out.println("**********************************");
            System.out.println("|           MAIN MENU            |");
            System.out.println("**********************************");
            System.out.println("|   [1] Cars                     |");
            System.out.println("|   [2] Renters                  |");
            System.out.println("|   [3] Contracts                |");
            System.out.println("|   [0] Close Program            |");
            System.out.println("**********************************");

            RenterMethods rm = new RenterMethods();System.out.print("Select Option: ");

            int selection = Input.checkInt(0, 3);

            switch (selection) {
                case 1:
                    carMenu();
                    break;
                case 2:
                    renterMenu();
                    break;
                case 3:
                    contractMenu();
                    pressAnyKey();
                    break;
                case 0:
                    System.exit(0);
                    break;

            }
        }
    }


    //accessing the Car Menu
    public void carMenu() throws Exception {
        while (true) {
            System.out.println("**********************************");
            System.out.println("|             CARS               |");
            System.out.println("**********************************");
            System.out.println("|   [1] Add Car                  |");
            System.out.println("|   [2] Edit Car                 |");
            System.out.println("|   [3] Delete Car               |");
            System.out.println("|   [4] Display Cars             |");
            System.out.println("|   [5] Make Available           |");
            System.out.println("|   [6] Make Unavailable         |");
            System.out.println("|   [7] Return To Main Menu      |");
            System.out.println("|   [0] Close Program            |");
            System.out.println("**********************************");

            CarMethods cm = new CarMethods();

            System.out.print("Select Option: ");

            int selection = Input.checkInt(0, 7);

            switch (selection) {
                case 1:
                    OurApp.getController().addCar();
                    pressAnyKey();
                    break;
                case 2:
                    //OurApp.getController().editCar();
                    pressAnyKey();
                    break;
                case 3:
                    //OurApp.getController().deleteCar();
                    pressAnyKey();
                    break;
                case 4:
                    //String end = "2020-01-31";
                    //String start = "2020-02-03";
                    //DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                    //Date endDate = dateFormat.parse(end);
                    //Date startDate = dateFormat.parse(start);
                    //OurApp.getController().displayAvailableCarsWithinDateRange(startDate,endDate);
                    OurApp.getController().displayCars();
                    pressAnyKey();
                    break;
                case 5:
                    OurApp.getController().makeAvailable();
                    pressAnyKey();
                    break;
                case 6:
                    OurApp.getController().makeUnavailable();
                    pressAnyKey();
                    break;
                case 7:
                    System.out.println("\n");
                    Thread.sleep(300);
                    mainMenu();
                    break;
                case 0:
                    System.exit(0);
                    break;

            }
        }
    }


    //accessing the Renters Menu
    public void renterMenu() throws Exception {

        while (true) {

            System.out.println("**********************************");
            System.out.println("|            RENTERS             |");
            System.out.println("**********************************");
            System.out.println("|   [1] Add Renter               |");
            System.out.println("|   [2] Edit Renter              |");
            System.out.println("|   [3] Delete Renter            |");
            System.out.println("|   [4] Display Renters          |");
            System.out.println("|   [5] Return To Main Menu      |");
            System.out.println("|   [0] Close Program            |");
            System.out.println("**********************************");

            RenterMethods rm = new RenterMethods();

            System.out.print("Select Option: ");

            int selection = Input.checkInt(0, 5);

            switch (selection) {
                case 1:
                    //Main.getController().addRenter();
                    rm.add();
                    pressAnyKey();
                    break;
                case 2:
                    //Main.getController().editRenter();
                    rm.update();
                    pressAnyKey();
                    break;
                case 3:
                    //Main.getController().deleteRenter();
                    rm.remove();
                    pressAnyKey();
                    break;
                case 4:
                    //Main.getController().displayRenters();
                    rm.displayRenters();
                    pressAnyKey();
                    break;
                case 5:
                    System.out.println("\n");
                    Thread.sleep(300);
                    mainMenu();
                    break;
                case 0:
                    System.exit(0);
                    break;

            }
        }
    }

    //accessing the Contracts Menu
    public void contractMenu() throws Exception {

        while (true) {

            System.out.println("**********************************");
            System.out.println("|           CONTRACTS            |");
            System.out.println("**********************************");
            System.out.println("|   [1] Add Contract             |");
            System.out.println("|   [2] End Contract             |");
            System.out.println("|   [3] Delete Contract          |");
            System.out.println("|   [4] Display Contracts        |");
            System.out.println("|   [5] Return To Main Menu      |");
            System.out.println("|   [0] Close Program            |");
            System.out.println("**********************************");

            ContractMethods contractMethods = new ContractMethods();

            int selection = Input.checkInt(0, 5);

            switch (selection) {
                case 1:
                    contractMethods.addContract();
                    pressAnyKey();
                    break;
                case 2:
                    contractMethods.endContract();
                    pressAnyKey();
                    break;
                case 3:
                    contractMethods.deleteContract();
                    pressAnyKey();
                    break;
                case 4:
                    displayContractMenu();
                    break;
                case 5:
                    System.out.println("\n");
                    Thread.sleep(300);
                    mainMenu();
                    break;
                case 0:
                    System.exit(0);
                    break;

            }
        }
    }

    public void displayContractMenu() throws Exception {
        while (true) {
            System.out.println("***************************************");
            System.out.println("|           DISPLAY CONTRACTS         |");
            System.out.println("***************************************");
            System.out.println("|   [1] Display Active Contracts      |");
            System.out.println("|   [2] Display Old Contracts         |");
            System.out.println("|   [3] Search by Start Date          |");
            System.out.println("|   [4] Search by End Date            |");
            System.out.println("|   [5] Search by Reg. No             |");
            System.out.println("|   [6] Return To Contract Menu       |");
            System.out.println("|   [7] Return To Main Menu           |");
            System.out.println("|   [0] Close Program                 |");
            System.out.println("***************************************");
            ContractMethods contractMethods = new ContractMethods();
            int selection = Input.checkInt(0, 7);
            switch (selection) {
                case 1:
                    contractMethods.displayActiveContracts();
                    pressAnyKey();
                    break;
                case 2:
                    contractMethods.displayOldContracts();
                    pressAnyKey();
                    break;
                case 3:
                    contractMethods.searchContractsByStartDate();
                    pressAnyKey();
                    break;
                case 4:
                    contractMethods.searchContractsByEndDate();
                    pressAnyKey();
                    break;
                case 5:
                    contractMethods.searchContractsByRegNo();
                    pressAnyKey();
                    break;
                case 6:
                    System.out.println("\n");
                    Thread.sleep(300);
                    contractMenu();
                    break;
                case 7:
                    System.out.println("\n");
                    Thread.sleep(300);
                    mainMenu();
                    break;
                case 0:
                    System.exit(0);
                    break;
            }
        }
    }

    void pressAnyKey() {
        System.out.println("Press any key to continue: ");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

}
