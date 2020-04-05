import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Scanner;

class Input {

    static Scanner in = new Scanner(System.in);
    private static ArrayList<Integer>months = new ArrayList<>(Arrays.asList(31,28,31,30,31,30,31,31,30,31,30,31));


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
                System.out.print("Date must have the following format (dd-mm-yyyy): ");
                str = console.nextLine();
                continue;
            }
        }
        return date;
    }



    public static Date setDate(){
        System.out.println("Driver since (please type the date): ");
        System.out.print("Year (yyyy): ");
        // we don't rent cars to drivers who got their licence before 1940 ;)
        // can't be higher than current year
        int year = checkInt(1940, Calendar.getInstance().get(Calendar.YEAR));
        System.out.print("Month (numeric): ");
        int month = checkInt(1, 12);
        while(year == Calendar.getInstance().get(Calendar.YEAR) && month>Calendar.getInstance().get(Calendar.MONTH)){
            System.out.print("Invalid month value. Latest possible: " + Calendar.getInstance().get(Calendar.MONTH) +"."+ Calendar.getInstance().get(Calendar.YEAR) +": ");
            month = in.nextInt();
        }
        System.out.print("Day (numeric): ");
        int day = checkInt(1, 31);
        while(checkDate(year,month,day)){
            int leap=0;
            if (year%4==0 && month==2){
                leap++;
            }
            System.out.print("Invalid day value. Please insert value between 1 - " + (months.get(month-1)+leap) + ": ");
            day = in.nextInt();
            while (year == Calendar.getInstance().get(Calendar.YEAR) && month>Calendar.getInstance().get(Calendar.MONTH) && day>Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
                System.out.print("Invalid day value. Latest possible: " + Calendar.getInstance().get(Calendar.MONTH) +"."+ Calendar.getInstance().get(Calendar.YEAR) +"."+
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH) +": ");
                day = in.nextInt();
            }
        }
        String date = year + "-" + month + "-" + day;
        Date sdate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while (true) {
            try {
                sdate = sdf.parse(date);
                break;
            } catch (ParseException e) {
                System.out.print("Date doesn't exist.");
            }
        }
        return sdate;
    }



    public static boolean checkDate(int year, int month, int day){
        return (day<0 || ( month==2 && year%4==0 && day>29) || (((month==2 && year%4!=0) || (month!=2)) && day>months.get(month-1)));
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
                    System.out.print("Invalid input. Please try again: ");
                    input = "";
                }
            } else {
                isValid = false;
                in.next();
                System.out.print("Invalid input. Please try again: ");

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


    public static String isCountryName() {

        String[] countryCodes = Locale.getISOCountries();

        ArrayList<String> countryNames = new ArrayList<String>();

        for (String countryCode : countryCodes) {

            Locale obj = new Locale("", countryCode);

            countryNames.add(obj.getDisplayCountry().toLowerCase());

        }

        System.out.print("Country: ");
        String string = in.next();

        while (!countryNames.contains(string.toLowerCase())){
            System.out.print("This country doesn't exist. Please try again: ");
            string = in.next();
        }

        return string;

    }

    public static boolean isCountryName(String country) {

        String[] countryCodes = Locale.getISOCountries();

        ArrayList<String> countryNames = new ArrayList<String>();

        for (String countryCode : countryCodes) {

            Locale obj = new Locale("", countryCode);

            countryNames.add(obj.getDisplayCountry().toLowerCase());

        }

        return  countryNames.contains(country.toLowerCase());
    }


}
