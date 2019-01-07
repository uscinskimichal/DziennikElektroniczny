package Notes;

import Database.Database;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class NotesWindowController implements Initializable {

    private String selectedItem;
    private ObservableList<String> subjects = FXCollections.observableArrayList();
    private ObservableList<Note> notes = FXCollections.observableArrayList();


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


    @FXML
    private void showNotes() {
        selectedItem = listSubjects.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            fillTable(selectedItem);
    }

    private void fillTable(String subject) {
        notes = Database.getNotes(subject, UserLoggedIn.Login);
        tableView.setItems(notes);
        double averageNote = 0;
        ifEmptyNotes(averageNote);
    }


    private void ifEmptyNotes(double averageNote) {
        if (notes.isEmpty()) {
            tableView.setPlaceholder(new Label("Brak ocen!"));
            averageNoteLabel.setText(null);
        } else {
            for (int i = 0; i < notes.size(); i++)
                averageNote = averageNote + columnValue.getCellData(i);
            averageNoteLabel.setText(new DecimalFormat("##.##").format(averageNote / notes.size()));
        }
    }

    @FXML
    private void backToMenu() {
        Main.changeScene("/Menu/MenuWindow.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double averageNote = 0;

        if (UserLoggedIn.ID_Klasy != null)
            subjects = Database.getSubjects(Integer.parseInt(UserLoggedIn.ID_Klasy));
        if (!subjects.isEmpty())
            notes = Database.getNotes(subjects.get(0), UserLoggedIn.Login);
        else
            subjects.add("Brak przedmiotów!");


        listSubjects.setItems(subjects);

        columnData.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        tableView.setItems(notes);


        ifEmptyNotes(averageNote);

    }
}


