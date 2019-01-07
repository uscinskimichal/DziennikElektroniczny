package Absences;

import Database.Database;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AbsenceWindowParentController implements Initializable {

    String login;
    String idClass;
    private ArrayList<ArrayList<String>> children = Database.getChildren(UserLoggedIn.Login);
    private ObservableList<Absence> absences;

    @FXML
    private TableColumn<Absence, String> dataColumn;

    @FXML
    private TableColumn<Absence, String> statusColumn;

    @FXML
    private TableView<Absence> tableView;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private void backToMenu() {
        Main.changeScene("/Menu/MenuWindow.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
    }

    @FXML
    private void setDataToDisplay() {
        login = children.get(comboBox.getSelectionModel().getSelectedIndex()).get(0);
        idClass = children.get(comboBox.getSelectionModel().getSelectedIndex()).get(3);

        absences = Database.getAbsences(login);
        tableView.setItems(absences);
        if (absences.isEmpty())
            tableView.setPlaceholder(new Label("Brak nieobecności, tak trzymać!"));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (int i = 0; i < children.size(); i++)
            comboBox.getItems().add(i, children.get(i).get(1) + " " + children.get(i).get(2) + " , " + children.get(i).get(4));

        dataColumn.setCellValueFactory(new PropertyValueFactory<>("absenceDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("absenceStatus"));
        tableView.setItems(absences);

        tableView.setPlaceholder(new Label("Wybierz osobę z list aby wyświetlić jej nieobecności."));
    }
}
