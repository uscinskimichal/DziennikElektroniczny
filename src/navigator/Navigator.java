package navigator;

import alerts.PopUpAlerts;
import database.Database;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import login.LoginWindowController;
import main.Main;
import userInformations.UserLoggedIn;

public abstract class Navigator {

    public Stage pleaseWait;

    @FXML
    private void goToAbsences() {
        if (UserLoggedIn.Permission == 1)
            Main.changeScene("/absences/AbsenceWindow.fxml", Main.getResourceBundle().getString("AbsenceTitle"), Main.getPrimaryStage());
        else if (UserLoggedIn.Permission == 0)
            Main.changeScene("/absences/AbsenceWindowParent.fxml", Main.getResourceBundle().getString("AbsenceTitle"), Main.getPrimaryStage());
        else
            Main.changeScene("/absences/CheckAbsenceWindow.fxml", Main.getResourceBundle().getString("AbsenceTitle"), Main.getPrimaryStage());
    }

    @FXML
    private void goToChangePassword() {
        Stage changePassword = new Stage();
        changePassword.initModality(Modality.APPLICATION_MODAL);
        changePassword.getIcons().add(new Image("file:./resources/images/password_icon.png"));
        Platform.setImplicitExit(false);
        Main.changeScene("/menu/ChangePasswordWindow.fxml", Main.getResourceBundle().getString("ChangePasswordTitle"), changePassword);
        changePassword.show();
    }

    @FXML
    private void goToMessages() {
        Main.changeScene("/message/MessageWindow.fxml", Main.getResourceBundle().getString("MessageTitle"), Main.getPrimaryStage());
    }

    @FXML
    private void goToNotes() {
        if (UserLoggedIn.Permission == 1)
            Main.changeScene("/notes/NotesWindow.fxml", Main.getResourceBundle().getString("YourNotesTitle"), Main.getPrimaryStage());
        else if (UserLoggedIn.Permission == 0)
            Main.changeScene("/notes/NotesWindowParent.fxml", Main.getResourceBundle().getString("NotesTitle"), Main.getPrimaryStage());
        else
            Main.changeScene("/notes/AddNoteWindow.fxml", Main.getResourceBundle().getString("NotesTitle"), Main.getPrimaryStage());
    }

    @FXML
    private void goToSchedule() {
        if (UserLoggedIn.Permission == 0)
            Main.changeScene("/schedule/ScheduleWindowParent.fxml", Main.getResourceBundle().getString("ScheduleTitle"), Main.getPrimaryStage());
        else
            Main.changeScene("/schedule/ScheduleWindow.fxml", Main.getResourceBundle().getString("ScheduleTitle"), Main.getPrimaryStage());
    }

    @FXML
    private void exitApplication() {
        if (PopUpAlerts.popAlertConfirmation(Main.getResourceBundle().getString("AreYouSureTitle"), Main.getResourceBundle().getString("AreYouSureHeader"), Main.getResourceBundle().getString("AreYouSureText")))
            System.exit(0);
    }

    @FXML
    private void logout() {
        if (PopUpAlerts.popAlertConfirmation(Main.getResourceBundle().getString("AreYouSureTitle"), Main.getResourceBundle().getString("AreYouSureLogout"), Main.getResourceBundle().getString("Logout"))) {
            Main.changeScene("/login/LoginWindowController.fxml", Main.getResourceBundle().getString("Application.title"), Main.getPrimaryStage());
            UserLoggedIn.eraseData();
        }
    }

    @FXML
    private void backToMenu() {
        Main.changeScene("/menu/MenuWindow.fxml", Main.getResourceBundle().getString("Application.title"), Main.getPrimaryStage());
    }


}
