package Menu;

import Alerts.PopUpAlerts;
import Database.Database;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

import java.util.ResourceBundle;

public class MenuWindowController implements Initializable {

    @FXML
    private Button addNoteButton;

    @FXML
    private Button checkListButton;

    @FXML
    private Button yourNotesButton;

    @FXML
    private Button scheduleButton;

    @FXML
    private Button yourAbsencesButton;

    @FXML
    private Button checkNotesTeacherButton;

    @FXML
    private Button justifyStudentAbsence;

    @FXML
    private void goToSchedule() {
        if (UserLoggedIn.Permission.equals("Uczen"))
            Main.changeScene("/Schedule/ScheduleWindow.fxml", "Plan zajęć", Main.getPrimaryStage());
        else if (UserLoggedIn.Permission.equals("Rodzic"))
            Main.changeScene("/Schedule/ScheduleWindowParent.fxml", "Plan zajęć", Main.getPrimaryStage());
    }

    @FXML
    private void exitApplication() {
        if (PopUpAlerts.popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz wyjść?", "Wyjście"))
            System.exit(0);
    }

    @FXML
    private void logout() {

        if (PopUpAlerts.popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz się wylogować?", "Wyloguj")) {
            Main.changeScene("/Login/LoginWindowController.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
            UserLoggedIn.eraseData();
        }
    }

    @FXML
    private void changePassword() {
        Stage changePassword = new Stage();
        changePassword.initModality(Modality.APPLICATION_MODAL);
        Platform.setImplicitExit(false);
        Main.changeScene("/Menu/ChangePasswordWindow.fxml", "Zmień hasło", changePassword);
        changePassword.show();
    }

    @FXML
    private void showMyNotes() {
        if (UserLoggedIn.Permission.equals("Uczen"))
            Main.changeScene("/Notes/NotesWindow.fxml", "Twoje oceny", Main.getPrimaryStage());
        else
            Main.changeScene("/Notes/NotesWindowParent.fxml", "Oceny", Main.getPrimaryStage());
    }

    @FXML
    private void showAbsences() {
        if (UserLoggedIn.Permission.equals("Uczen"))
            Main.changeScene("/Absences/AbsenceWindow.fxml", "Nieobecności", Main.getPrimaryStage());
        else
            Main.changeScene("/Absences/AbsenceWindowParent.fxml", "Nieobecności", Main.getPrimaryStage());
    }

    @FXML
    private void checkPresence() {
        Main.changeScene("/Absences/CheckAbsenceWindow.fxml", "Sprawdź obecnosć", Main.getPrimaryStage());
    }

    @FXML
    private void showMessages() {
        Main.changeScene("/Message/MessageWindow.fxml", "Wiadomości", Main.getPrimaryStage());
    }

    @FXML
    private void addNote() {
        Main.changeScene("/Notes/AddNoteWindow.fxml", "Dodaj ocenę", Main.getPrimaryStage());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        switch (UserLoggedIn.Permission) {
            case "Uczen":
                checkListButton.setVisible(false);
                addNoteButton.setVisible(false);
                checkNotesTeacherButton.setVisible(false);
                justifyStudentAbsence.setVisible(false);
                break;
            case "Nauczyciel":
                yourAbsencesButton.setVisible(false);
                scheduleButton.setVisible(false);
                yourNotesButton.setVisible(false);
                break;
            case "Rodzic":
                checkListButton.setVisible(false);
                addNoteButton.setVisible(false);
                checkNotesTeacherButton.setVisible(false);
                justifyStudentAbsence.setVisible(false);
                yourAbsencesButton.setText("Nieobecności");
                yourNotesButton.setText("Oceny");
                break;
        }
    }
}
