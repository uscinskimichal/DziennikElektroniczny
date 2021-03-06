package main.controller;

import main.utilities.alerts.PopUpAlerts;
import main.database.Database;
import main.Main;
import main.utilities.data.Note;
import main.utilities.navigator.Navigator;
import main.utilities.userInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NotesWindowEducatorController extends Navigator implements Initializable {

    private String selectedItem;
    private ObservableList<String> subjects = FXCollections.observableArrayList();
    private ObservableList<Note> notes = FXCollections.observableArrayList();
    private ArrayList<ArrayList<String>> children = Database.getChildrenILead(UserLoggedIn.LOGIN);
    private String login;
    private String idClass;


    @FXML
    private Label userLabel;

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


    @FXML
    private void goToAddNote() {
        changeScene("/Notes/AddNoteWindow.fxml", getResourceBundle().getString("NotesTitle"), Main.getPrimaryStage());
    }

    @FXML
    private void goToCheckNotes(){
        changeScene("/Notes/CheckNotesIPutWindow.fxml",getResourceBundle().getString("NotesTitle"),Main.getPrimaryStage());
    }

    private void fillTable(String subject) {
        notes = Database.getNotes(subject, login);
        tableView.setItems(notes);
        if (notes.isEmpty()) {
            tableView.setPlaceholder(new Label(getResourceBundle().getString("LabelNoNoteSubject")));
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

        Platform.runLater(() -> {
            userLabel.setText(UserLoggedIn.NAME + " " + UserLoggedIn.SURNAME);
            for (int i = 0; i < children.size(); i++)
                comboBox.getItems().add(i, children.get(i).get(1) + " " + children.get(i).get(2) + " , " + children.get(i).get(4));

            columnData.setCellValueFactory(new PropertyValueFactory<>("date"));
            columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
            columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
            columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
            tableView.setItems(notes);

            tableView.setPlaceholder(new Label(getResourceBundle().getString("NotesShow")));

            if (children.isEmpty()) {
                comboBox.setPromptText(getResourceBundle().getString("NotesWindowEducator.header"));
                PopUpAlerts.popAlertInformation(getResourceBundle().getString("AttentionMessage"), getResourceBundle().getString("NotesWindowEducator.header"), getResourceBundle().getString("ToNoteHead"));
                changeScene("/resources/fxml/AddNoteWindow.fxml", getResourceBundle().getString("Application.title"), Main.getPrimaryStage());
            }
        });


    }
}


