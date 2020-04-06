import java.util.Date;

public class Contract {
    private int contractID;
    private Renter renter;
    private Car car;
    private Date startDate;
    private Date endDate;
    private int maxKm;
    private int actualKm;



    public Contract(int contractID, Renter renter, Car car, Date startDate, Date endDate, int maxKm, int actualKm) {
        this.contractID = contractID;
        this.renter = renter;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxKm = maxKm;
        this.actualKm = actualKm;
    }

    public Contract(){}

    public int getContractID() { return contractID; }

    public void setContractID(int contractID) { this.contractID = contractID; }

    public Renter getRenter() {
        return renter;
    }

    public Car getCar() {
        return car;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getMaxKm() {
        return maxKm;
    }

    public int getActualKm() {
        return actualKm;
    }

    public void setRenter(Renter renter) {
        this.renter = renter;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMaxKm(int maxKm) {
        this.maxKm = maxKm;
    }

    public void setActualKm(int actualKm) {
        this.actualKm = actualKm;
    }

    public String toString() {
        return String.format("%-20d%-20s%-20s%-40s%-40s%-20s%-20s%-20s", this.contractID, this.car.getType().getName(), this.car.getRegistration_number(), this.car.getBrand().getName() + " " + this.car.getModel().getName(), this.renter.getFirst_name() + " " + this.renter.getLast_name(), this.renter.getDriverLicenseNumber(), this.startDate, this.endDate);
    }
}
