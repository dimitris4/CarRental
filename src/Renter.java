import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Renter {
    private int renterID;
    private String first_name;
    private String last_name;
    private Telephone telephone;
    private Address address;
    private String email;
    private String driverLicenseNumber;
    private Date sinceDate;

    public Renter() { }

    public Renter(int renterID, String first_name, String last_name, Telephone telephone, Address address, String email, String driverLicenseNumber, Date sinceDate) {
        this.renterID = renterID;
        this.first_name = first_name;
        this.last_name = last_name;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
        this.driverLicenseNumber = driverLicenseNumber;
        this.sinceDate = sinceDate;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Telephone getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public Address getAddress() {
        return address;
    }

    public int getRenterID() {
        return renterID;
    }

    public Date getSinceDate() {
        return sinceDate;
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

    public void setTelephones(Telephone telephone) {
        this.telephone = telephone;
    }

    public void setAddresses(Address addresses) {
        this.address = addresses;
    }

    public void setSinceDate(Date sinceDate) {this.sinceDate = sinceDate; }



    public String toString() {
        return String.format("%-15s %-25s %-25s %-25s %-25s %-30s %-25s %-25s %-25s", getRenterID(), getFirst_name(),
                getLast_name(), getTelephone().getMobile_phone_number(), getTelephone().getHome_phone_number(),
                getEmail(), getDriverLicenseNumber(), formatDate(getSinceDate()), getAddress().getStreetName() + " " +
                getAddress().getBuilding() + " " + getAddress().getFloor() + " " + getAddress().getDoor() + " " +
                getAddress().getZip() + " " + getAddress().getCity() + " " + getAddress().getCountry());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Renter renter = (Renter) o;
        return last_name.equals(renter.last_name) &&
                driverLicenseNumber.equals(renter.driverLicenseNumber);
    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }
}
