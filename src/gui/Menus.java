package gui;

import io.DataLoaded;
import io.ScriptReader;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import opcodes.opcodetexts.OpCodeTextFiles;
import process.ScriptParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menus {
    static ArrayList<OpCodeTextFiles> gameList = new ArrayList<>();
    static Button loadOpCodeBtn;
    static String TITLE =  "GGeScriptReader";
    static String VERSION = "0.01a";
    public static String getTitle() {
        return TITLE + " - " + VERSION;
    }

    public static StatusBar sb;

    static TextArea mainTextArea;

    static ObservableList<String> options;
    static DataLoaded dataLoaded;

    public static String getFileName() {
        return fileNameLabel.getText();
    }

    public static void setFileName(String fileName) {
        Menus.fileNameLabel.setText(fileName);
    }

    static Label fileNameLabel = new Label("-");;
    public static MenuBar getMenuBar() {
        // Create the MenuBar
        MenuBar menuBar = new MenuBar();

        // Create the File menu with menu items
        Menu fileMenu = new Menu("File");

        // Create the Options menu with menu items
        Menu optionsMenu = new Menu("Options");
        optionsMenu.getItems().addAll(
                createMenuItem("Configure Engine", Menus::handleMenuItem),
                createMenuItem("Configurations", Menus::handleMenuItem)
        );


        // Create the Edit menu with menu items
        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(
                createMenuItem("Cut", Menus::handleMenuItem),
                createMenuItem("Copy", Menus::handleMenuItem),
                createMenuItem("Paste", Menus::handleMenuItem)
        );


        fileMenu.getItems().addAll(
                createMenuItem("New Project", Menus::handleMenuItem),
                new SeparatorMenuItem(),
                createMenuItem("Open Script", Menus::handleMenuItem),
                createMenuItem("Save Script", Menus::handleMenuItem),
                new SeparatorMenuItem(),
                createMenuItem("Open Text", Menus::handleMenuItem),
                createMenuItem("Save Text", Menus::handleMenuItem),
                new SeparatorMenuItem(),
                createMenuItem("Exit", Menus::handleMenuItem)
        );

        // Add the menus to the menu bar
        menuBar.getMenus().addAll(
                fileMenu,
                editMenu,
                optionsMenu
        );

        return menuBar;
    }

    private static void handleMenuItem(ActionEvent event) {
        String menuItemText = ((MenuItem) event.getSource()).getText();
        FileChooser fileChooser = new FileChooser();
        File selectedFile;

        switch (menuItemText) {
            case "New Project":
                sb.setStatusText("New Project");
                break;
            case "Open Script":

                fileChooser.setTitle("Open Script");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Script files (*.scp)", "*.scp")
                );
                selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                    ScriptReader reader = new ScriptReader(selectedFile);
                    dataLoaded = new DataLoaded(reader.getByteBuffer());
                    setFileName(selectedFile.getName());
                    ScriptParser sp = new ScriptParser(dataLoaded);
                    setMainTextArea(sp.getHexValuesString());
                    sb.setStatusText("Opened file: " + selectedFile.getAbsolutePath());
                }
                break;
            case "Save Script":
                fileChooser.setTitle("Save Script");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Script files (*.scp)", "*.scp")
                );
                selectedFile = fileChooser.showSaveDialog(null);
                if (selectedFile != null) {
                    sb.setStatusText("Saved file: " + selectedFile.getAbsolutePath());
                }
                break;
            case "Open Text":
                fileChooser.setTitle("Open Text file");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")
                );
                selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                    sb.setStatusText("Opened file: " + selectedFile.getAbsolutePath());
                }
                break;
            case "Save Text":
                fileChooser.setTitle("Save Text");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")
                );
                selectedFile = fileChooser.showSaveDialog(null);
                if (selectedFile != null) {
                    sb.setStatusText("Saved file: " + selectedFile.getAbsolutePath());
                }
                break;
            case "Exit":
                System.exit(0);
                break;
            case "Configure Engine":
                sb.setStatusText("Configure Engine");
                break;
            case "Configurations":
                sb.setStatusText("Configurations");
                break;
            case "Cut":
                sb.setStatusText("Cut");
                break;
            case "Copy":
                sb.setStatusText("Copy");
                break;
            case "Paste":
                sb.setStatusText("Paste");
                break;
            default:
                break;
        }
    }


    private static MenuItem createMenuItem(String text, EventHandler<ActionEvent> handler) {
        MenuItem menuItem = new MenuItem(text);
        menuItem.setOnAction(handler);
        return menuItem;
    }

    public static StatusBar getStatusBar() {
        sb = new StatusBar();
        return sb;
    }

    public static void setUpGameTextFilesList() {
        File[] files = new File(".").listFiles();
        List<File> opCodeFiles = new ArrayList<>();
        assert files != null;
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith("_opCodes.txt")) {
                opCodeFiles.add(file);
            }
        }

        if (opCodeFiles.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No *_opCodes.txt files found");
            alert.setHeaderText(null);
            alert.setContentText("No *_opCodes.txt files were found in the current directory");
            alert.showAndWait();
        } else {
            for (File file : opCodeFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String gameName = "";
                    String[] fileFormats = new String[0];
                    String version = "";
                    String author = "";

                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("#") || line.startsWith("/") || line.isEmpty()) continue;
                        if (line.startsWith("0x")) break;   //ends when you encounter the first hexcode

                        if (line.startsWith("GameName:")) {
                            gameName = line.split(":")[1].trim();
                        }
                        if (line.startsWith("FileFormats:")) {
                            fileFormats = line.replaceAll("\\s+", "").split(":")[1].split(",");
                        }
                        if (line.startsWith("Version:")) {
                            version = line.split(":")[1].trim();
                        }
                        if (line.startsWith("Author:")) {
                            author  = line.split(":")[1].trim();
                        }
                        if (!gameName.isEmpty() && fileFormats.length != 0 && !version.isEmpty() && !author.isEmpty()) {
                            getGameList().add(
                                    new OpCodeTextFiles(gameName, fileFormats, version, author, file)
                            );
                            break;
                        }


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            options.clear();
            for (OpCodeTextFiles textFile : gameList) {
                options.add(textFile.getGameName());
            }

        }
    }

    public static Pane getCenterPane() {
        VBox pane = new VBox();
        pane.setPrefSize(800, 580);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        loadOpCodeBtn = new Button("Load OpCodes");
        Button button2 = new Button("Button 2");
        Button button3 = new Button("Button 3");
        buttonBox.getChildren().addAll(loadOpCodeBtn, button2, button3);


        mainTextArea = new TextArea();
        mainTextArea.setPrefSize(780, 580);
        mainTextArea.setLayoutX(10);
        mainTextArea.setLayoutY(10);
        mainTextArea.setStyle("-fx-text-fill: white; -fx-control-inner-background: black");




        Font font = Font.font("Monospaced", FontWeight.BOLD, 20);
        mainTextArea.setFont(font);

        mainTextArea.prefWidthProperty().bind(pane.widthProperty().subtract(20));
        mainTextArea.prefHeightProperty().bind(pane.heightProperty().subtract(80));

        pane.getChildren().addAll(buttonBox, mainTextArea);

        setUpButtonInteractions();
        return pane;
    }

    private static void setUpButtonInteractions() {
        loadOpCodeBtn.setOnAction(event -> {
            gameList.clear();
            setUpGameTextFilesList();
        });
    }

    public static VBox getLeftSideBar() {
        VBox vbox = new VBox();
        vbox.setPrefSize(150, 580);
        VBox.setMargin(vbox, new Insets(5));
        vbox.setPadding(new Insets(5));
        vbox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        Label gameEngineLabel = new Label("GameEngine:");

        options = FXCollections.observableArrayList(
                "Love Fetish 1",
                "Viper RSR",
                "Viper CTR"
        );

        ComboBox<String> comboBox = new ComboBox<>(options);
        comboBox.setPrefWidth(150);
        comboBox.getSelectionModel().selectFirst();
        comboBox.prefWidthProperty().bind(comboBox.maxWidthProperty());

        comboBox.setCellFactory(lv -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item);
                }
            };
        });
        comboBox.setButtonCell(comboBox.getCellFactory().call(null));

        // interactive sidebar
        ToggleButton toggleButton = new ToggleButton("Hide");
        VBox.setMargin(toggleButton, new Insets(5));

        toggleButton.setOnAction(event -> {
            if (toggleButton.isSelected()) {
                vbox.setMaxWidth(Double.MAX_VALUE);
                vbox.setVisible(false);
                toggleButton.setText("Show");
            } else {
                vbox.setMaxWidth(Double.MAX_VALUE);
                vbox.setVisible(true);
                toggleButton.setText("Hide");
            }
        });

        toggleButton.prefWidthProperty().addListener((obs, oldVal, newVal) -> vbox.setMaxWidth(newVal.doubleValue()));
        vbox.prefHeightProperty().bind(Bindings.createDoubleBinding(
                () -> vbox.getChildren().stream().mapToDouble(node -> node.getBoundsInParent().getMaxY()).max().orElse(0),
                vbox.getChildren()
        ));

        StackPane stackPane = new StackPane(vbox);
        stackPane.setMaxWidth(Double.MAX_VALUE);
        ///
        Label fileLoadedLabel = new Label("File Loaded:");


        ///
        comboBox.setMaxWidth(Double.MAX_VALUE);
        vbox.getChildren().addAll(gameEngineLabel, comboBox, fileLoadedLabel, fileNameLabel);
        HBox.setHgrow(comboBox, Priority.ALWAYS);
        VBox.setVgrow(comboBox, Priority.ALWAYS);
        VBox wrapper = new VBox(stackPane, toggleButton);
        VBox.setVgrow(wrapper, Priority.ALWAYS);
        wrapper.setStyle("-fx-background-color: black; -fx-border-color: #0f2a0f; -fx-border-width: 2px");
        return wrapper;
    }

    public static TextArea getMainTextArea() {
        return mainTextArea;
    }

    public static void setMainTextArea(String text) {
        mainTextArea.setText(text);
    }

    public static ArrayList<OpCodeTextFiles> getGameList() {
        return gameList;
    }

    public static void setStyle() {
        mainTextArea.lookup(".scroll-bar:vertical").setStyle("-fx-background-color: #333333;");
        mainTextArea.lookup(".thumb:vertical").setStyle("-fx-background-color: #555555;");
        mainTextArea.lookup(".increment-button:vertical").setStyle("-fx-background-color: #444444;");
        mainTextArea.lookup(".decrement-button:vertical").setStyle("-fx-background-color: #444444;");
        mainTextArea.lookup(".increment-arrow:vertical").setStyle("-fx-background-color: white;");
        mainTextArea.lookup(".decrement-arrow:vertical").setStyle("-fx-background-color: white;");
    }
}
