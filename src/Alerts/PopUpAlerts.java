package Alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class PopUpAlerts {

    public static boolean popAlertConfirmation(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Nie");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Tak");
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        Optional<ButtonType> option = alert.showAndWait();
        return option.get() != ButtonType.CANCEL;
    }

    public static void popAlertError(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static void popAlertInformation(String title, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
