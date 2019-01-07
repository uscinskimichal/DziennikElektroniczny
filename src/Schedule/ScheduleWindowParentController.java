package Schedule;

import Database.Database;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.collections.FXCollections;
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

    @FXML
    private void backToMenu() {
        Main.changeScene("/Menu/MenuWindow.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
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
        idClass = children.get(comboBox.getSelectionModel().getSelectedIndex()).get(3);
        schedule = Database.getSchedule(Integer.parseInt(idClass));
        tableView.setItems(schedule);
        if (schedule.isEmpty())
            tableView.setPlaceholder(new Label("Brak zajęć."));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
