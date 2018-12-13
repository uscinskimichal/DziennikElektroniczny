package LoginWindow;


import Database.Database;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;


public class LoginController {

    @FXML
    private Button exitButton;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private TextField LoginField;
    @FXML
    private Button LogInButton;
    public static Stage newWindow = new Stage();

    private void popAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    @FXML
    void logIn(ActionEvent event) {


        if (!Database.isConnected) {
            newWindow.initStyle(StageStyle.UNDECORATED);
            newWindow.initModality(Modality.APPLICATION_MODAL);
            Main.changeScene("/Alerts/PleaseWait.fxml", "Please wait...", newWindow);
            newWindow.showAndWait();
        }

        if (LoginField.getText().isEmpty()) {
            popAlert("Błąd!", "Pole login nie może być puste!", "Błąd logowania");
            return;
        }

        ArrayList<String> userInfo = Database.getUserInfo(LoginField.getText());
        if (userInfo.size() != 0) {
            if (userInfo.get(1).equals(PasswordField.getText())) {
                UserLoggedIn.Login = userInfo.get(0);
                UserLoggedIn.Haslo = userInfo.get(1);
                UserLoggedIn.Imie = userInfo.get(2);
                UserLoggedIn.Nazwisko = userInfo.get(3);
                UserLoggedIn.Plec = userInfo.get(4);
                UserLoggedIn.Uprawnienia = userInfo.get(5);
                UserLoggedIn.Status = userInfo.get(6);
                Main.changeScene("/LoginWindow/Login.fxml", "Witaj!", Main.getPrimaryStage());
                System.out.println("Success");
            } else if (LoginField.getText().equals(userInfo.get(0))) {
                popAlert("Błąd!", "Login lub hasło jest niepoprawne!", "Błąd logowania");
            }
        }  else {
            popAlert("Błąd!", "Login lub hasło jest niepoprawne!", "Błąd logowania");
        }
    }

    @FXML
    void exitApp(ActionEvent event) {
        System.exit(0);
    }

}


