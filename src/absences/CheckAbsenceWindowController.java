package absences;

import alerts.PopUpAlerts;
import database.Database;
import main.GenerateReport;
import main.Main;
import navigator.Navigator;
import userInformations.UserLoggedIn;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class CheckAbsenceWindowController extends Navigator implements Initializable {

    private Map<Integer, String> classes = Database.getTeacherClasses(UserLoggedIn.Login);
    private ArrayList<ArrayList<String>> members;
    private ArrayList<String> list = new ArrayList<>();


    private int getKeyFromValue(Map map, Object value) {
        for (Object o : map.keySet()) {
            if (map.get(o).equals(value)) {
                return (int) o;
            }
        }
        return -1;
    }

    private void printUser() {
        if (UserLoggedIn.Permission==1) {
            clsLabel.setVisible(true);
            classLabel.setVisible(true);
            classLabel.setText(UserLoggedIn.Class);
        }
        userLabel.setText(UserLoggedIn.Name + " " + UserLoggedIn.Surname);
    }

    @FXML
    private Button generateReport;

    @FXML
    private Label userLabel;

    @FXML
    private Label classLabel;

    @FXML
    private Text clsLabel;

    @FXML
    private ListView<String> listView;

    @FXML
    private ComboBox<String> classesBox;

    @FXML
    private Button addAbsenceButton;

    @FXML
    private void generateTheReport() {
        new GenerateReport(getKeyFromValue(classes, classesBox.getSelectionModel().getSelectedItem()));
    }

    @FXML
    private void goToJustifyAbsence() {
        Main.changeScene("/Absences/JustifyAbsenceWindow.fxml", Main.getResourceBundle().getString("Absence.Title"), Main.getPrimaryStage());
    }

    @FXML
    private void getMembers() {
        members = Database.getClassMembers(getKeyFromValue(classes, classesBox.getSelectionModel().getSelectedItem()));
        listView.getItems().clear();
        generateReport.setDisable(false);
        for (int i = 0; i < members.size(); i++) {
            listView.getItems().add(members.get(i).get(1) + " " + members.get(i).get(2));
        }

        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected)
                        list.add(item);
                    else
                        list.remove(item);
                    if (!list.isEmpty())
                        addAbsenceButton.setDisable(false);
                    else
                        addAbsenceButton.setDisable(true);
                });
                return observable;
            }
        }));

    }

    @FXML
    private void setAbsences() {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < members.size(); j++)
                if (((members.get(j).get(1) + " " + members.get(j).get(2)).equals(list.get(i)))) {
                    int index = j;
                    new Thread(() -> Database.addAbsence(members.get(index).get(0), UserLoggedIn.Login)).start();
                }
        }
        PopUpAlerts.popAlertInformation(Main.getResourceBundle().getString("Sucseed"), Main.getResourceBundle().getString("CheckAbsence.headerText"), Main.getResourceBundle().getString("Absence.Title"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        printUser();
        for (Map.Entry<Integer, String> map : classes.entrySet())
            classesBox.getItems().add(map.getValue());
    }
}
