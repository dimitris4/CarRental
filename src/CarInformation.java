public class CarInformation {

    private String registration_number;
    private String first_registration;
    private int odometer;
    private int fuelID;
    private String fuel;
    private int brandID;
    private String brand;
    private int modelID;
    private String model;
    private boolean isAvailable;
    private int typeID;
    private String type;
    private String description;

    public CarInformation() { }

    public CarInformation(String registration_number, String first_registration, int odometer,
                          String fuel, String model, String brand, String type, String description,
                          int isAvailable, int fuelID, int modelID, int brandID, int typeID) {
        this.registration_number = registration_number;
        this.first_registration = first_registration;
        this.odometer = odometer;
        this.fuel=fuel;
        this.model=model;
        this.brand=brand;
        this.type=type;
        this.description=description;
        if (isAvailable==1) this.isAvailable = true; else {
            this.isAvailable=false;
        }
        this.fuelID=fuelID;
        this.modelID = modelID;
        this.brandID=brandID;
        this.typeID=typeID;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getFirst_registration() {
        return first_registration;
    }

    public void setFirst_registration(String first_registration) {
        this.first_registration = first_registration;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public int getFuelID() {
        return fuelID;
    }

    public void setFuelID(int fuelID) {
        this.fuelID = fuelID;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getModelID() {
        return modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Car{" +
                "registration_number='" + registration_number + '\'' +
                ", first_registration='" + first_registration + '\'' +
                ", odometer=" + odometer +
                ", fuelID=" + fuelID +
                ", fuel='" + fuel + '\'' +
                ", brandID=" + brandID +
                ", brand='" + brand + '\'' +
                ", modelID=" + modelID +
                ", model='" + model + '\'' +
                ", isAvailable=" + isAvailable +
                ", typeID=" + typeID +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
