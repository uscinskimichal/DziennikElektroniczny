package src.controller;

import src.utilities.alerts.PopUpAlerts;
import src.database.Database;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.utilities.data.Note;
import src.utilities.navigator.Navigator;

import java.net.URL;
import java.util.ResourceBundle;

public class EditNoteWindowController extends Navigator implements Initializable {


    private CheckNotesIPutWindowController controller;
    private Note note;

    public void setNote(Note note) {
        this.note = note;
    }

    public void setController(CheckNotesIPutWindowController controller) {
        this.controller = controller;
    }

    @FXML
    private void enableButton(){
        editNoteButton.setDisable(false);
    }

    @FXML
    private Button editNoteButton;

    @FXML
    private ComboBox<Double> valueBox;

    @FXML
    private TextField commentText;

    @FXML
    void editNote() {
        new Thread(() -> Database.editNote(valueBox.getSelectionModel().getSelectedItem(), commentText.getText(), note.getNoteId())).start();
        controller.notes.get(controller.tableView.getSelectionModel().getSelectedIndex()).setComment(commentText.getText() + getResourceBundle().getString("Edited"));
        controller.notes.get(controller.tableView.getSelectionModel().getSelectedIndex()).setValue(valueBox.getSelectionModel().getSelectedItem());
        controller.notes.set(controller.tableView.getSelectionModel().getSelectedIndex(),controller.tableView.getSelectionModel().getSelectedItem());
        controller.refreshNotes();
        PopUpAlerts.popAlertInformation(getResourceBundle().getString("Sucseed"),getResourceBundle().getString("EditNoteHeader"),getResourceBundle().getString("EditNoteContent"));
        returnToMenu();
    }

    @FXML
    void returnToMenu() {
        Stage stage = (Stage) valueBox.getScene().getWindow();
        stage.close();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (double note = 1.0; note <= 6.0; note = note + 0.5)
            valueBox.getItems().add(note);

    }
}
