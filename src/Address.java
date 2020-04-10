public class Address {
    private String streetName;
    private int building;
    private int floor;
    private String door;
    private String zip;
    private String city;
    private String country;

    public Address(String streetName, int building, int floor, String door, String zip, String city, String country) {
        this.streetName = streetName;
        this.building = building;
        this.floor = floor;
        this.door = door;
        this.zip = zip;
        this.city = city;
        this.country = country;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public int getBuilding() {
        return building;
    }

    public int getFloor() {
        return floor;
    }

    public String getDoor() {
        return door;
    }

    public String getCountry() {
        return country;
    }
}

