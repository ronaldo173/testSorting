package test;

/**
 * Created by Developer on 14.03.2016.
 */
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class JavaFXDnDApplication extends Application
{

    private   ListView<Player> playersListView = new ListView<Player>();

    private static final ObservableList<Player> playersList = FXCollections.observableArrayList();

    private static final ListView<Player> teamListView = new ListView<Player>();

    private static final GridPane rootPane = new GridPane();

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Drag and Drop Application");

        initializeComponents();

        initializeListeners();

        buildGUI();

        populateData();

        primaryStage.setScene(new Scene(rootPane, 400, 325));
        primaryStage.show();
    }

    private void initializeListeners()
    {
        playersListView.setOnDragDetected(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                System.out.println("setOnDragDetected");

                Dragboard dragBoard = playersListView.startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();

                content.putString(playersListView.getSelectionModel().getSelectedItem().getName());

                dragBoard.setContent(content);
            }
        });

        playersListView.setOnDragDone(new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent dragEvent)
            {
                System.out.println("setOnDragDone");

// This is not the ideal place to remove the player because the drag might not have been exited on the target.
// String player = dragEvent.getDragboard().getString();
// playersList.remove(new Player(player));
            }
        });

        teamListView.setOnDragEntered(new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent dragEvent)
            {
                System.out.println("setOnDragEntered");

                teamListView.setBlendMode(BlendMode.DIFFERENCE);
            }
        });

        teamListView.setOnDragExited(new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent dragEvent)
            {
                System.out.println("setOnDragExited");

                teamListView.setBlendMode(null);
            }
        });

        teamListView.setOnDragOver(new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent dragEvent)
            {
                System.out.println("setOnDragOver");

                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
        });

        teamListView.setOnDragDropped(new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent dragEvent)
            {
                System.out.println("setOnDragDropped");

                String player = dragEvent.getDragboard().getString();

                teamListView.getItems().addAll(new Player(player));

                playersList.remove(new Player(player));

                dragEvent.setDropCompleted(true);
            }
        });
    }

    private void buildGUI()
    {
        rootPane.setPadding(new Insets(10));
        rootPane.setPrefHeight(30);
        rootPane.setPrefWidth(100);
        rootPane.setVgap(10);
        rootPane.setHgap(20);

        Label playersLabel = new Label("Players");
        Label teamLabel = new Label("Team");

        rootPane.add(playersLabel, 0, 0);
        rootPane.add(playersListView, 0, 1);
        rootPane.add(teamLabel, 1, 0);
        rootPane.add(teamListView, 1, 1);
    }

    private void populateData()
    {
        playersList.addAll(
                new Player("Adam"), new Player("Alex"), new Player("Alfred"), new Player("Albert"),
                new Player("Brenda"), new Player("Connie"), new Player("Derek"), new Player("Donny"),
                new Player("Lynne"), new Player("Myrtle"), new Player("Rose"), new Player("Rudolph"),
                new Player("Tony"), new Player("Trudy"), new Player("Williams"), new Player("Zach")
        );

        playersListView.setItems(playersList);
    }

    private void initializeComponents()
    {
        initializeListView(playersListView);

        initializeListView(teamListView);
    }

    private void initializeListView(ListView<Player> listView)
    {
        listView.setPrefSize(250, 290);
        listView.setEditable(false);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setCellFactory(new StringListCellFactory());
    }

    class StringListCellFactory implements Callback<ListView<Player>, ListCell<Player>>
    {
        @Override
        public ListCell<Player> call(ListView<Player> playerListView)
        {
            return new StringListCell();
        }

        class StringListCell extends ListCell<Player>
        {
            @Override
            protected void updateItem(Player player, boolean b)
            {
                super.updateItem(player, b);

                if (player != null)
                {
                    setText(player.getName());
                }
            }
        }
    }
}
