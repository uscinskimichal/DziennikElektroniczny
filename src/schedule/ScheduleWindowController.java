package schedule;

import database.Database;
import navigator.Navigator;
import userInformations.UserLoggedIn;
import javafx.collections.FXCollections;
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

public class ScheduleWindowController extends Navigator implements Initializable {

    private ObservableList<Schedule> schedule = FXCollections.observableArrayList();

    private void printUser() {
        if (UserLoggedIn.PERMISSION == 1) {
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
    private TableColumn<Schedule, String> fridayCol;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        printUser();
        tableView.setPlaceholder(new Label(getResourceBundle().getString("NoLessonsCommunicat.label")));

        if (UserLoggedIn.PERMISSION == 1)
            schedule = Database.getSchedule(Integer.parseInt(UserLoggedIn.ID_KLASY));
        else
            schedule = Database.getTeacherSchedule(UserLoggedIn.LOGIN);


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