import java.text.SimpleDateFormat;
import java.util.Date;

public class Contract {
    private int contractID;
    private int renterID;
    private String registrationNumber;
    private Date startDate;
    private Date endDate;
    private int maxKm;
    private int actualKm;


    public Contract(int contractID, int renterID, String registrationNumber, Date startDate, Date endDate, int maxKm, int actualKm) {
        this.contractID = contractID;
        this.renterID = renterID;
        this.registrationNumber = registrationNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxKm = maxKm;
        this.actualKm = actualKm;
    }

    public Contract(){}

    public int getContractID() { return contractID; }

    public void setContractID(int contractID) { this.contractID = contractID; }

    public int getRenterID() {
        return renterID;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
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

    public void setRenterID(int renterID) {
        this.renterID = renterID;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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
        return String.format("%-25s %-25s %-25s %-25s %-25s", getContractID(),
                formatDate(getStartDate()), formatDate(getEndDate()), getMaxKm(), getActualKm());
    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

}
