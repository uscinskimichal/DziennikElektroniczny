package notes;

import alerts.PopUpAlerts;
import database.Database;
import main.Main;
import navigator.Navigator;
import userInformations.UserLoggedIn;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;


import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class AddNoteWindowController extends Navigator implements Initializable {

    private Map<Integer, String> classes = Database.getTeacherClasses(UserLoggedIn.LOGIN);
    private Map<Integer, String> subjects;
    private ArrayList<ArrayList<String>> members;

    @FXML
    private Label userLabel;

    @FXML
    private ComboBox<String> classesBox;

    @FXML
    private ComboBox<String> subjectsBox;

    @FXML
    private ComboBox<String> membersBox;

    @FXML
    private ComboBox<Double> noteValueBox;

    @FXML
    private ComboBox<String> noteTypeBox;

    @FXML
    private TextArea noteCommentBox;

    @FXML
    private Button addNoteButton;


    private int getKeyFromValue(Map map, Object value) {
        for (Object o : map.keySet()) {
            if (map.get(o).equals(value)) {
                return (int) o;
            }
        }
        return -1;
    }

    @FXML
    private void goToCheckNotes(){
        changeScene("/notes/CheckNotesIPutWindow.fxml",getResourceBundle().getString("NotesTitle"),Main.getPrimaryStage());
    }

    @FXML
    private void goToNoteHead() {
        changeScene("/notes/NotesWindowEducator.fxml", getResourceBundle().getString("ToNoteHead"), Main.getPrimaryStage());
    }

    @FXML
    private void getMembersOfClass() {
        noteTypeBox.setDisable(true);
        noteValueBox.setDisable(true);
        noteCommentBox.setDisable(true);
        addNoteButton.setDisable(true);


        membersBox.getItems().clear();
        members = Database.getClassMembers(getKeyFromValue(classes, classesBox.getSelectionModel().getSelectedItem()));
        for (int i = 0; i < members.size(); i++) {
            membersBox.getItems().add(members.get(i).get(1) + " " + members.get(i).get(2));
        }

    }

    @FXML
    private void addNote() {
        new Thread(() ->  Database.addNote(getKeyFromValue(subjects, subjectsBox.getSelectionModel().getSelectedItem()),
                members.get(membersBox.getSelectionModel().getSelectedIndex()).get(0),
                noteValueBox.getSelectionModel().getSelectedItem(),
                noteTypeBox.getSelectionModel().getSelectedItem(),
                noteCommentBox.getText())).start();
        PopUpAlerts.popAlertInformation(getResourceBundle().getString("Sucseed"), getResourceBundle().getString("AddNoteHeader"), getResourceBundle().getString("NotesAdd.content"));

    }

    @FXML
    private void showCommentAndButton() {
        noteCommentBox.setDisable(false);
        addNoteButton.setDisable(false);

    }

    @FXML
    private void showNoteValue() {
        noteValueBox.setDisable(false);

    }

    @FXML
    private void showNoteType() {
        if (membersBox.getSelectionModel().getSelectedItem() != null)
            noteTypeBox.setDisable(false);
        else
            noteTypeBox.setDisable(true);
    }

    @FXML
    private void getSubjectsOfClass() {
        subjectsBox.getItems().clear();
        subjects = Database.getTeachesSubjects(getKeyFromValue(classes, classesBox.getSelectionModel().getSelectedItem()), UserLoggedIn.LOGIN);
        for (Map.Entry<Integer, String> map : subjects.entrySet())
            subjectsBox.getItems().add(map.getValue());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userLabel.setText(UserLoggedIn.NAME + " " + UserLoggedIn.SURNAME);
        noteTypeBox.getItems().addAll(getResourceBundle().getString("QuizLabel"), getResourceBundle().getString("TestLabel"), getResourceBundle().getString("AnswerLabel"), getResourceBundle().getString("Homework"), getResourceBundle().getString("AnotherAnswerLabel"));
        for (double note = 1.0; note <= 6.0; note = note + 0.5)
            noteValueBox.getItems().add(note);

        for (Map.Entry<Integer, String> map : classes.entrySet())
            classesBox.getItems().add(map.getValue());

    }
}