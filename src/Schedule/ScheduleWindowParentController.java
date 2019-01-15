package Schedule;

import Alerts.PopUpAlerts;
import Database.Database;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ScheduleWindowParentController implements Initializable {

    private ObservableList<Schedule> schedule = FXCollections.observableArrayList();
    private ArrayList<ArrayList<String>> children = Database.getChildren(UserLoggedIn.Login);
    private String idClass;

    @FXML
    private TableColumn<Schedule, String> thursdayCol;

    @FXML
    private TableColumn<Schedule, String> mondayCol;

    @FXML
    private TableColumn<Schedule, String> tuesdayCol;

    @FXML
    private TableColumn<Schedule, String> wednesdayCol;

    @FXML
    private TableColumn<Schedule, String> hours;

    @FXML
    private TableView<Schedule> tableView;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TableColumn<Schedule, String> fridayCol;


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
    private void logout() {

        if (PopUpAlerts.popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz się wylogować?", "Wyloguj")) {
            Main.changeScene("/Login/LoginWindowController.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
            UserLoggedIn.eraseData();
        }
    }


    private void setColumnProperties() {
        mondayCol.setSortable(false);
        tuesdayCol.setSortable(false);
        wednesdayCol.setSortable(false);
        thursdayCol.setSortable(false);
        fridayCol.setSortable(false);
        hours.setSortable(false);
        mondayCol.setStyle("-fx-alignment: CENTER;");
        tuesdayCol.setStyle("-fx-alignment: CENTER;");
        wednesdayCol.setStyle("-fx-alignment: CENTER;");
        thursdayCol.setStyle("-fx-alignment: CENTER;");
        fridayCol.setStyle("-fx-alignment: CENTER;");
        hours.setStyle("-fx-alignment: CENTER;");
        tableView.setSelectionModel(null);
    }

    @FXML
    private void setDataToDisplay() {
        tableView.getItems().clear();
        idClass = children.get(comboBox.getSelectionModel().getSelectedIndex()).get(3);
        schedule = Database.getSchedule(Integer.parseInt(idClass));
        tableView.setItems(schedule);

        if (schedule.isEmpty())
            tableView.setPlaceholder(new Label("Brak zajęć."));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        printUser();
        tableView.setPlaceholder(new Label("Wybierz osobę z list aby wyświetlić jej plan."));

        for (int i = 0; i < children.size(); i++)
            comboBox.getItems().add(i, children.get(i).get(1) + " " + children.get(i).get(2) + " , " + children.get(i).get(4));

        mondayCol.setCellValueFactory(new PropertyValueFactory<>("day1"));
        tuesdayCol.setCellValueFactory(new PropertyValueFactory<>("day2"));
        wednesdayCol.setCellValueFactory(new PropertyValueFactory<>("day3"));
        thursdayCol.setCellValueFactory(new PropertyValueFactory<>("day4"));
        fridayCol.setCellValueFactory(new PropertyValueFactory<>("day5"));
        hours.setCellValueFactory(new PropertyValueFactory<>("lessonhours"));
        tableView.setItems(schedule);

        setColumnProperties();
    }
}
