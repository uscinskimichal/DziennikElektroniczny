package main.utilities.navigator;

import main.utilities.alerts.PopUpAlerts;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import main.utilities.userInformations.UserLoggedIn;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class Navigator {

    public ResourceBundle getResourceBundle() {
        Locale.setDefault(new Locale("pl"));
        return ResourceBundle.getBundle("resources.boundles.messages");
    }

    public void changeScene(String path, String title, Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MethodHandles.lookup().lookupClass().getResource(path));
            fxmlLoader.setResources(getResourceBundle());
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAbsences() {
        if (UserLoggedIn.PERMISSION == 1)
            changeScene("/resources/fxml/AbsenceWindow.fxml", getResourceBundle().getString("AbsenceTitle"), Main.getPrimaryStage());
        else if (UserLoggedIn.PERMISSION == 0)
            changeScene("/resources/fxml/AbsenceWindowParent.fxml", getResourceBundle().getString("AbsenceTitle"), Main.getPrimaryStage());
        else
            changeScene("/resources/fxml/CheckAbsenceWindow.fxml", getResourceBundle().getString("AbsenceTitle"), Main.getPrimaryStage());
    }

    @FXML
    private void goToChangePassword() {
        Stage changePassword = new Stage();
        changePassword.initModality(Modality.APPLICATION_MODAL);
        changePassword.getIcons().add(new Image("file:./resources/images/password_icon.png"));
        Platform.setImplicitExit(false);
        changeScene("/resources/fxml/ChangePasswordWindow.fxml", getResourceBundle().getString("ChangePasswordTitle"), changePassword);
        changePassword.show();
    }

    @FXML
    private void goToMessages() {
        changeScene("/resources/fxml/MessageWindow.fxml", getResourceBundle().getString("MessageTitle"), Main.getPrimaryStage());
    }

    @FXML
    private void goToNotes() {
        if (UserLoggedIn.PERMISSION == 1)
            changeScene("/resources/fxml/NotesWindow.fxml", getResourceBundle().getString("YourNotesTitle"), Main.getPrimaryStage());
        else if (UserLoggedIn.PERMISSION == 0)
            changeScene("/resources/fxml/NotesWindowParent.fxml", getResourceBundle().getString("NotesTitle"), Main.getPrimaryStage());
        else
            changeScene("/resources/fxml/AddNoteWindow.fxml", getResourceBundle().getString("NotesTitle"), Main.getPrimaryStage());
    }

    @FXML
    private void goToSchedule() {
        if (UserLoggedIn.PERMISSION == 0)
            changeScene("/resources/fxml/ScheduleWindowParent.fxml", getResourceBundle().getString("ScheduleTitle"), Main.getPrimaryStage());
        else
            changeScene("/resources/fxml/ScheduleWindow.fxml", getResourceBundle().getString("ScheduleTitle"), Main.getPrimaryStage());
    }

    @FXML
    private void exitApplication() {
        if (PopUpAlerts.popAlertConfirmation(getResourceBundle().getString("AreYouSureTitle"), getResourceBundle().getString("AreYouSureHeader"), getResourceBundle().getString("AreYouSureText")))
            System.exit(0);
    }

    @FXML
    private void logout() {
        if (PopUpAlerts.popAlertConfirmation(getResourceBundle().getString("AreYouSureTitle"), getResourceBundle().getString("AreYouSureLogout"), getResourceBundle().getString("Logout"))) {
            changeScene("/resources/fxml/LoginWindowController.fxml", getResourceBundle().getString("Application.title"), Main.getPrimaryStage());
            UserLoggedIn.eraseData();
        }
    }

    @FXML
    private void backToMenu() {
        changeScene("/resources/fxml/MenuWindow.fxml", getResourceBundle().getString("Application.title"), Main.getPrimaryStage());
    }


}
