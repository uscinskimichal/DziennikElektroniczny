package main.controller;

import main.database.Database;
import main.utilities.data.Note;
import main.utilities.navigator.Navigator;
import main.utilities.userInformations.UserLoggedIn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NotesWindowParentController extends Navigator implements Initializable {

    private String selectedItem;
    private ObservableList<String> subjects = FXCollections.observableArrayList();
    private ObservableList<Note> notes = FXCollections.observableArrayList();
    private ArrayList<ArrayList<String>> children = Database.getChildren(UserLoggedIn.LOGIN);
    private String login;
    private String idClass;


    @FXML
    private ComboBox<String> comboBox;

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



    private void fillTable(String subject) {
        notes = Database.getNotes(subject, login);
        tableView.setItems(notes);
        if (notes.isEmpty()) {
            tableView.setPlaceholder(new Label(getResourceBundle().getString("LabelNoNotesSubject")));
            averageNoteLabel.setText(null);
        } else {
            double averageNote = 0;
            for (int i = 0; i < notes.size(); i++)
                averageNote = averageNote + columnValue.getCellData(i);
            averageNoteLabel.setText(new DecimalFormat("##.##").format(averageNote / notes.size()));
        }

    }

    @FXML
    private void setDataToDisplay() {
        login = children.get(comboBox.getSelectionModel().getSelectedIndex()).get(0);
        idClass = children.get(comboBox.getSelectionModel().getSelectedIndex()).get(3);

        subjects = Database.getSubjects(Integer.parseInt(idClass));


        if (!subjects.isEmpty())
            fillTable(subjects.get(0));
        else {
            subjects.add(getResourceBundle().getString("NoSubjects"));
            tableView.getItems().clear();
            tableView.setPlaceholder(new Label(getResourceBundle().getString("NoNote")));
            averageNoteLabel.setText(null);
        }

        listSubjects.setItems(subjects);
    }

    @FXML
    private void showNotes() {
        selectedItem = listSubjects.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            fillTable(selectedItem);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        printUser();

        for (int i = 0; i < children.size(); i++)
            comboBox.getItems().add(i, children.get(i).get(1) + " " + children.get(i).get(2) + " , " + children.get(i).get(4));

        columnData.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        tableView.setItems(notes);


        tableView.setPlaceholder(new Label(getResourceBundle().getString("NotesShow")));

    }
}


