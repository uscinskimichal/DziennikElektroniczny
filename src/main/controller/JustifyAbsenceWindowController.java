package main.controller;

import main.utilities.data.Absence;
import main.utilities.alerts.PopUpAlerts;
import main.database.Database;
import main.Main;
import main.utilities.navigator.Navigator;
import main.utilities.userInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class JustifyAbsenceWindowController extends Navigator implements Initializable {

    private Map<Integer, String> classes = Database.getTeacherClassesILead(UserLoggedIn.LOGIN);
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
        if (UserLoggedIn.PERMISSION ==1) {
            clsLabel.setVisible(true);
            classLabel.setVisible(true);
            classLabel.setText(UserLoggedIn.CLASS);
        }
        userLabel.setText(UserLoggedIn.NAME + " " + UserLoggedIn.SURNAME);
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
        if (tableView.getSelectionModel().getSelectedItem().getAbsenceStatus().equals(getResourceBundle().getString("SetYes")))
            PopUpAlerts.popAlertError(getResourceBundle().getString("ErrorCommunicat"), getResourceBundle().getString("JustifyAbsence.header"), getResourceBundle().getString("JustifyAbsence.content"));
        else {
            int absenceId = tableView.getSelectionModel().getSelectedItem().getAbsenceId();
            new Thread(() -> Database.justifyAbsence(absenceId)).start();

            tableView.getSelectionModel().getSelectedItem().setAbsenceStatus(getResourceBundle().getString("SetYes"));
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
            tableView.setPlaceholder(new Label(getResourceBundle().getString("NoData.Text")));
            for (Map.Entry<Integer, String> map : classes.entrySet())
                classesBox.getItems().add(map.getValue());

            if (classes.isEmpty()) {
                PopUpAlerts.popAlertInformation(getResourceBundle().getString("AttentionMessage"), getResourceBundle().getString("Absence.InitializeHeader"), getResourceBundle().getString("ShowNoteContent"));
                changeScene("/resources/fxml/CheckAbsenceWindow.fxml", getResourceBundle().getString("Absence.Title"), Main.getPrimaryStage());
            }
        });
    }
}

