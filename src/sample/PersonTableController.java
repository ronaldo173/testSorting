package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.*;

import java.util.Comparator;


/**
 * View-Controller for the person table.
 *
 * @author Marco Jakob
 */
public class PersonTableController {

    @FXML
    private TextField filterField;
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    private ObservableList<Person> masterData = FXCollections.observableArrayList();
    private Comparator<String> comparator = null;

    /**
     * Just add some sample data in the constructor.
     */
    public PersonTableController() {
        masterData.add(new Person("Hans", "Muster"));
        masterData.add(new Person("Ruth", "Mueller"));
        masterData.add(new Person("Heinz", "Kurz"));
        masterData.add(new Person("Cornelia", "Meier"));
        masterData.add(new Person("Werner", "Meyer"));
        masterData.add(new Person("Lydia", "Kunz"));
        masterData.add(new Person("Anna", "Best"));
        masterData.add(new Person("Stefan", "Meier"));
        masterData.add(new Person("Martin", "Mueller"));

        for (int i = 21; i > 0; i--) {
            masterData.addAll(new Person("ly" + i, "test" + i));
        }
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * <p>
     * Initializes the table columns and sets up sorting and filtering.
     */
    @FXML
    private void initialize() {
        comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String s1 = o1.toLowerCase();
                String s2 = o2.toLowerCase();

                String[] s1Parts = s1.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                String[] s2Parts = s2.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

                int i = 0;
                while (i < s1Parts.length && i < s2Parts.length) {

                    if (s1Parts[i].compareTo(s2Parts[i]) == 0) {
                        ++i;
                    } else {
                        try {
                            int intS1 = Integer.parseInt(s1Parts[i]);
                            int intS2 = Integer.parseInt(s2Parts[i]);
                            int diff = intS1 - intS2;
                            if (diff == 0) {
                                ++i;
                            } else {
                                return diff;
                            }
                        } catch (Exception ex) {
                            return s1.compareTo(s2);
                        }
                    }//end else
                }//end while
                if (s1.length() < s2.length()) {
                    return -1;
                } else if (s1.length() > s2.length()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        // 0. Initialize the columns.
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Person> filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches first name.
                } else if (person.getLastName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Person> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(personTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        personTable.setItems(sortedData);

        sortTable();
        makeDragDrop();
    }

    private void sortTable() {
        for (TableColumn<Person, ?> column : personTable.getColumns()) {
            TableColumn<Person, String> temp = (TableColumn<Person, String>) column;
            temp.setComparator(comparator);
        }
        firstNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        personTable.getSortOrder().add(firstNameColumn);
    }

    private void makeDragDrop() {

        personTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Person selected = personTable.getSelectionModel().getSelectedItem();

                    if (selected != null) {
                        System.out.println(selected);
                    } else {
                        System.out.println("no selected");
                    }
                }
            }
        });

        //drag
        personTable.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Person personForSwap = personTable.getSelectionModel().getSelectedItem();
                if (personForSwap != null) {
                    System.out.println("try drag..." + personForSwap);


                    Dragboard dragboard = personTable.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent clipboardContent = new ClipboardContent();

                    clipboardContent.putString(personForSwap.getFirstName());
                    dragboard.setContent(clipboardContent);
                    event.consume();
                }
            }
        });

        personTable.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });

        personTable.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();

                String firstName = dragboard.getString();
                System.out.println(firstName + "\n");

                PickResult result = event.getPickResult();
                System.out.println(result);





                event.setDropCompleted(true);
                sortTable();
            }
        });


    }
}
