package menu;


import alerts.PopUpAlerts;
import database.Database;
import main.Main;
import userInformations.UserLoggedIn;
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
                PopUpAlerts.popAlertError(Main.getResourceBundle().getString("ErrorCommunicat"), Main.getResourceBundle().getString("SamePasswordErrorHeader"), Main.getResourceBundle().getString("ChangePasswordContent"));

            else if (oldPassword.getText().equals(UserLoggedIn.Password) && newPassword.getText().equals(newPasswordConfirm.getText())) {
                Database.changePassword(UserLoggedIn.Login, newPasswordConfirm.getText());
                PopUpAlerts.popAlertInformation(Main.getResourceBundle().getString("Sucseed"), Main.getResourceBundle().getString("ChangePasswordHeader"), Main.getResourceBundle().getString("ChangePasswordContent"));
                UserLoggedIn.Password = newPasswordConfirm.getText();
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                stage.close();
            } else
                PopUpAlerts.popAlertError(Main.getResourceBundle().getString("ErrorCommunicat"), Main.getResourceBundle().getString("BadPasswordErrorHeader"), Main.getResourceBundle().getString("ChangePasswordContent"));
        } else
            PopUpAlerts.popAlertError(Main.getResourceBundle().getString("ErrorCommunicat"), Main.getResourceBundle().getString("ChangePasswordErrorHeader"), Main.getResourceBundle().getString("ChangePasswordContent"));
    }
}
