public class Fuel {
    private String type;

    public Fuel(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "Fuel{" +
                "type='" + type + '\'' +
                '}';
    }
}