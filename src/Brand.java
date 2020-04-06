public class Brand {
    private String name;
    private int brandID;

    public Brand(String name) {
        this.name = name;
    }

    public Brand(){}

    public int getBrandID() { return brandID; }

    public void setBrandID(int brandID) { this.brandID = brandID; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Brand{" +
                "name='" + name + '\'' +
                '}';
    }
}
