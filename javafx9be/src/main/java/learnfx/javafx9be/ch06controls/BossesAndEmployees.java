package learnfx.javafx9be.ch06controls;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static learnfx.javafx9be.ch06controls.Person.MOOD_TYPES.*;

public class BossesAndEmployees extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Bosses and Employees: Working with Tables");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 630, 250, Color.WHITE);

        // Create a grid pane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Boss label
        Label bossLbl = new Label("Boss");
        GridPane.setHalignment(bossLbl, HPos.CENTER);
        gridPane.add(bossLbl, 0, 0);

        // List of bosses
        ObservableList<Person> bosses = getPeople();
        final ListView<Person> leaderListView = new ListView<>(bosses);
        leaderListView.setPrefWidth(150);
        leaderListView.setMinWidth(200);
        leaderListView.setMaxWidth(200);
        leaderListView.setPrefHeight(Integer.MAX_VALUE);

        // display first and last name with tooltip using alias
        leaderListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Person item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getFirstName() + " " + item.getLastName());
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(item.getAliasName());
                    setTooltip(tooltip);
                }
            }
        });

        gridPane.add(leaderListView, 0, 1);

        Label employeesLbl = new Label("Employees");
        GridPane.setHalignment(employeesLbl, HPos.CENTER);
        gridPane.add(employeesLbl, 2, 0);

        TableView<Person> employeeTableView = new TableView<>();
        employeeTableView.setEditable(true);
        employeeTableView.setPrefWidth(Integer.MAX_VALUE);

        ObservableList<Person> teamMembers = FXCollections.observableArrayList();
        employeeTableView.setItems(teamMembers);

        TableColumn<Person, String> aliasNameCol = new TableColumn<>("Alias");
        aliasNameCol.setCellValueFactory(new PropertyValueFactory<>("aliasName"));
        aliasNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Person, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Person, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Person, Person.MOOD_TYPES> moodCol = new TableColumn<>("Mood");
        moodCol.setCellValueFactory(new PropertyValueFactory<>("mood"));
        ObservableList<Person.MOOD_TYPES> moods = FXCollections.observableArrayList(Person.MOOD_TYPES.values());
        moodCol.setCellFactory(ComboBoxTableCell.forTableColumn(moods));
        moodCol.setPrefWidth(100);

        employeeTableView.getColumns().add(aliasNameCol);
        employeeTableView.getColumns().add(firstNameCol);
        employeeTableView.getColumns().add(lastNameCol);
        employeeTableView.getColumns().add(moodCol);
        gridPane.add(employeeTableView, 2, 1);

        // selection listening
        leaderListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue() != null) {
                teamMembers.clear();
                teamMembers.addAll(observable.getValue().employeesProperty());
            }
        });

        root.setCenter(gridPane);
        stage.setScene(scene);
        stage.show();
    }

    @SuppressWarnings("SpellCheckingInspection")
    private ObservableList<Person> getPeople() {
        ObservableList<Person> people = FXCollections.observableArrayList();

        Person docX = new Person("Professor X", "Charles", "Xavier", Positive);
        docX.employeesProperty().add(new Person("Wolverine", "James", "Howlett", Angry));
        docX.employeesProperty().add(new Person("Cyclops", "Scott", "Summers", Happy));
        docX.employeesProperty().add(new Person("Storm", "Ororo", "Munroe", Positive));

        Person magneto = new Person("Magneto", "Max", "Eisenhardt", Sad);
        magneto.employeesProperty().add(new Person("Juggernaut", "Cain", "Marko", Angry));
        magneto.employeesProperty().add(new Person("Mystique", "Raven", "Darkhölme", Sad));
        magneto.employeesProperty().add(new Person("Sabretooth", "Victor", "Creed", Angry));

        Person biker = new Person("Mountain Biker", "Jonathan", "Gennick", Positive);
        biker.employeesProperty().add(new Person("MkHeck", "Mark", "Heckler", Happy));
        biker.employeesProperty().add(new Person("Hansolo", "Gerrit", "Grunwald", Positive));
        biker.employeesProperty().add(new Person("Doc", "José", "Pereda", Happy));
        biker.employeesProperty().add(new Person("Cosmonaut", "Sean", "Phillips", Positive));
        biker.employeesProperty().add(new Person("CarlFX", "Carl", "Dea", Happy));

        people.add(docX);
        people.add(magneto);
        people.add(biker);
        return people;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
