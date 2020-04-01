import java.util.Date;

public class Contract {
    private Renter renter;
    private Car car;
    private Date startDate;
    private Date endDate;
    private int maxKm;
    private int actualKm;

    public Contract(Renter renter, Car car, Date startDate, Date endDate, int maxKm, int actualKm) {
        this.renter = renter;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxKm = maxKm;
        this.actualKm = actualKm;
    }

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
        return "Contract{" +
                "renter=" + renter +
                ", car=" + car +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", maxKm=" + maxKm +
                ", actualKm=" + actualKm +
                '}';
    }
}
