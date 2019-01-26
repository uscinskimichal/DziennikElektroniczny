package login;

import alerts.PopUpAlerts;
import database.Database;
import main.ConnectionChecker;
import main.Main;
import userInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class LoginWindowController {

    public static Stage pleaseWaitWindow;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private TextField LoginField;

    private void popPleaseWait() {
        pleaseWaitWindow = new Stage();
        pleaseWaitWindow.initStyle(StageStyle.UNDECORATED);
        pleaseWaitWindow.initModality(Modality.APPLICATION_MODAL);
        pleaseWaitWindow.setOnCloseRequest(Event::consume);
        Platform.setImplicitExit(false);
        Main.changeScene("/alerts/PleaseWait.fxml", "Please wait...", pleaseWaitWindow);
        pleaseWaitWindow.showAndWait();
    }

    @FXML
    void exitApp() {
        System.exit(0);
    }

    @FXML
    void logIn() {


        if (LoginField.getText().isEmpty()) {
            PopUpAlerts.popAlertError("Błąd!", "Pole login nie może być puste!", "Błąd logowania");
            return;
        }

        if (ConnectionChecker.isInternetConnected)
            Database.connectingThread();
        else {
            PopUpAlerts.popAlertError("Błąd", "Brak połączenia z internetem", "Internet");
            return;
        }

        if (!Database.isConnected)
            popPleaseWait();


        ArrayList<String> userInfo = Database.getUserInfo(LoginField.getText());
        if (userInfo.size() != 0) {
            if (userInfo.get(1).equals(PasswordField.getText())) {
                UserLoggedIn.Login = userInfo.get(0);
                UserLoggedIn.Password = userInfo.get(1);
                UserLoggedIn.Name = userInfo.get(2);
                UserLoggedIn.Surname = userInfo.get(3);
                UserLoggedIn.Sex = userInfo.get(4);
                UserLoggedIn.Permission = Integer.parseInt(userInfo.get(5));
                UserLoggedIn.Status = userInfo.get(6);
                ArrayList<String> classInfo = Database.getUserClass();
                if (!classInfo.isEmpty()) {
                    UserLoggedIn.Class = classInfo.get(0);
                    UserLoggedIn.ID_Klasy = classInfo.get(1);
                }
                Main.changeScene("/menu/MenuWindow.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
                System.out.println("Success");
            } else if (LoginField.getText().equals(userInfo.get(0)))
                PopUpAlerts.popAlertError("Błąd!", "login lub hasło jest niepoprawne!", "Błąd logowania");
        } else PopUpAlerts.popAlertError("Błąd!", "login lub hasło jest niepoprawne!", "Błąd logowania");
    }
}


