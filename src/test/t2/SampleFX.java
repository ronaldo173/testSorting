package test.t2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 *
 * @author Graham Smith
 */
public class SampleFX extends Application {
    private TableView table = new TableView();
    private final ObservableList<Person> data =
            FXCollections.observableArrayList(
                    new Person("Jacob", "Smith", "jacob.smith_at_example.com", "js_at_example.com"),
                    new Person("Isabella", "Johnson", "isabella.johnson_at_example.com", "ij_at_example.com"),
                    new Person("Ethan", "Williams", "ethan.williams_at_example.com", "ew_at_example.com"),
                    new Person("Emma", "Jones", "emma.jones_at_example.com", "ej_at_example.com"),
                    new Person("Michael", "Brown", "michael.brown_at_example.com", "mb_at_example.com"));
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(650);
        stage.setHeight(500);
        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));
        //Create a customer cell factory so that cells can support editing.
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingCell();
            }
        };

        //Set up the columns
        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth( 100 );
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        firstNameCol.setCellFactory(cellFactory);
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth( 100 );
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        lastNameCol.setCellFactory(cellFactory);
//        lastNameCol.setEditable( false );
        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(400);
        TableColumn primaryEmailCol = new TableColumn("Primary Email");
        primaryEmailCol.setMinWidth(200);
        primaryEmailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("primaryEmail"));
        primaryEmailCol.setCellFactory(cellFactory);
        //Make this column un-editable
        primaryEmailCol.setEditable( false );
        TableColumn secondaryEmailCol = new TableColumn("Secondary Email");
        secondaryEmailCol.setMinWidth(200);
        secondaryEmailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("secondaryEmail"));
        secondaryEmailCol.setCellFactory(cellFactory);
//        secondaryEmailCol.setEditable( false );
        emailCol.getColumns().addAll(primaryEmailCol, secondaryEmailCol);
        //Add the columns and data to the table.
        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
        //Make the table editable
        table.setEditable(true);
        //Modifying the firstName property
        firstNameCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
            @Override
            public void handle(CellEditEvent<Person, String> t) {
                ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFirstName(t.getNewValue());
            }
        });
        //Modifying the lastName property
        lastNameCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
            @Override
            public void handle(CellEditEvent<Person, String> t) {
                ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastName(t.getNewValue());
            }
        });
        //Modifying the primary email property
        primaryEmailCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
            @Override
            public void handle(CellEditEvent<Person, String> t) {
                ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPrimaryEmail(t.getNewValue());
            }
        });
        //Modifying the secondary email property
        secondaryEmailCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
            @Override
            public void handle(CellEditEvent<Person, String> t) {
                ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSecondaryEmail(t.getNewValue());
            }
        });
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(label, table);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setScene(scene);
        stage.show();
    }
}