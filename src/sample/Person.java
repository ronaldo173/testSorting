package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

/**
 * Simple model class for the person table.
 *
 * @author Marco Jakob
 */
public class Person implements Comparable<Person>{

    private final StringProperty firstName;
    private final StringProperty lastName;

    public Person(String firstName, String lastName) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }


    @Override
    public int compareTo(Person o) {
        NaturalOrderComparator naturalOrderComparator = new NaturalOrderComparator();
        return naturalOrderComparator.compareRight(this.firstName.getValue(),
                o.firstName.getValue());
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName=" + firstName +
                ", lastName=" + lastName +
                '}';
    }


}