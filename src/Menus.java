import java.util.Scanner;

public class Menus {

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
                    AsciiArt.printLogo3();
                    System.exit(0);
                    break;
            }
        }
    }

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
            System.out.print("Select Option: ");
            int selection = Input.checkInt(0, 7);
            switch (selection) {
                case 1:
                    OurApp.getController().addCar();
                    pressAnyKey();
                    break;
                case 2:
                    OurApp.getController().editCar();
                    pressAnyKey();
                    break;
                case 3:
                    OurApp.getController().deleteCar();
                    pressAnyKey();
                    break;
                case 4:
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
                    AsciiArt.printLogo3();
                    System.exit(0);
                    break;
            }
        }
    }

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
            System.out.print("Select Option: ");
            int selection = Input.checkInt(0, 5);
            switch (selection) {
                case 1:
                    OurApp.getController().addRenter();
                    pressAnyKey();
                    break;
                case 2:
                    OurApp.getController().editRenter();
                    pressAnyKey();
                    break;
                case 3:
                    OurApp.getController().deleteRenter();
                    pressAnyKey();
                    break;
                case 4:
                    OurApp.getController().displayRenters();
                    pressAnyKey();
                    break;
                case 5:
                    System.out.println("\n");
                    Thread.sleep(300);
                    mainMenu();
                    break;
                case 0:
                    AsciiArt.printLogo3();
                    System.exit(0);
                    break;
            }
        }
    }

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
            System.out.print("Select Option: ");
            int selection = Input.checkInt(0, 5);
            switch (selection) {
                case 1:
                    OurApp.getController().addContract();
                    pressAnyKey();
                    break;
                case 2:
                    OurApp.getController().endContract();
                    pressAnyKey();
                    break;
                case 3:
                    OurApp.getController().deleteContract();
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
                    AsciiArt.printLogo3();
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
            System.out.print("Select Option: ");
            int selection = Input.checkInt(0, 7);
            switch (selection) {
                case 1:
                    OurApp.getController().displayActiveContracts();
                    pressAnyKey();
                    break;
                case 2:
                    OurApp.getController().displayOldContracts();
                    pressAnyKey();
                    break;
                case 3:
                    OurApp.getController().searchByStartDate();
                    pressAnyKey();
                    break;
                case 4:
                    OurApp.getController().searchByEndDate();
                    pressAnyKey();
                    break;
                case 5:
                    OurApp.getController().searchByRegNumber();
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
                    AsciiArt.printLogo3();
                    System.exit(0);
                    break;
            }
        }
    }

    public void pressAnyKey() {
        System.out.println("Press any key to continue: ");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
