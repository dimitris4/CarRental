public class Model extends Brand{

    private String name;
    private int modelID;


    public void setModelID(int modelID) { this.modelID = modelID; }

    public int getModelID() { return modelID; }

    public Model(String name) {
        this.name = name;
    }

    public Model(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                '}';
    }
}