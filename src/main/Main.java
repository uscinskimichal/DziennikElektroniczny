package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.utilities.connectionChecker.ConnectionChecker;

import java.util.ResourceBundle;

public class Main extends Application {

    private static Stage primaryStage;

    private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/LoginWindowController.fxml"));
        primaryStage.setTitle(ResourceBundle.getBundle("resources.boundles.messages").getString("Application.title"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/resources/images/icon.png"));
        primaryStage.show();

    }

    public static void main(String[] args) {
        new ConnectionChecker();
        launch();
    }
}
