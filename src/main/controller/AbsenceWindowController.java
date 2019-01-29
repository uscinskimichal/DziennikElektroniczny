package main.controller;

import main.utilities.data.Absence;
import main.database.Database;
import main.utilities.navigator.Navigator;
import main.utilities.userInformations.UserLoggedIn;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class AbsenceWindowController extends Navigator implements Initializable {

    private final ObservableList<Absence> absences = Database.getAbsences(UserLoggedIn.LOGIN);


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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        printUser();

        dataColumn.setCellValueFactory(new PropertyValueFactory<>("absenceDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("absenceStatus"));
        tableView.setItems(absences);


        if (absences.isEmpty())
            tableView.setPlaceholder(new Label(getResourceBundle().getString("AbsenceNo.Label")));
    }
}
