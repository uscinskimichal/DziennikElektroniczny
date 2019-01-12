package Absences;

import Alerts.PopUpAlerts;
import Database.Database;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class JustifyAbsenceWindowController implements Initializable {

    private Map<Integer, String> classes = Database.getTeacherClassesILead(UserLoggedIn.Login);
    private ArrayList<ArrayList<String>> members;
    private ObservableList<Absence> absences;

    private int getKeyFromValue(Map map, Object value) {
        for (Object o : map.keySet()) {
            if (map.get(o).equals(value)) {
                return (int) o;
            }
        }
        return -1;
    }

    private void printUser() {
        if (UserLoggedIn.Permission.equals("Uczen")) {
            clsLabel.setVisible(true);
            classLabel.setVisible(true);
            classLabel.setText(UserLoggedIn.Class);
        }
        userLabel.setText(UserLoggedIn.Name + " " + UserLoggedIn.Surname);
    }

    @FXML
    private Label userLabel;

    @FXML
    private Label classLabel;

    @FXML
    private Text clsLabel;

    @FXML
    private Button justifyButton;

    @FXML
    private TableView<Absence> tableView;

    @FXML
    private TableColumn<Absence, String> dataColumn;

    @FXML
    private TableColumn<Absence, String> statusColumn;

    @FXML
    private ComboBox<String> classesBox;

    @FXML
    private ComboBox<String> classMembers;

    @FXML
    private void backToMenu() {
        Main.changeScene("/Menu/MenuWindow.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
    }

    @FXML
    private void goToAbsences() {
        if (UserLoggedIn.Permission.equals("Uczen"))
            Main.changeScene("/Absences/AbsenceWindow.fxml", "Nieobecności", Main.getPrimaryStage());
        else if (UserLoggedIn.Permission.equals("Rodzic"))
            Main.changeScene("/Absences/AbsenceWindowParent.fxml", "Nieobecności", Main.getPrimaryStage());
        else
            Main.changeScene("/Absences/CheckAbsenceWindow.fxml", "Nieobecności", Main.getPrimaryStage());
    }

    @FXML
    private void goToChangePassword() {
        Stage changePassword = new Stage();
        changePassword.initModality(Modality.APPLICATION_MODAL);
        changePassword.getIcons().add(new Image("file:./resources/images/password_icon.png"));
        Platform.setImplicitExit(false);
        Main.changeScene("/Menu/ChangePasswordWindow.fxml", "Zmień hasło", changePassword);
        changePassword.show();
    }

    @FXML
    private void goToMessages() {
        Main.changeScene("/Message/MessageWindow.fxml", "Wiadomości", Main.getPrimaryStage());
    }

    @FXML
    private void goToNotes() {
        if (UserLoggedIn.Permission.equals("Uczen"))
            Main.changeScene("/Notes/NotesWindow.fxml", "Twoje oceny", Main.getPrimaryStage());
        else if (UserLoggedIn.Permission.equals("Rodzic"))
            Main.changeScene("/Notes/NotesWindowParent.fxml", "Oceny", Main.getPrimaryStage());
        else
            Main.changeScene("/Notes/AddNoteWindow.fxml", "Oceny", Main.getPrimaryStage());
    }

    @FXML
    private void goToSchedule() {
        if (UserLoggedIn.Permission.equals("Rodzic"))
            Main.changeScene("/Schedule/ScheduleWindowParent.fxml", "Plan zajęć", Main.getPrimaryStage());
        else
            Main.changeScene("/Schedule/ScheduleWindow.fxml", "Plan zajęć", Main.getPrimaryStage());
    }

    @FXML
    private void exitApplication() {
        if (PopUpAlerts.popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz wyjść?", "Wyjście"))
            System.exit(0);
    }

    @FXML
    private void goToMenu() {
        Main.changeScene("/Menu/MenuWindow.fxml", "Dzienniks asa", Main.getPrimaryStage());
    }

    @FXML
    private void logout() {

        if (PopUpAlerts.popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz się wylogować?", "Wyloguj")) {
            Main.changeScene("/Login/LoginWindowController.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
            UserLoggedIn.eraseData();
        }
    }

    @FXML
    private void setButtonEnabled() {
        if (tableView.getSelectionModel().getSelectedItem() != null)
            justifyButton.setDisable(false);

    }

    @FXML
    private void getAbsences() {
        tableView.getItems().clear();
        absences = Database.getAbsences(members.get(classMembers.getSelectionModel().getSelectedIndex()).get(0));

        dataColumn.setCellValueFactory(new PropertyValueFactory<>("absenceDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("absenceStatus"));
        tableView.setItems(absences);
    }

    @FXML
    private void getMembers() {
        classMembers.getItems().clear();
        tableView.getItems().clear();
        members = Database.getClassMembers(getKeyFromValue(classes, classesBox.getSelectionModel().getSelectedItem()));
        for (int i = 0; i < members.size(); i++) {
            classMembers.getItems().add(members.get(i).get(1) + " " + members.get(i).get(2));
        }
    }

    @FXML
    private void justify() {
        if (tableView.getSelectionModel().getSelectedItem().getAbsenceStatus().equals("Tak"))
            PopUpAlerts.popAlertError("Błąd!", "Ta nieobecność jest już usprawiedliwiona!", "Usprawiedliwienie nieobecności");
        else {
            int absenceId = tableView.getSelectionModel().getSelectedItem().getAbsenceId();
            new Thread(() -> Database.justifyAbsence(absenceId)).start();

            tableView.getSelectionModel().getSelectedItem().setAbsenceStatus("Tak");
            absences.set(tableView.getSelectionModel().getSelectedIndex(), tableView.getSelectionModel().getSelectedItem());

            dataColumn.setCellValueFactory(new PropertyValueFactory<>("absenceDate"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("absenceStatus"));
            tableView.setItems(absences);
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        printUser();

        Platform.runLater(() -> {
            tableView.setPlaceholder(new Label("Brak danych"));
            for (Map.Entry<Integer, String> map : classes.entrySet())
                classesBox.getItems().add(map.getValue());

            if (classes.isEmpty()) {
                PopUpAlerts.popAlertInformation("Uwaga!", "Nie jesteś wychowawcą żadnej klasy!", "Podgląd ocen");
                Main.changeScene("/Absences/CheckAbsenceWindow.fxml", "Nieobecności", Main.getPrimaryStage());
            }
        });
    }


}

