package src.controller;

import src.database.Database;
import src.utilities.data.Note;
import src.utilities.navigator.Navigator;
import src.utilities.userInformations.UserLoggedIn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class NotesWindowController extends Navigator implements Initializable {

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

    private void printUser(){
        if(UserLoggedIn.PERMISSION ==1){
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
    private void showNotes() {
        selectedItem = listSubjects.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            fillTable(selectedItem);
    }

    private void fillTable(String subject) {
        notes = Database.getNotes(subject, UserLoggedIn.LOGIN);
        tableView.setItems(notes);
        double averageNote = 0;
        ifEmptyNotes(averageNote);
    }


    private void ifEmptyNotes(double averageNote) {
        if (notes.isEmpty()) {
            tableView.setPlaceholder(new Label(getResourceBundle().getString("NoNote")));
            averageNoteLabel.setText(null);
        } else {
            for (int i = 0; i < notes.size(); i++)
                averageNote = averageNote + columnValue.getCellData(i);
            averageNoteLabel.setText(new DecimalFormat("##.##").format(averageNote / notes.size()));

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        printUser();
        double averageNote = 0;

        if (UserLoggedIn.ID_KLASY != null)
            subjects = Database.getSubjects(Integer.parseInt(UserLoggedIn.ID_KLASY));
        if (!subjects.isEmpty())
            notes = Database.getNotes(subjects.get(0), UserLoggedIn.LOGIN);
        else
            subjects.add(getResourceBundle().getString("NoSubjects"));


        listSubjects.setItems(subjects);

        columnData.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        tableView.setItems(notes);


        ifEmptyNotes(averageNote);

    }
}


