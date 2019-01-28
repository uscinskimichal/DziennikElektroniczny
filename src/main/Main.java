package main;

import database.Database;
import javafx.event.Event;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import login.LoginWindowController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import navigator.Navigator;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.util.Locale;
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
        Parent root = FXMLLoader.load(getClass().getResource("/login/LoginWindowController.fxml"));
        primaryStage.setTitle(ResourceBundle.getBundle("boundles.messages").getString("Application.title"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:./resources/images/icon.png"));
        primaryStage.show();

    }

    public static void main(String[] args) {
        new ConnectionChecker();
        launch();
    }
}
