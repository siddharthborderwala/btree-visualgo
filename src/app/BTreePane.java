package app;

import model.Node;
import model.BTree;
import javafx.animation.FillTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BTreePane extends Pane {
    private BTree<Integer> bTree;
    private final double originalX;
    private final double originalY;
    private final int fontSize = 12;
    private final int size = 30;
    private final int rowSpace = 60;

    public BTreePane(double x, double y, BTree<Integer> bTree) {
        this.originalX = x;
        this.originalY = y;
        this.bTree = bTree;
    }

    /*
     * Draw Tree & Node
     */
    public void updatePane(BTree<Integer> bTree) {
        this.getChildren().clear();
        this.bTree = bTree;
        DrawBTree(bTree.getRoot(), originalX, originalY);
    }

    private void DrawNode(String s, double x, double y, Color color) {
        Rectangle rect = new Rectangle(x, y, size, size);
        rect.setFill(color);
        rect.setStroke(Color.WHITESMOKE);
        rect.setArcHeight(10);
        rect.setArcWidth(10);
        Text txt = new Text(x + 11 - s.length(), y + 20, s);
        txt.setFill(Color.WHITE);
        txt.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, fontSize));
        this.getChildren().addAll(rect, txt);
    }

    private void DrawBTree(Node<Integer> root, double x, double y) {
        if (root != null) {
            for (int i = 0; i < root.getSize(); i++) {
                DrawNode(root.getKey(i).toString(), x + i * size, y, Color.web("#6ab5ff"));
            }
            double startY = y + 2 * fontSize;
            if (!root.isLastInternalNode()) {
                for (int i = 0; i < root.getChildren().size(); i++) {
                    double startX = x + i * size;
                    double startX2, endX;

                    if ((double) i > ((double) root.getSize()) / 2) {
                        startX2 = startX
                                + ((bTree.getOrder() - 1) * (bTree.getHeight(root.getChild(i)) - 1) * size >> 1);
                        endX = startX2 + ((double) root.getChild(i).getSize()) / 2 * size;
                    } else if ((double) i < ((double) root.getSize()) / 2) {
                        endX = startX - ((bTree.getOrder() - 1) * (bTree.getHeight(root.getChild(i)) - 1) * size >> 1)
                                - ((double) root.getChild(i).getSize()) / 2 * size;
                        startX2 = endX - ((double) root.getChild(i).getSize()) / 2 * size;
                    } else {
                        startX2 = startX - ((double) root.getChild(i).getSize()) / 2 * size;
                        endX = startX;
                    }

                    if (i == 0) {
                        startX2 -= size * 2;
                        endX -= size * 2;
                    } else if (i == root.getSize()) {
                        startX2 += size * 2;
                        endX += size * 2;
                    }

                    if (!root.getChild(i).isNull()) {
                        Line line = new Line(startX, startY, endX, y + rowSpace);
                        line.setStroke(Color.SILVER);
                        line.setStrokeWidth(1.5);
                        this.getChildren().add(line);
                    }
                    DrawBTree(root.getChild(i), startX2, y + rowSpace);
                }
            }
        }
    }

    public void searchPathColoring(BTree<Integer> bTree, int key) throws Exception {
        updatePane(bTree);
        if (!bTree.isEmpty()) {
            Node<Integer> currentNode = bTree.getRoot();
            double x = originalX, y = originalY;
            double delay = 0;
            while (!currentNode.equals(bTree.nullNode)) {
                int i = 0;
                while (i < currentNode.getSize()) {
                    makeNodeAnimation(currentNode.getKey(i).toString(), x, y, delay);
                    delay += 1;

                    if (currentNode.getKey(i).equals(key)) {
                        return;
                    } else if (currentNode.getKey(i).compareTo(key) > 0) {

                        y += rowSpace;
                        if ((double) i < ((double) currentNode.getSize()) / 2) {
                            x -= ((bTree.getOrder() - 1) * (bTree.getHeight(currentNode.getChild(i)) - 1) * size >> 1) - ((double) currentNode.getChild(i).getSize()) * size;
                        } else {
                            x = x - ((double) currentNode.getChild(i).getSize()) / 2 * size;
                        }
                        if (i == 0) {
                            x -= size * 2;
                        }

                        currentNode = currentNode.getChild(i);
                        i = 0;
                    } else {

                        i++;
                        x += size;
                    }
                }

                if (!currentNode.isNull()) {
                    y += rowSpace;
                    x = x + ((bTree.getOrder() - 1) * (bTree.getHeight(currentNode.getChild(i)) - 1) * size >> 1)
                            + size * 2;

                    currentNode = currentNode.getChild(currentNode.getSize());
                }
            }
        }
        throw new Exception("Not in the tree!");
    }

    /*
     * Draw Animation
     */
    private void makeNodeAnimation(String s, double x, double y, double delay) {
        Rectangle rect = new Rectangle(x, y, size, size);
        rect.setFill(Color.web("#6ab5ff"));
        rect.setStroke(Color.WHITESMOKE);
        rect.setArcHeight(10);
        rect.setArcWidth(10);
        Text txt = new Text(x + 11 - s.length(), y + 20, s);
        txt.setFill(Color.WHITE);
        txt.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, fontSize));
        this.getChildren().addAll(rect, txt);
        FillTransition fill = new FillTransition();

        fill.setAutoReverse(false);
        fill.setCycleCount(1);
        fill.setDelay(Duration.seconds(delay));
        fill.setDuration(Duration.seconds(1));
        fill.setFromValue(Color.web("#6ab5ff"));
        fill.setToValue(Color.web("#f57f7f"));
        fill.setShape(rect);
        fill.play();
    }
}
