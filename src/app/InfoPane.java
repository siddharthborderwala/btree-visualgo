package app;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class InfoPane extends HBox {
    public final Label hText;
    public final Label nText;

    public InfoPane() {
        hText = new Label("Height: " + 0);
        nText = new Label("Nodes: " + 0);
        hText.setLayoutY(40);
        nText.setLayoutY(40);
        setSpacing(25.0);

        getChildren().addAll(hText, nText);
    }

    public void updatePane(int height, int nodes) {
        nText.setText("Nodes: " + nodes);
        hText.setText("Height: " + height);
        System.out.println(nText);
        System.out.println(hText);
    }
}
