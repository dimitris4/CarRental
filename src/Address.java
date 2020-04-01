public class Address {

    private String streetName;
    private int streetNumber;
    private String zip;
    private String city;

    public Address(String streetName, int streetNumber, String zip, String city) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.zip = zip;
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String toString() {
        return "Address{" +
                "streetName='" + streetName + '\'' +
                ", streetNumber=" + streetNumber +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}

