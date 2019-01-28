package menu;


import alerts.PopUpAlerts;
import database.Database;
import navigator.Navigator;
import userInformations.UserLoggedIn;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChangePasswordController extends Navigator {

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
                PopUpAlerts.popAlertError(getResourceBundle().getString("ErrorCommunicat"), getResourceBundle().getString("SamePasswordErrorHeader"), getResourceBundle().getString("ChangePasswordContent"));

            else if (oldPassword.getText().equals(UserLoggedIn.PASSWORD) && newPassword.getText().equals(newPasswordConfirm.getText())) {
                Database.changePassword(UserLoggedIn.LOGIN, newPasswordConfirm.getText());
                PopUpAlerts.popAlertInformation(getResourceBundle().getString("Sucseed"), getResourceBundle().getString("ChangePasswordHeader"), getResourceBundle().getString("ChangePasswordContent"));
                UserLoggedIn.PASSWORD = newPasswordConfirm.getText();
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                stage.close();
            } else
                PopUpAlerts.popAlertError(getResourceBundle().getString("ErrorCommunicat"), getResourceBundle().getString("BadPasswordErrorHeader"), getResourceBundle().getString("ChangePasswordContent"));
        } else
            PopUpAlerts.popAlertError(getResourceBundle().getString("ErrorCommunicat"), getResourceBundle().getString("ChangePasswordErrorHeader"), getResourceBundle().getString("ChangePasswordContent"));
    }
}
