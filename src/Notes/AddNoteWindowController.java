package Notes;

import Alerts.PopUpAlerts;
import Database.Database;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;


import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class AddNoteWindowController implements Initializable {

    private Map<Integer, String> classes = Database.getTeacherClasses(UserLoggedIn.Login);
    private Map<Integer, String> subjects;
    private ArrayList<ArrayList<String>> members;

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

    @FXML
    private void goToMenu() {
        Main.changeScene("/Menu/MenuWindow.fxml", "Dziennik elektroniczny", Main.getPrimaryStage());
    }

    private int getKeyFromValue(Map map, Object value) {
        for (Object o : map.keySet()) {
            if (map.get(o).equals(value)) {
                return (int) o;
            }
        }
        return -1;
    }

    @FXML
    private void getMembersOfClass() {
        membersBox.setVisible(true);
        membersBox.getItems().clear();
        members = Database.getClassMembers(getKeyFromValue(classes, classesBox.getSelectionModel().getSelectedItem()));
        for (int i = 0; i < members.size(); i++) {
            membersBox.getItems().add(members.get(i).get(1) + " " + members.get(i).get(2));
        }

    }

    @FXML
    private void addNote() {
        Database.addNote(getKeyFromValue(subjects, subjectsBox.getSelectionModel().getSelectedItem()), members.get(membersBox.getSelectionModel().getSelectedIndex()).get(0), noteValueBox.getSelectionModel().getSelectedItem(), noteTypeBox.getSelectionModel().getSelectedItem(), noteCommentBox.getText());
        PopUpAlerts.popAlertInformation("Suckes!", "Ocena została dodana.", "Dodaj ocenę");
    }

    @FXML
    private void showCommentAndButton() {
        noteCommentBox.setVisible(true);
        addNoteButton.setVisible(true);

    }

    @FXML
    private void showNoteValue() {
        noteValueBox.setVisible(true);

    }

    @FXML
    private void showNoteType() {
        noteTypeBox.setVisible(true);

    }

    @FXML
    private void getSubjectsOfClass() {
        subjectsBox.setVisible(true);
        subjectsBox.getItems().clear();
        subjects = Database.getTeachesSubjects(getKeyFromValue(classes, classesBox.getSelectionModel().getSelectedItem()), UserLoggedIn.Login);
        for (Map.Entry<Integer, String> map : subjects.entrySet())
            subjectsBox.getItems().add(map.getValue());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        noteTypeBox.getItems().addAll("Kartkówka", "Sprawdzian", "Odpowiedź ustna", "Praca domowa", "Inne");
        for (double note = 1.0; note <= 6.0; note = note + 0.5)
            noteValueBox.getItems().add(note);

        for (Map.Entry<Integer, String> map : classes.entrySet())
            classesBox.getItems().add(map.getValue());

    }
}