import java.util.Date;

public class Car {

    private String registration_number;
    private Date first_registration;
    private int odometer;
    private Fuel fuel;
    private Brand brand;
    private Model model;
    private boolean isAvailable;
    private CarType type;

    public Car() { }

    public Car(String registration_number, Date first_registration, int odometer, Fuel fuel, Brand brand, Model model, CarType type) {
        this.registration_number = registration_number;
        this.first_registration = first_registration;
        this.odometer = odometer;
        this.fuel = fuel;
        this.brand = brand;
        this.model = model;
        this.isAvailable = true;
        this.type = type;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public Date getFirst_registration() {
        return first_registration;
    }

    public int getOdometer() {
        return odometer;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public Brand getBrand() {
        return brand;
    }

    public Model getModel() {
        return model;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public CarType getType() {
        return type;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public void setFirst_registration(Date first_registration) {
        this.first_registration = first_registration;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setType(CarType type) {
        this.type = type;
    }

    public String toString() {
        return "Car{" +
                "registration_number='" + registration_number + '\'' +
                ", first_registration=" + first_registration +
                ", odometer=" + odometer +
                " " + fuel +
                " " + brand +
                " " + model +
                ", isAvailable=" + isAvailable +
                " " + type +
                '}';
    }

    public void updateOdometer(int km) {
        this.odometer += km;
    }
}
