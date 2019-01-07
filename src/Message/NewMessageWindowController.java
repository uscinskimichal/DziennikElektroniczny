package Message;

import Alerts.PopUpAlerts;
import Database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class NewMessageWindowController {

    void setReceiver(String receiver) {
        toPerson.setText(receiver);
        toPerson.setEditable(false);
    }

    @FXML
    private TextField toPerson;

    @FXML
    private TextField topic;

    @FXML
    private TextArea message;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void backToMessageWindow() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void sendMessage() {
        String receiver = toPerson.getText();
        String messageInterior = message.getText();
        String title = topic.getText();

        if (Database.checkIfLoginExist(receiver)) {
            Database.addMessage(receiver, title, messageInterior);
            PopUpAlerts.popAlertInformation("Sukces!", "Twoja wiadomość została pomyślnie wysłana!", "Wyślij wiadomość");
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.close();
        } else
            PopUpAlerts.popAlertError("Błąd!", "Nie można wysłać wiadmości - nie ma osoby o określonym loginie!", "Wyślij wiadomość");

    }

}
