import java.util.List;

public class CarList {
    
    private List<Car> cars;

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
    
    public void add(Car car) {
        cars.add(car);
    }
    
    public void remove(Car car) {
        cars.remove(car);
    }
    
    public void displayAvailableCars() {
        for (Car car : cars) {
            if (car.isAvailable()) {
                System.out.println(car);
            }
        }    
    }

    public void displayUnavailableCars() {
        for (Car car : cars) {
            if (!car.isAvailable()) {
                System.out.println(car);
            }
        }
    }

    public void displayFamilyCars() {
        for (Car car : cars) {
            if (car.getType().getName().equals("Family")) {
                System.out.println(car);
            }
        }
    }

    public void displaySportCars() {
        for (Car car : cars) {
            if (car.getType().getName().equals("Sport")) {
                System.out.println(car);
            }
        }
    }

    public void displayLuxuryCars() {
        for (Car car : cars) {
            if (car.getType().getName().equals("Luxury")) {
                System.out.println(car);
            }
        }
    }

    public void searchByRegistrationNumber(String registrationNumber) {
        for (Car car : cars) {
            if (car.getRegistration_number().equals(registrationNumber)) {
                System.out.println(car);
            }
        }
    }
}
