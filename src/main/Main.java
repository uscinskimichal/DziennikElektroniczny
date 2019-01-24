package main;

import database.Database;
import login.LoginWindowController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class Main extends Application {

    private static Stage primaryStage;

    private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    public static void changeScene(String path, String title, Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MethodHandles.lookup().lookupClass().getResource(path));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void connectingThread() {
        Database.connectToTheDatabase();
        if (LoginWindowController.pleaseWaitWindow != null)
            Platform.runLater(
                    () -> LoginWindowController.pleaseWaitWindow.close()
            );
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("/login/LoginWindowController.fxml"));
        primaryStage.setTitle("Dziennik Elektroniczny");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:./resources/images/icon.png"));
        primaryStage.show();

    }

    public static void main(String[] args) {
        new Thread(() -> Main.connectingThread()).start();
        launch();
    }
}