import java.util.ArrayList;

public class Database {

    private static ArrayList<Contract> contracts;

    public Database(){
        contracts = new ArrayList<Contract>();
    }

    public ArrayList<Contract> getContracts() {
        return contracts;
    }
    public void setContracts(ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }
}
