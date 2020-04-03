import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

class Input {

    static Scanner in = new Scanner(System.in);

    public static Date insertDateWithoutTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Scanner console = new Scanner(System.in);
        String str = console.nextLine();
        Date date;
        while (true) {
            try {
                date = sdf.parse(str);
                sdf = new SimpleDateFormat("dd-MM-yyyy");
                break;
            } catch (ParseException e) {
                System.out.print("Day must have the following format (dd-mm-yyyy): ");
                str = console.nextLine();
                continue;
            }
        }
        return date;
    }

    public static int checkInt(int min, int max) {

        boolean isValid = false;
        String input = "";
        while (!isValid) {
            isValid = true;
            if (in.hasNextInt()) {
                input = in.next().trim();
                if (Integer.parseInt(input) > min - 1 && Integer.parseInt(input) < max + 1) {
                    isValid = true;
                } else {
                    isValid = false;
                    System.out.print("The number isn't in our system, try another one: ");
                    input = "";
                }
            } else {
                isValid = false;
                in.next();
                System.out.print("only numbers please, try again: ");
            }
        }
        in.nextLine();
        return Integer.parseInt(input);
    }

    public static String checkEmail() {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        System.out.print("Email: ");
        String email = in.next();
        while (!email.matches(regex)) {
            System.out.print("Invalid email. Please try again: ");
            email = in.next();
        }
        return email;
    }
    
}
