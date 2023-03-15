package gui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UiBuilder {
    public static BorderPane buildUI(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setBottom(Menus.getStatusBar());
        root.setTop(Menus.getMenuBar());
        root.setCenter(Menus.getCenterPane());
        root.setLeft(Menus.getLeftSideBar());

        primaryStage.setTitle(Menus.getTitle());
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        return root;
    }
}
