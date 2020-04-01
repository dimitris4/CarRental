import java.util.ArrayList;
import java.util.List;

public class RenterList {
    private List<Renter> renters;

    public List<Renter> getRenters() {
        return renters;
    }

    public void setRenters(List<Renter> renters) {
        this.renters = renters;
    }

    public void add(Renter renter) {
        renters.add(renter);
    }

    public void remove(Renter renter) {
        renters.remove(renter);
    }

    public void updateDriverLicenceNumber(Renter renter, String newDriverLicenseNumber) {
        renter.setDriverLicenseNumber(newDriverLicenseNumber);
    }

    public void updateTelephone(Renter renter, String oldTelephone, String newTelephone) {
        ArrayList<Telephone> telephones = renter.getTelephones();
        for (Telephone t : telephones) {
            if (t.getNumber().equals(oldTelephone)) {
                telephones.remove(t);
                telephones.add(new Telephone(newTelephone));
            }
        }
    }

    public void updateAddress(Renter renter, String oldStreetName, String newStreetName, int newStreetNumber,
                              String newZip, String newCity) {
        ArrayList<Address> addresses = renter.getAddresses();
        for (Address a : addresses) {
            if (a.getStreetName().equals(oldStreetName)) {
                addresses.remove(a);
                addresses.add(new Address(newStreetName, newStreetNumber, newZip, newCity));
            }
        }
    }

    public void displayRenters() {
        for (Renter renter : renters) {
            System.out.println(renter);
        }
    }

    public void searchByDriverLicenseNumber(String driverLicenseNumber) {
        for (Renter renter : renters) {
            if (renter.getDriverLicenseNumber().equals(driverLicenseNumber)) {
                System.out.println(renter);
            }
        }
    }

    public void searchByRenterLastName(String lastName) {
        for (Renter renter : renters) {
            if (renter.getLast_name().equals(lastName)) {
                System.out.println(renter);
            }
        }
    }
}

