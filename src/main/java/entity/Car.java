package entity;

import lombok.Getter;

@Getter
public class Car {

    private String registrationYear;
    private double price;

    public Car(final String registrationYear, final double price) {
        this.registrationYear = registrationYear;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Car details: \n" +
                "Registration year: " + registrationYear + ",\n" +
                "Price: " + price + " €.";
    }
}
