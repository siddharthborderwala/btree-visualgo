package app;

import model.BTree;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.LinkedList;

public class Main extends Application {

    private int key;
    private BTreePane btPane;
    private InfoPane infoPane;
    private final TextField keyText = new TextField();
    private final Button previousButton = new Button("Prev");
    private final Button nextButton = new Button("Next");

    private int index = 0;
    private LinkedList<BTree<Integer>> bTreeLinkedList = new LinkedList<>();
    private final BTree<Integer> bTree = new BTree<>(3);

    @Override
    public void start(Stage primaryStage) {
        final int windowHeight = 480;
        final int windowWidth = 720;

        BorderPane root = new BorderPane();

        HBox hBox = new HBox(15);
        BorderPane.setMargin(hBox, new Insets(10, 10, 10, 10));
        keyText.setPrefWidth(60);
        keyText.setAlignment(Pos.BASELINE_RIGHT);
        Button insertButton = new Button("Insert");
        Button deleteButton = new Button("Delete");
        Button searchButton = new Button("Search");
        Button resetButton = new Button("Reset");
        resetButton.setId("reset");
        resetButton.setStyle("-fx-base: red;");
        Label nullLabel = new Label();
        nullLabel.setPrefWidth(30);
        checkVisible();

        btPane = new BTreePane(windowWidth >> 1, 50, bTree);
        infoPane = new InfoPane();

        root.setTop(hBox);
        root.setCenter(btPane);

        hBox.getChildren().addAll(
                new Label("Enter a number: "), keyText, insertButton, deleteButton,
                searchButton, resetButton, nullLabel, previousButton, nextButton, infoPane
        );
        hBox.setAlignment(Pos.TOP_LEFT);

        insertButton.setOnMouseClicked(e -> insertValue());
        deleteButton.setOnMouseClicked(e -> deleteValue());
        searchButton.setOnMouseClicked(e -> searchValue());
        resetButton.setOnMouseClicked(e -> reset());
        previousButton.setOnMouseClicked(e -> goPrevious());
        nextButton.setOnMouseClicked(e -> goNext());

        Scene scene = new Scene(root, 820, 420);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setTitle("B-Tree Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void checkVisible() {
        if (index > 0 && index < bTreeLinkedList.size() - 1) {
            previousButton.setVisible(true);
            nextButton.setVisible(true);
        } else if (index > 0 && index == bTreeLinkedList.size() - 1) {
            previousButton.setVisible(true);
            nextButton.setVisible(false);
        } else if (index == 0 && index < bTreeLinkedList.size() - 1) {
            previousButton.setVisible(false);
            nextButton.setVisible(true);
        } else {
            previousButton.setVisible(false);
            nextButton.setVisible(false);
        }
    }

    private void insertValue() {
        try {
            key = Integer.parseInt(keyText.getText());
            keyText.setText("");
            bTree.setStepTrees(new LinkedList<>());
            bTree.insert(key);
            index = 0;
            bTreeLinkedList = bTree.getStepTrees();
            btPane.updatePane(bTreeLinkedList.get(0));
            infoPane.updatePane(bTree.getHeight(), bTree.getTreeSize());
            checkVisible();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Enter a number!", ButtonType.YES);
            alert.show();
        }
    }

    private void deleteValue() {
        try {
            key = Integer.parseInt(keyText.getText());
            keyText.setText("");
            if (bTree.getNode(key) == bTree.nullNode) {
                throw new Exception("Not in the tree!");
            }
            bTree.setStepTrees(new LinkedList<>());
            bTree.delete(key);
            index = 0;
            bTreeLinkedList = bTree.getStepTrees();
            btPane.updatePane(bTreeLinkedList.get(0));
            infoPane.updatePane(bTree.getHeight(), bTree.getTreeSize());
            checkVisible();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Enter a number!", ButtonType.YES);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.YES);
            alert.show();
        }
    }

    private void searchValue() {
        try {
            key = Integer.parseInt(keyText.getText());
            keyText.setText("");
            btPane.searchPathColoring(bTree, key);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Enter a number!", ButtonType.YES);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.YES);
            alert.show();
        }
    }

    private void goPrevious() {
        if (index > 0) {
            index--;
            btPane.updatePane(bTreeLinkedList.get(index));
            checkVisible();
        }
    }

    private void goNext() {
        if (index < bTreeLinkedList.size() - 1) {
            index++;
            System.out.println("index: " + index + " - size: " + bTreeLinkedList.size());
            btPane.updatePane(bTreeLinkedList.get(index));
            checkVisible();
        }
    }

    private void reset() {
        keyText.setText("");
        bTree.setRoot(null);
        index = 0;
        bTreeLinkedList.clear();
        btPane.updatePane(bTree);
        infoPane.updatePane(0, 0);
        checkVisible();
    }
}
