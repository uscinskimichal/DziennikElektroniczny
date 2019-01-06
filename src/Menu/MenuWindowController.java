package Menu;

import Alerts.PopUpAlerts;
import Main.Main;
import UserInformations.UserLoggedIn;
import com.jfoenix.controls.JFXButton;
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
    private JFXButton changePasswordButton;

    @FXML
    private JFXButton exitButton;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private JFXButton messagesButton;

    @FXML
    private Button yourAbsencesButton;

    @FXML
    private Button checkNotesTeacherButton;

    @FXML
    private Button justifyStudentAbsence;

    @FXML
    private Button scheduleButton;
    @FXML
    private void exitApplication() {
        if (PopUpAlerts.popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz wyjść?", "Wyjście"))
            System.exit(0);
    }

    @FXML
    private void logout() {

        if (PopUpAlerts.popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz się wylogować?", "Wyloguj")) {
            Main.changeScene("/LoginWindow/Login.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
            UserLoggedIn.eraseData();
            System.out.println(UserLoggedIn.Sex);
        }
    }

    @FXML
    private void changePassword() {
        System.out.println(UserLoggedIn.Login + " "+ UserLoggedIn.Password);
        Stage changePassword = new Stage();
        changePassword.initModality(Modality.APPLICATION_MODAL);
        Platform.setImplicitExit(false);
        Main.changeScene("/Menu/ChangePasswordWindow.fxml", "Zmień hasło", changePassword);
        changePassword.show();
    }

    @FXML
    private void showMyNotes(){
        Main.changeScene("/Notes/YourNotes.fxml","Twoje oceny",Main.getPrimaryStage());
    }

    @FXML
    private void showAbsences(){
        Main.changeScene("/Absences/AbsenceWindow.fxml","Nieobecności",Main.getPrimaryStage());
    }


    @FXML
    private void showMessages() {
        Main.changeScene("/MessageWindow/MessageWindow.fxml", "Wiadomości", Main.getPrimaryStage());
    }

    @FXML
    private void dosmth(){
        System.out.println("aa");
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
                yourNotesButton.setVisible(false);
                break;
            case "Rodzic":
                checkListButton.setVisible(false);
                addNoteButton.setVisible(false);
                checkNotesTeacherButton.setVisible(false);
                justifyStudentAbsence.setVisible(false);

                yourAbsencesButton.setText("Nieobecności");
                yourNotesButton.setText("Oceny");

                //yourNotesButton.setOnAction(m-> System.out.println("s")); TO:DO

                break;
        }


    }
}
