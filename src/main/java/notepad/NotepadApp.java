package notepad;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class NotepadApp extends Application {
    private TextArea textArea;
    private File currentFile;

    @Override
    public void start(Stage stage) {
        textArea = new TextArea();
        textArea.setWrapText(true);

        MenuBar menuBar = createMenuBar(stage);
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(textArea);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Simple Notepad");
        stage.show();
    }

    private MenuBar createMenuBar(Stage stage) {
        Menu fileMenu = new Menu("File");

        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        newItem.setOnAction(event -> newFile());

        MenuItem openItem = new MenuItem("Open...");
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openItem.setOnAction(event -> openFile(stage));

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveItem.setOnAction(event -> saveFile(stage));

        MenuItem saveAsItem = new MenuItem("Save As...");
        saveAsItem.setOnAction(event -> saveFileAs(stage));

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> stage.close());

        fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem, new SeparatorMenuItem(), exitItem);

        MenuBar menuBar = new MenuBar(fileMenu);
        return menuBar;
    }

    private void newFile() {
        textArea.clear();
        currentFile = null;
    }

    private void openFile(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Text File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.md", "*.java", "*.log", "*.csv", "*.text"));
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            readFile(file);
            currentFile = file;
            stage.setTitle("Simple Notepad - " + file.getName());
        }
    }

    private void saveFile(Stage stage) {
        if (currentFile == null) {
            saveFileAs(stage);
        } else {
            writeFile(currentFile);
        }
    }

    private void saveFileAs(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Text File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.md", "*.text"));
        File file = chooser.showSaveDialog(stage);
        if (file != null) {
            currentFile = file;
            writeFile(file);
            stage.setTitle("Simple Notepad - " + file.getName());
        }
    }

    private void readFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            textArea.setText(content.toString());
        } catch (IOException ex) {
            showAlert("Unable to open file", ex.getMessage());
        }
    }

    private void writeFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textArea.getText());
        } catch (IOException ex) {
            showAlert("Unable to save file", ex.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
