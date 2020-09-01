package entity;

import lombok.Getter;

@Getter
public class Car {

    private String title; //this is redundant - used for checking if cars were properly loaded
    private String registrationYear;
    private double price;

    public Car(final String title, final String registrationYear, final double price) {
        this.title = title;
        this.registrationYear = registrationYear;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Car details: \n" +
                "Title: " + title + ",\n" +
                "Registration year: " + registrationYear + ",\n" +
                "Price: " + price + " €.";
    }
}
