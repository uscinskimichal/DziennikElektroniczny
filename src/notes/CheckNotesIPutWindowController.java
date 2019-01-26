package notes;

import database.Database;
import main.Main;
import navigator.Navigator;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import userInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class CheckNotesIPutWindowController extends Navigator implements Initializable {

    private Map<Integer, String> classes = Database.getTeacherClasses(UserLoggedIn.Login);
    private Map<Integer, String> subjects;
    private ArrayList<ArrayList<String>> members;
    public ObservableList<Note> notes;

    private int getKeyFromValue(Map map, Object value) {
        for (Object o : map.keySet()) {
            if (map.get(o).equals(value)) {
                return (int) o;
            }
        }
        return -1;
    }

    @FXML
    private Label userLabel;

    @FXML
    private TableColumn<Note, String> columnType;

    @FXML
    private TableColumn<Note, String> columnComment;

    public TableView<Note> tableView;

    @FXML
    private TableColumn<Note, String> columnData;

    @FXML
    private TableColumn<Note, Double> columnValue;

    @FXML
    private ComboBox<String> classesBox;

    @FXML
    private ComboBox<String> subjectsBox;

    @FXML
    private ComboBox<String> membersBox;

    @FXML
    private Button editNoteButton;



    @FXML
    private void goToAddNote() {
        Main.changeScene("/Notes/AddNoteWindow.fxml", "Oceny", Main.getPrimaryStage());
    }

    @FXML
    private void goToNoteHead() {
        Main.changeScene("/Notes/NotesWindowEducator.fxml", "Przegląd ocen", Main.getPrimaryStage());
    }

    @FXML
    private void showNotesIPut() {
        tableView.getItems().clear();
        editNoteButton.setDisable(true);
        if (membersBox.getItems().size() > 0) {
            notes = Database.getNotesIPut(members.get(membersBox.getSelectionModel().getSelectedIndex()).get(0), UserLoggedIn.Login);


            tableView.setItems(notes);
        }
    }

    @FXML
    private void tableListener() {
        if(tableView.getSelectionModel().getSelectedItem()!=null)
        editNoteButton.setDisable(false);
    }

    @FXML
    private void getSubjectsOfClass() {
        subjectsBox.getItems().clear();
        membersBox.getItems().clear();

        int i = 0;
        subjects = Database.getTeachesSubjects(getKeyFromValue(classes, classesBox.getSelectionModel().getSelectedItem()), UserLoggedIn.Login);
        for (Map.Entry<Integer, String> map : subjects.entrySet()) {
            subjectsBox.getItems().add(i, map.getValue());
            i++;
        }

    }

    public void refreshNotes() {
        tableView.setItems(notes);
    }

    @FXML
    private void editNote() throws IOException {

        Stage editNote = new Stage();
        editNote.initModality(Modality.APPLICATION_MODAL);
        Platform.setImplicitExit(false);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/notes/EditNoteWindow.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        EditNoteWindowController controller = fxmlLoader.<EditNoteWindowController>getController();
        controller.setNote(tableView.getSelectionModel().getSelectedItem());
        controller.setController(this);

        editNote.getIcons().add(new Image("file:./resources/images/editnote_icon.png"));
        Scene scene = new Scene(root);
        editNote.setScene(scene);
        editNote.setTitle("Wiadomość");
        editNote.setResizable(false);
        editNote.show();
    }


    @FXML
    private void getMembersOfClass() {
        membersBox.getItems().clear();
        members = Database.getClassMembers(getKeyFromValue(classes, classesBox.getSelectionModel().getSelectedItem()));
        for (int i = 0; i < members.size(); i++) {
            membersBox.getItems().add(i, members.get(i).get(1) + " " + members.get(i).get(2));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userLabel.setText(UserLoggedIn.Name + " " + UserLoggedIn.Surname);
        columnData.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));



        tableView.setPlaceholder(new Label("Brak ocen."));
        for (Map.Entry<Integer, String> map : classes.entrySet())
            classesBox.getItems().add(map.getValue());
    }
}