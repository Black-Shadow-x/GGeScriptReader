import gui.Menus;
import gui.UiBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainProgram extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        UiBuilder.buildUI(primaryStage);
        primaryStage.show();

        Menus.setStyle();

    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
