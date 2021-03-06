package main.controller;

import main.utilities.data.Absence;
import main.database.Database;
import main.utilities.navigator.Navigator;
import main.utilities.userInformations.UserLoggedIn;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AbsenceWindowParentController extends Navigator implements Initializable {

    String login;
    String idClass;
    private ArrayList<ArrayList<String>> children = Database.getChildren(UserLoggedIn.LOGIN);
    private ObservableList<Absence> absences;


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
    private TableColumn<Absence, String> dataColumn;

    @FXML
    private TableColumn<Absence, String> statusColumn;

    @FXML
    private TableView<Absence> tableView;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private void setDataToDisplay() {
        login = children.get(comboBox.getSelectionModel().getSelectedIndex()).get(0);
        idClass = children.get(comboBox.getSelectionModel().getSelectedIndex()).get(3);

        absences = Database.getAbsences(login);
        tableView.setItems(absences);
        if (absences.isEmpty())
            tableView.setPlaceholder(new Label(getResourceBundle().getString("AbsenceParent.Label")));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        printUser();
        for (int i = 0; i < children.size(); i++)
            comboBox.getItems().add(i, children.get(i).get(1) + " " + children.get(i).get(2) + " , " + children.get(i).get(4));

        dataColumn.setCellValueFactory(new PropertyValueFactory<>("absenceDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("absenceStatus"));
        tableView.setItems(absences);

        tableView.setPlaceholder(new Label(getResourceBundle().getString("AbsenceParentController.Label")));
    }
}
