import java.util.ArrayList;

public class Database {

    static final public Database instance = new Database();

    private static ArrayList<Contract> contracts = new ArrayList<>();
    private static ArrayList<CarInformation> carList = new ArrayList<>();
    private static ArrayList<Integer> renterIDs = new ArrayList<>();

    public Database(){
        contracts = new ArrayList<Contract>();
        renterIDs = new ArrayList<Integer>();
        carList = new ArrayList<>();
    }

    public ArrayList<Contract> getContracts() {
        return contracts;
    }
    public void setContracts(ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }
    public ArrayList<CarInformation> getCarList(){return carList;}
    public void setCarList(ArrayList<CarInformation> carList){this.carList = carList;}
    public ArrayList<Integer> getRenterIDs() {
        return renterIDs;
    }
    public void setRenterIDs(ArrayList<Integer> renterIDs) { this.renterIDs = renterIDs; }
}
