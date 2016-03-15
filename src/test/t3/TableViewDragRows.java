package test.t3;

/**
 * Created by Developer on 14.03.2016.
 */

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.function.Function;

public class TableViewDragRows extends Application {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private static final IntegerProperty dragFromIndex = new SimpleIntegerProperty(-1);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TableView<Person> tableView = new TableView<>();
        tableView.getColumns().add(createCol("First Name", Person::firstNameProperty, 150));
        tableView.getColumns().add(createCol("Last Name", Person::lastNameProperty, 150));
        tableView.getColumns().add(createCol("Email", Person::emailProperty, 200));

        tableView.getItems().addAll(

        );
        ObservableList<Person> data = tableView.getItems();
        data.addAll(new Person("Jacob", "Smith", "jacob.smith@example.com"),
                new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
                new Person("Ethan", "Williams", "ethan.williams@example.com"),
                new Person("Emma", "Jones", "emma.jones@example.com"),
                new Person("Michael", "Brown", "michael.brown@example.com"));
        tableView.setItems(data);


        tableView.setRowFactory(tv -> {

            TableRow<Person> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    Integer index = row.getIndex();
                    dragFromIndex.set(index);

                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragEntered(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    if (dragFromIndex.get() >= 0 && dragFromIndex.get() != row.getIndex())
                        row.setStyle("-fx-background-color: gold");
                }
            });

            row.setOnDragExited(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    row.setStyle("");
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);

                    Person personDragged = tableView.getItems().get(draggedIndex);
                    int dropIndex;

                    if (row.isEmpty()) {
                        dropIndex = tableView.getItems().size();
                    } else {
                        dropIndex = row.getIndex();
                        System.out.println(dropIndex);
                        Person personResult = tableView.getItems().get(dropIndex);

                        swapPersonsFirstColumn(personDragged, personResult);
                    }

//                    tableView.getItems().add(dropIndex, draggedPerson);

                    event.setDropCompleted(true);
                    tableView.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });

            return row;
        });


        Scene scene = new Scene(new BorderPane(tableView), 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void swapPersonsFirstColumn(Person personDragged, Person personResult) {
        String tempPersonDraggedFirstName = personDragged.getFirstName();

        personDragged.setFirstName(personResult.getFirstName());
        personResult.setFirstName(tempPersonDraggedFirstName);
    }

    private TableColumn<Person, String> createCol(String title,
                                                  Function<Person, ObservableValue<String>> mapper, double size) {

        TableColumn<Person, String> col = new TableColumn<>(title);
        col.setCellValueFactory(cellData -> mapper.apply(cellData.getValue()));
        col.setPrefWidth(size);

        return col;
    }
}

class Person {
    private final StringProperty firstName = new SimpleStringProperty(this, "firstName");
    private final StringProperty lastName = new SimpleStringProperty(this, "lastName");
    private final StringProperty email = new SimpleStringProperty(this, "email");

    public Person(String firstName, String lastName, String email) {
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.email.set(email);
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

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    @Override
    public String toString() {
        return firstName.getValue() + " " + lastName.getValue() + " " + email.getValue();
    }
}