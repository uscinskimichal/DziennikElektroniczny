package main.utilities.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.ResourceBundle;

public class PopUpAlerts {

    public static boolean popAlertConfirmation(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:./resources/images/confirm_icon.png"));
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText(ResourceBundle.getBundle("resources.boundles.messages").getString("SetNo"));
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText(ResourceBundle.getBundle("resources.boundles.messages").getString("SetYes"));
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        Optional<ButtonType> option = alert.showAndWait();
        return option.get() != ButtonType.CANCEL;
    }

    public static void popAlertError(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:./resources/images/error_icon.png"));
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static void popAlertInformation(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:./resources/images/info_icon.png"));
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
