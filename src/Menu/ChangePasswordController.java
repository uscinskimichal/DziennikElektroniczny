package Menu;


import Alerts.PopUpAlerts;
import Database.Database;
import UserInformations.UserLoggedIn;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChangePasswordController {

    @FXML
    private PasswordField newPasswordConfirm;

    @FXML
    private PasswordField oldPassword;

    @FXML
    private PasswordField newPassword;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void returnToMenu() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void proceed() {
        if (!oldPassword.getText().isEmpty() && !newPassword.getText().isEmpty() && !newPasswordConfirm.getText().isEmpty()) {
            if (oldPassword.getText().equals(newPassword.getText()) && oldPassword.getText().equals(newPasswordConfirm.getText()))
                PopUpAlerts.popAlertError("Błąd!", "Podane hasła są takie same!", "Zmiana hasła");

            else if (oldPassword.getText().equals(UserLoggedIn.Password) && newPassword.getText().equals(newPasswordConfirm.getText())) {
                Database.changePassword(UserLoggedIn.Login, newPasswordConfirm.getText());
                PopUpAlerts.popAlertInformation("Sukces!", "Hasło zostało pomyślnie zmienione!", "Zmiana hasła");
                UserLoggedIn.Password = newPasswordConfirm.getText();
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                stage.close();
            } else
                PopUpAlerts.popAlertError("Błąd!", "Podane hasła są różne bądź wprowadzone niepoprawne obecne hasło!", "Zmiana hasła");
        } else
            PopUpAlerts.popAlertError("Błąd", "Wszystkie pola muszą być wypełnione!", "Zmiana hasła");
    }
}
