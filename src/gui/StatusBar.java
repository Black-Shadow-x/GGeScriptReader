package gui;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class StatusBar extends BorderPane {
    private Label statusLabel;

    public StatusBar() {
        // Create the label for the status bar
        statusLabel = new Label("Ready");
        setStyle("-fx-border-color: #444444; -fx-border-width: 1; -fx-padding: 2;");
        // Add the label to the status bar
        setLeft(statusLabel);
    }

    public void setStatusText(String text) {
        statusLabel.setText(text);
    }
}
