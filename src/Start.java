import java.util.Scanner;

public class Start {
    Scanner scanner = new Scanner(System.in);

    public Start() throws Exception {
        //Filling up the contracts ArrayList from the DB
        OurApp.getController().initiateContractList();
        //Printing the Logo
        AsciiArt.printLogo4();

        System.out.println("*************************** ***************");
        System.out.println("|             KAILUA CAR RENTAL           |");
        System.out.println("|               LOGIN SCREEN              |");
        System.out.println("*******************************************\n");

        boolean ok = true;
        while (ok) {
            System.out.print("Please enter Username: ");
            String un = scanner.next();
            System.out.print("Please enter password: ");
            String pw = scanner.next();

            //Not sure if needed, but if needed then we'll have a method that populates the ArrayLists from our Database
            //Main.getController().initiateLists();

            if (un.equalsIgnoreCase("admin") && pw.equalsIgnoreCase("1234")) {
                System.out.println("\n");
                Thread.sleep(500);
                Menus menu = new Menus();
                menu.mainMenu();
                ok = false;
            } else {
                System.out.println("Wrong username and/or password. Please try again!\n\n");
            }
        }

    }
}
