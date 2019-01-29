package src.controller;

import src.utilities.alerts.PopUpAlerts;
import src.database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import src.utilities.navigator.Navigator;


public class NewMessageWindowController extends Navigator {

    MessageWindowController controller;

    public void setController(MessageWindowController controller){
        this.controller=controller;
    }

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
            controller.refreshMessages();
            PopUpAlerts.popAlertInformation(getResourceBundle().getString("Sucseed"), getResourceBundle().getString("MessageSendHeader"), getResourceBundle().getString("MessageSend.content"));
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.close();
        } else
            PopUpAlerts.popAlertError(getResourceBundle().getString("ErrorCommunicat"), getResourceBundle().getString("ErrorMessageHeader"), getResourceBundle().getString("MessageSend.content"));

    }

}
