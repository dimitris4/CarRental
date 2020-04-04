import java.util.ArrayList;

public class Database {

    private static ArrayList<Contract> contracts;
    private static ArrayList<Integer> renterIDs;

    public Database(){
        contracts = new ArrayList<Contract>();
        renterIDs = new ArrayList<Integer>();
    }

    public ArrayList<Contract> getContracts() {
        return contracts;
    }
    public void setContracts(ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }
    public ArrayList<Integer> getRenterIDs() {
        return renterIDs;
    }
    public void setRenterIDs(ArrayList<Integer> renterIDs) { this.renterIDs = renterIDs; }
}
