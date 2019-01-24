package notes;

import alerts.PopUpAlerts;
import database.Database;
import main.Main;
import navigator.Navigator;
import userInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NotesWindowEducatorController extends Navigator implements Initializable {

    private String selectedItem;
    private ObservableList<String> subjects = FXCollections.observableArrayList();
    private ObservableList<Note> notes = FXCollections.observableArrayList();
    private ArrayList<ArrayList<String>> children = Database.getChildrenILead(UserLoggedIn.Login);
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
        Main.changeScene("/Notes/AddNoteWindow.fxml", "Oceny", Main.getPrimaryStage());
    }

    @FXML
    private void goToCheckNotes(){
        Main.changeScene("/Notes/CheckNotesIPutWindow.fxml","Oceny",Main.getPrimaryStage());
    }

    private void fillTable(String subject) {
        notes = Database.getNotes(subject, login);
        tableView.setItems(notes);
        if (notes.isEmpty()) {
            tableView.setPlaceholder(new Label("Brak ocen z przedmiotu!"));
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
            subjects.add("Brak przedmiotów!");
            tableView.getItems().clear();
            tableView.setPlaceholder(new Label("Brak ocen!"));
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
            userLabel.setText(UserLoggedIn.Name + " " + UserLoggedIn.Surname);
            for (int i = 0; i < children.size(); i++)
                comboBox.getItems().add(i, children.get(i).get(1) + " " + children.get(i).get(2) + " , " + children.get(i).get(4));

            columnData.setCellValueFactory(new PropertyValueFactory<>("date"));
            columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
            columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
            columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
            tableView.setItems(notes);

            tableView.setPlaceholder(new Label("Wybierz osobę dla której chcesz wyświetlic oceny."));

            if (children.isEmpty()) {
                comboBox.setPromptText("Nie jesteś wychowawcą żadnej klasy!");
                PopUpAlerts.popAlertInformation("Uwaga!", "Nie jesteś wychowawcą żadnej klasy!", "Podgląd ocen");
                Main.changeScene("/notes/AddNoteWindow.fxml", "Dziennik elektroniczny", Main.getPrimaryStage());
            }
        });


    }
}


