package test.t4;

/**
 * Created by Developer on 14.03.2016.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DragAndDropListView extends Application {

    @Override
    public void start(Stage primaryStage) {
        final ListView<String> listView = new ListView<>();
        for (int i=1; i<=20; i++) {
            listView.getItems().add("Item "+i);
        }

        final IntegerProperty dragFromIndex = new SimpleIntegerProperty(-1);

        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> lv) {
                final ListCell<String> cell = new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item,  empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item);
                        }
                    }
                };

                cell.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (! cell.isEmpty()) {
                            dragFromIndex.set(cell.getIndex());
                            Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                            ClipboardContent cc = new ClipboardContent();
                            cc.putString(cell.getItem());
                            db.setContent(cc);
                            // Java 8 only:
//                          db.setDragView(cell.snapshot(null, null));
                        }
                    }
                });

                cell.setOnDragOver(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        if (dragFromIndex.get() >= 0 && dragFromIndex.get() != cell.getIndex()) {
                            event.acceptTransferModes(TransferMode.MOVE);
                        }
                    }
                });


                // highlight drop target by changing background color:
                cell.setOnDragEntered(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        if (dragFromIndex.get() >= 0 && dragFromIndex.get() != cell.getIndex()) {
                            // should really set a style class and use an external style sheet,
                            // but this works for demo purposes:
                            cell.setStyle("-fx-background-color: gold;");
                        }
                    }
                });

                // remove highlight:
                cell.setOnDragExited(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        cell.setStyle("");
                    }
                });

                cell.setOnDragDropped(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {

                        int dragItemsStartIndex ;
                        int dragItemsEndIndex ;
                        int direction ;
                        if (cell.isEmpty()) {
                            dragItemsStartIndex = dragFromIndex.get();
                            dragItemsEndIndex = listView.getItems().size();
                            direction = -1;
                        } else {
                            if (cell.getIndex() < dragFromIndex.get()) {
                                dragItemsStartIndex = cell.getIndex();
                                dragItemsEndIndex = dragFromIndex.get() + 1 ;
                                direction = 1 ;
                            } else {
                                dragItemsStartIndex = dragFromIndex.get();
                                dragItemsEndIndex = cell.getIndex() + 1 ;
                                direction = -1 ;
                            }
                        }

                        List<String> rotatingItems = listView.getItems().subList(dragItemsStartIndex, dragItemsEndIndex);
                        List<String> rotatingItemsCopy = new ArrayList<>(rotatingItems);
                        Collections.rotate(rotatingItemsCopy, direction);
                        rotatingItems.clear();
                        rotatingItems.addAll(rotatingItemsCopy);
                        dragFromIndex.set(-1);
                    }
                });

                cell.setOnDragDone(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        dragFromIndex.set(-1);
                        listView.getSelectionModel().select(event.getDragboard().getString());
                    }
                });


                return cell ;
            }


        });

        BorderPane root = new BorderPane();
        root.setCenter(listView);
        Scene scene = new Scene(root, 250, 400);
//        scene.getStylesheets().add(getClass().getResource("drag-and-drop-list.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}