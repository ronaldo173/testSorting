package test.t2;

/**
 * Created by Developer on 14.03.2016.
 */
import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author Graham Smith
 */
public class Person {
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty primaryEmail;
    private final SimpleStringProperty secondaryEmail;
    public Person(String firstName, String lastName, String primaryEmail, String secondaryEmail) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.primaryEmail = new SimpleStringProperty(primaryEmail);
        this.secondaryEmail = new SimpleStringProperty(secondaryEmail);
    }
    public String getFirstName() {
        return firstName.get();
    }
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }
    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }
    public String getLastName() {
        return lastName.get();
    }
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }
    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }
    public String getPrimaryEmail() {
        return primaryEmail.get();
    }

    public void setPrimaryEmail( String primaryEmail ) {
        this.primaryEmail.set( primaryEmail );
    }

    public SimpleStringProperty getPrimaryEmailProperty() {
        return primaryEmail;
    }
    public String getSecondaryEmail() {
        return secondaryEmail.get();
    }

    public void setSecondaryEmail( String secondaryEmail ) {
        this.secondaryEmail.set( secondaryEmail );
    }

    public SimpleStringProperty getSecondaryEmailProperty() {
        return secondaryEmail;
    }
}