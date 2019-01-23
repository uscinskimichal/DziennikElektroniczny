package Notes;

import Alerts.PopUpAlerts;
import Database.Database;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NotesWindowParentController implements Initializable {

    private String selectedItem;
    private ObservableList<String> subjects = FXCollections.observableArrayList();
    private ObservableList<Note> notes = FXCollections.observableArrayList();
    private ArrayList<ArrayList<String>> children = Database.getChildren(UserLoggedIn.Login);
    private String login;
    private String idClass;


    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private Label averageNoteLabel;

    @FXML
    private ListView<String> listSubjects;

    @FXML
    private TableColumn<Note, String> columnType;

    @FXML
    private TableColumn<Note, String> columnComment;

    @FXML
    private TableView<Note> tableView;

    @FXML
    private TableColumn<Note, String> columnData;

    @FXML
    private TableColumn<Note, Double> columnValue;

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

    private void fillTable(String subject) {
        notes = Database.getNotes(subject, login);
        tableView.setItems(notes);
        if (notes.isEmpty()) {
            tableView.setPlaceholder(new Label("Brak ocen z przedmiotu!"));
            averageNoteLabel.setText(null);
        } else {
            double averageNote = 0;
            for (int i = 0; i < notes.size(); i++)
                averageNote = averageNote + columnValue.getCellData(i);
            averageNoteLabel.setText(new DecimalFormat("##.##").format(averageNote / notes.size()));
        }

    }

    @FXML
    private void setDataToDisplay() {
        login = children.get(comboBox.getSelectionModel().getSelectedIndex()).get(0);
        idClass = children.get(comboBox.getSelectionModel().getSelectedIndex()).get(3);

        subjects = Database.getSubjects(Integer.parseInt(idClass));


        if (!subjects.isEmpty())
            fillTable(subjects.get(0));
        else {
            subjects.add("Brak przedmiotów!");
            tableView.getItems().clear();
            tableView.setPlaceholder(new Label("Brak ocen!"));
            averageNoteLabel.setText(null);
        }

        listSubjects.setItems(subjects);
    }

    @FXML
    private void showNotes() {
        selectedItem = listSubjects.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            fillTable(selectedItem);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        printUser();

        for (int i = 0; i < children.size(); i++)
            comboBox.getItems().add(i, children.get(i).get(1) + " " + children.get(i).get(2) + " , " + children.get(i).get(4));

        columnData.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        tableView.setItems(notes);


        tableView.setPlaceholder(new Label("Wybierz osobę dla której chcesz wyświetlic oceny."));

    }
}


