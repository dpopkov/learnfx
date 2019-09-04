package learnfx.javafx9be.ch06controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Represents a boss or an employee in the 'Bosses and Employees' application. */
public class Person {

    public enum MOOD_TYPES {
        Happy,
        Sad,
        Angry,
        Positive
    }

    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty aliasName;
    private ObjectProperty<MOOD_TYPES> mood;

    /** List of subordinate employees (minions). */
    private final ObservableList<Person> employees = FXCollections.observableArrayList();

    public Person(String firstName, String lastName, String aliasName, MOOD_TYPES mood) {
        setFirstName(firstName);
        setLastName(lastName);
        setAliasName(aliasName);
        setMood(mood);
    }

    public final void setAliasName(String aliasName) {
        aliasNameProperty().set(aliasName);
    }

    public final String getAliasName() {
        return aliasNameProperty().get();
    }

    public StringProperty aliasNameProperty() {
        if (aliasName == null) {
            aliasName = new SimpleStringProperty();
        }
        return aliasName;
    }

    public final void setFirstName(String firstName) {
        firstNameProperty().set(firstName);
    }

    public final String getFirstName() {
        return firstNameProperty().get();
    }

    public StringProperty firstNameProperty() {
        if (firstName == null) {
            firstName = new SimpleStringProperty();
        }
        return firstName;
    }

    public final void setLastName(String lastName) {
        lastNameProperty().set(lastName);
    }

    public final String getLastName() {
        return lastNameProperty().get();
    }

    public StringProperty lastNameProperty() {
        if (lastName == null) {
            lastName = new SimpleStringProperty();
        }
        return lastName;
    }

    public final void setMood(MOOD_TYPES mood) {
        moodProperty().set(mood);
    }

    public final MOOD_TYPES getMood() {
        return moodProperty().get();
    }

    public ObjectProperty<MOOD_TYPES> moodProperty() {
        if (mood == null) {
            mood = new SimpleObjectProperty<>();
        }
        return mood;
    }

    public ObservableList<Person> employeesProperty() {
        return employees;
    }
}
