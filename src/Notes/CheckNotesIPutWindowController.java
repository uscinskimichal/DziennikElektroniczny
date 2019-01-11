package Notes;

import Database.Database;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class CheckNotesIPutWindowController implements Initializable {

    private Map<Integer, String> classes = Database.getTeacherClasses(UserLoggedIn.Login);
    private Map<Integer, String> subjects;
    private ArrayList<ArrayList<String>> members;
    private ObservableList<Note> notes;

    private int getKeyFromValue(Map map, Object value) {
        for (Object o : map.keySet()) {
            if (map.get(o).equals(value)) {
                return (int) o;
            }
        }
        return -1;
    }

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
    private ComboBox<String> classesBox;

    @FXML
    private ComboBox<String> subjectsBox;

    @FXML
    private ComboBox<String> membersBox;


    @FXML
    private void showNotesIPut() {
        tableView.getItems().clear();
        if (membersBox.getItems().size() > 0) {
            notes = Database.getNotesIPut(members.get(membersBox.getSelectionModel().getSelectedIndex()).get(0), UserLoggedIn.Login);

            columnData.setCellValueFactory(new PropertyValueFactory<>("date"));
            columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
            columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
            columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
            tableView.setItems(notes);
        }
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

    @FXML
    private void refreshNotes(){
        showNotesIPut();
    }

    @FXML
    private void editNote() throws IOException {

        Stage editNote= new Stage();
        editNote.initModality(Modality.APPLICATION_MODAL);
        Platform.setImplicitExit(false);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Notes/EditNoteWindow.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        EditNoteWindowController controller = fxmlLoader.<EditNoteWindowController>getController();
        controller.setNote(tableView.getSelectionModel().getSelectedItem());

        Scene scene = new Scene(root);
        editNote.setScene(scene);
        editNote.setTitle("Wiadomość");
        editNote.setResizable(false);
        editNote.show();
    }


    //bogdan.szmyks
    @FXML
    private void backToMenu() {
        Main.changeScene("/Menu/MenuWindow.fxml", "Dziennik elektroniczny", Main.getPrimaryStage());
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

        tableView.setPlaceholder(new Label("Brak ocen."));
        for (Map.Entry<Integer, String> map : classes.entrySet())
            classesBox.getItems().add(map.getValue());
    }
}
// bogdan.szmyks