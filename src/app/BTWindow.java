package app;

import java.util.LinkedList;

import model.BTree;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class BTWindow extends BorderPane {
    private final int windowHeight;
    private final int windowWidth;

    private int key;
    private BTreePane btPane;
    private final TextField keyText = new TextField();
    private final Button previousButton = new Button("Prev");
    private final Button nextButton = new Button("Next");

    private int index = 0;
    private LinkedList<BTree<Integer>> bTreeLinkedList = new LinkedList<>();
    private final BTree<Integer> bTree = new BTree<>(3);

    public BTWindow(int windowWidth, int windowHeight) {
        super();
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
    }

    public void run() {
        HBox hBox = new HBox(15);
        this.setTop(hBox);
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

        hBox.getChildren().addAll(new Label("Enter a number: "), keyText, insertButton, deleteButton, searchButton,
                resetButton, nullLabel, previousButton, nextButton);
        hBox.setAlignment(Pos.CENTER);
        checkVisible();

        btPane = new BTreePane(windowWidth >> 1, 50, bTree);
        btPane.setPrefSize(windowHeight, windowWidth);
        this.setCenter(btPane);

        insertButton.setOnMouseClicked(e -> insertValue());
        deleteButton.setOnMouseClicked(e -> deleteValue());
        searchButton.setOnMouseClicked(e -> searchValue());
        resetButton.setOnMouseClicked(e -> reset());
        previousButton.setOnMouseClicked(e -> goPrevious());
        nextButton.setOnMouseClicked(e -> goNext());
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
            checkVisible();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input data!", ButtonType.OK);
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
            checkVisible();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input data!", ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    private void searchValue() {
        try {
            key = Integer.parseInt(keyText.getText());
            keyText.setText("");

            btPane.searchPathColoring(bTree, key);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input data!", ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
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
        checkVisible();
    }

}
