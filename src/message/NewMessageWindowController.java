package message;

import alerts.PopUpAlerts;
import database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;


public class NewMessageWindowController {

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
            PopUpAlerts.popAlertInformation(Main.getResourceBundle().getString("Sucseed"), Main.getResourceBundle().getString("MessageSendHeader"), Main.getResourceBundle().getString("MessageSend.content"));
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.close();
        } else
            PopUpAlerts.popAlertError(Main.getResourceBundle().getString("ErrorCommunicat"), Main.getResourceBundle().getString("ErrorMessageHeader"), Main.getResourceBundle().getString("MessageSend.content"));

    }

}
