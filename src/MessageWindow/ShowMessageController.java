package MessageWindow;

import Database.Database;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShowMessageController implements Initializable {

    private Message message;

    void setMessage(Message message) {
        this.message = message;
    }

    @FXML
    private Label topic;

    @FXML
    private Label from;

    @FXML
    private Label to;

    @FXML
    private TextArea messageDisplay;

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
            //to.setText(message.getReceiver());
            //from.setText(message.getSender());
            messageDisplay.setText(message.getMessage());
        });

    }

    @FXML
    private void replyMessage() throws IOException {
        Stage newMessage = new Stage();
        newMessage.initModality(Modality.APPLICATION_MODAL);
        Platform.setImplicitExit(false);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MessageWindow/NewMessageWindow.fxml"));
        Parent root = fxmlLoader.load();
        NewMessageWindowController controller = fxmlLoader.getController();
        controller.setReceiver(message.getSender());

        Scene scene = new Scene(root);
        newMessage.setScene(scene);
        newMessage.setTitle("Nowa wiadomość");
        newMessage.setResizable(false);
        newMessage.show();
    }


}
