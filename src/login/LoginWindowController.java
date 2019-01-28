package login;

import alerts.PopUpAlerts;
import database.Database;
import main.ConnectionChecker;
import main.Main;
import navigator.Navigator;
import userInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class LoginWindowController extends Navigator {

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
        changeScene("/alerts/PleaseWait.fxml", getResourceBundle().getString("PleaseWaitTitle"), pleaseWaitWindow);
        pleaseWaitWindow.showAndWait();
    }

    @FXML
    void exitApp() {
        System.exit(0);
    }

    @FXML
    void logIn() {


        if (LoginField.getText().isEmpty()) {
            PopUpAlerts.popAlertError(getResourceBundle().getString("ErrorCommunicat"), getResourceBundle().getString("LoginEmptyHeader"), getResourceBundle().getString("LoginError"));
            return;
        }

        if (ConnectionChecker.isInternetConnected)
            Database.connectingThread();
        else {
            PopUpAlerts.popAlertError(getResourceBundle().getString("ErrorCommunicat"), getResourceBundle().getString("InternetErrorHeader"), getResourceBundle().getString("InternetContent"));
            return;
        }

        if (!Database.isConnected)
            popPleaseWait();


        ArrayList<String> userInfo = Database.getUserInfo(LoginField.getText());
        if (userInfo.size() != 0) {
            if (userInfo.get(1).equals(PasswordField.getText())) {
                UserLoggedIn.LOGIN = userInfo.get(0);
                UserLoggedIn.PASSWORD = userInfo.get(1);
                UserLoggedIn.NAME = userInfo.get(2);
                UserLoggedIn.SURNAME = userInfo.get(3);
                UserLoggedIn.SEX = userInfo.get(4);
                UserLoggedIn.PERMISSION = Integer.parseInt(userInfo.get(5));
                UserLoggedIn.STATUS = userInfo.get(6);
                ArrayList<String> classInfo = Database.getUserClass();
                if (!classInfo.isEmpty()) {
                    UserLoggedIn.CLASS = classInfo.get(0);
                    UserLoggedIn.ID_KLASY = classInfo.get(1);
                }
                changeScene("/menu/MenuWindow.fxml", getResourceBundle().getString("Application.title"), Main.getPrimaryStage());
                System.out.println("Success");
            } else if (LoginField.getText().equals(userInfo.get(0)))
                PopUpAlerts.popAlertError(getResourceBundle().getString("ErrorCommunicat"), getResourceBundle().getString("BadLoginErrorHeader"), getResourceBundle().getString("LoginErrorContent"));
        } else PopUpAlerts.popAlertError(getResourceBundle().getString("ErrorCommunicat"), getResourceBundle().getString("BadLoginErrorHeader"), getResourceBundle().getString("LoginErrorContent"));
    }
}


