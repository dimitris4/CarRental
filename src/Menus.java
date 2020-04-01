import java.util.Scanner;

public class Menus {

    Scanner scanner = new Scanner(System.in);

    //accessing the Main Menu
    public void mainMenu() throws Exception {

        //a delay time is added after certain operations
        System.out.println("***************************");
        System.out.println("|        ΜΑΙΝ MENU        |");
        System.out.println("***************************");
        System.out.println("|    Select Operation:    |");
        System.out.println("|   [1] Cars              |");
        System.out.println("|   [2] Renters           |");
        System.out.println("|   [3] Contracts         |");
        System.out.println("|   [0] Close Program     |");
        System.out.println("***************************\n");

        boolean isWorking = true;
        while (isWorking) {
            System.out.print("Select Option: ");
            String answer = scanner.next();
            Thread.sleep(300);
            switch (answer) {
                case "1":
                    carMenu();
                    break;
                case "2":
                    rentersMenu();
                    break;
                case "3":
                    contractsMenu();
                    break;
                case "0":
                    System.out.println("System closing");
                    Thread.sleep(1000);
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choice must be a value between \"0\" and \"3\".\n");
                    break;
            }
        }
    }


    //accessing the Car Menu
    public void carMenu() throws Exception {
        System.out.println("**********************************");
        System.out.println("|             CARS               |");
        System.out.println("**********************************");
        System.out.println("|   [1] Add Car                  |");
        System.out.println("|   [2] Edit Car                 |");
        System.out.println("|   [3] Delete Car               |");
        System.out.println("|   [4] Make Car Unavailable     |");
        System.out.println("|   [5] Display Cars             |");
        System.out.println("|   [6] Return To Previous Menu  |");
        System.out.println("|   [0] Close Program            |");
        System.out.println("**********************************");

        boolean isTrue = true;

        while (isTrue) {
            System.out.print("Select Option: ");

            String selection = scanner.next();

            switch (selection) {
                case "1":
                    //Main.getController().addCar();
                    carMenu();
                    break;
                case "2":
                    //Main.getController().editCar();
                    carMenu();
                    break;
                case "3":
                    //Main.getController().deleteCar();
                    carMenu();
                    break;
                case "4":
                    //Main.getController().makeUnav();
                    carMenu();
                    break;
                case "5":
                    //Main.getController().displayCars();
                    carMenu();
                    break;
                case "6":
                    System.out.println("\n");
                    Thread.sleep(300);
                    mainMenu();
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choice must be a value between \"0\" and \"6\".\n");
                    break;
            }
        }
    }

    //accessing the Renters Menu
    public void rentersMenu() throws Exception {
        System.out.println("**********************************");
        System.out.println("|            RENTERS             |");
        System.out.println("**********************************");
        System.out.println("|   [1] Add Renter               |");
        System.out.println("|   [2] Edit Renter              |");
        System.out.println("|   [3] Delete Renter            |");
        System.out.println("|   [4] Display Renters          |");
        System.out.println("|   [5] Return To Previous Menu  |");
        System.out.println("|   [0] Close Program            |");
        System.out.println("**********************************");

        boolean isTrue = true;

        while (isTrue) {
            System.out.print("Select Option: ");

            String selection = scanner.next();

            switch (selection) {
                case "1":
                    //Main.getController().addRenter();
                    rentersMenu();
                    break;
                case "2":
                    //Main.getController().editRenter();
                    rentersMenu();
                    break;
                case "3":
                    //Main.getController().deleteRenter();
                    rentersMenu();
                    break;
                case "4":
                    //Main.getController().displayRenters();
                    rentersMenu();
                    break;
                case "5":
                    System.out.println("\n");
                    Thread.sleep(300);
                    mainMenu();
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choice must be a value between \"0\" and \"5\".\n");
                    break;
            }
        }
    }

    //accessing the Contracts Menu
    public void contractsMenu() throws Exception {
        System.out.println("**********************************");
        System.out.println("|           CONTRACTS            |");
        System.out.println("**********************************");
        System.out.println("|   [1] New Contract             |");
        System.out.println("|   [2] End Contract             |");
        System.out.println("|   [3] Edit Contract            |");
        System.out.println("|   [4] Display Contracts        |");
        System.out.println("|   [5] Return To Previous Menu  |");
        System.out.println("|   [0] Close Program            |");
        System.out.println("**********************************");

        boolean isTrue = true;

        while (isTrue) {
            System.out.print("Select Option: ");

            String selection = scanner.next();

            switch (selection) {
                case "1":
                    //Main.getController().newContract();
                    contractsMenu();
                    break;
                case "2":
                    //Main.getController().endContract();
                    contractsMenu();
                    break;
                case "3":
                    //Main.getController().editContract();
                    contractsMenu();
                    break;
                case "4":
                    //Main.getController().displayContracts();
                    contractsMenu();
                    break;
                case "5":
                    System.out.println("\n");
                    Thread.sleep(300);
                    mainMenu();
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choice must be a value between \"0\" and \"5\".\n");
                    break;
            }
        }
    }

}
