public class CarType {

    private String name;
    private String description;

    public CarType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CarType(){}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "CarType{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}