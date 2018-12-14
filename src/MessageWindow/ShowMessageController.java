package MessageWindow;

import Database.Database;
import UserInformations.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShowMessageController implements Initializable {

    Message message;

    public void setMessage(Message message) {
        this.message = message;
    }

    @FXML
    private Label topic;

    @FXML
    private Label from;

    @FXML
    private Label to;

    @FXML
    private Label messageDisplay;

    @FXML
    private Button returnButton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void exit() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(() -> {
            ArrayList<String> nameAndSurnameSender = Database.getNameAndSurname(message.getSender());
            ArrayList<String> nameAndSurnameReceiver = Database.getNameAndSurname(message.getReceiver());
            topic.setText(message.getTopic());
            to.setText(message.getReceiver() + " , <" + nameAndSurnameReceiver.get(0) + " " + nameAndSurnameReceiver.get(1) + ">");
            from.setText(message.getSender() + " , <" + nameAndSurnameSender.get(0) + " " + nameAndSurnameSender.get(1) + ">");
            messageDisplay.setText(message.getMessage());
        });

    }
}
