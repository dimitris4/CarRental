public class Telephone {

    private String mobile_phone_number;
    private String home_phone_number;

    public Telephone(String mobile_phone_number, String home_phone_number) {
        this.mobile_phone_number = mobile_phone_number;
        this.home_phone_number = home_phone_number;
    }

    public String getMobile_phone_number() {
        return mobile_phone_number;
    }

    public String getHome_phone_number() {
        return home_phone_number;
    }

    public void setMobile_phone_number(String mobile_phone_number) {
        this.mobile_phone_number = mobile_phone_number;
    }

    public void setHome_phone_number(String home_phone_number) {
        this.home_phone_number = home_phone_number;
    }

    public String toString() {
        return "Telephone{" +
                "mobile_phone_number='" + mobile_phone_number + '\'' +
                ", home_phone_number='" + home_phone_number + '\'' +
                '}';
    }
}

