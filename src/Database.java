import java.util.ArrayList;

public class Database {

    private static ArrayList<Contract> contracts = new ArrayList<>();
    private static ArrayList<CarInformation> carList = new ArrayList<>();

    public Database(){}

    public ArrayList<Contract> getContracts() {
        return contracts;
    }
    public void setContracts(ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }
    public ArrayList<CarInformation> getCarList(){return carList;}
    public void setCarList(ArrayList<CarInformation> carList){this.carList = carList;}
}
