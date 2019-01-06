package Absences;

import Database.Database;
import Main.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AbsenceWindowController implements Initializable {

    private final ObservableList<Absence> absences = Database.getAbsences();

    @FXML
    private TableColumn<Absence, String> dataColumn;

    @FXML
    private TableColumn<Absence, String> statusColumn;

    @FXML
    private TableView<Absence> tableView;

    @FXML
    private void backToMenu(){
        Main.changeScene("/Menu/MenuWindow.fxml","Dziennik Elektroniczny",Main.getPrimaryStage());
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("absenceDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("absenceStatus"));
        tableView.setItems(absences);

        if(absences.isEmpty())
            tableView.setPlaceholder(new Label("Brak nieobecności, oby tak dalej!"));


    }
}
