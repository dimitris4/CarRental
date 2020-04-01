import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Renter {
    private String first_name;
    private String last_name;
    private ArrayList<Telephone> telephones;
    private ArrayList<Address> addresses;
    private String email;
    private String driverLicenseNumber;

    public Renter() {
    }

    public Renter(String first_name, String last_name, ArrayList<Telephone> telephones, ArrayList<Address> addresses, String email, String driverLicenseNumber) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.telephones = telephones;
        this.addresses = addresses;
        this.email = email;
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public ArrayList<Telephone> getTelephones() {
        return telephones;
    }

    public String getEmail() {
        return email;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDriverLicenseNumber(String driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public void setTelephones(ArrayList<Telephone> telephones) {
        this.telephones = telephones;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    public String toString() {
        return "Renter{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", telephones=" + telephones +
                ", addresses=" + addresses +
                ", email='" + email + '\'' +
                ", driverLicenseNumber='" + driverLicenseNumber + '\'' +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Renter renter = (Renter) o;
        return last_name.equals(renter.last_name) &&
                driverLicenseNumber.equals(renter.driverLicenseNumber);
    }
}
