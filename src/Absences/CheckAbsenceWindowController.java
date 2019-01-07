package Absences;

import Alerts.PopUpAlerts;
import Database.Database;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;


import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class CheckAbsenceWindowController implements Initializable {

    Map<Integer, String> classes = Database.getTeacherClasses(UserLoggedIn.Login);
    ArrayList<ArrayList<String>> members;
    ArrayList<String> list = new ArrayList<>();


    private int getKeyFromValue(Map map, Object value) {
        for (Object o : map.keySet()) {
            if (map.get(o).equals(value)) {
                return (int) o;
            }
        }
        return -1;
    }

    @FXML
    private ListView<String> listView;

    @FXML
    private ComboBox<String> classesBox;

    @FXML
    private void getMembers() {
        members = Database.getClassMembers(getKeyFromValue(classes, classesBox.getSelectionModel().getSelectedItem()));
        listView.getItems().clear();

        for (int i = 0; i < members.size(); i++) {
            listView.getItems().add(members.get(i).get(1) + " " + members.get(i).get(2));
        }


//barbara.rabarbar
        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        list.add(item);
                    } else {
                        list.remove(item);
                    }
                });
                return observable;
            }
        }));

    }

    @FXML
    private void goToMenu() {
        Main.changeScene("/Menu/MenuWindow.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
    }

    @FXML
    private void setAbsences() {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < members.size(); j++)
                if (((members.get(j).get(1) + " " + members.get(j).get(2)).equals(list.get(i))))
                    Database.addAbsence(members.get(j).get(0), UserLoggedIn.Login);
        }
        PopUpAlerts.popAlertInformation("Sukces!", "Nieobecności zostały wstawione.", "Nieobecności");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Map.Entry<Integer, String> map : classes.entrySet())
            classesBox.getItems().add(map.getValue());
    }
}
