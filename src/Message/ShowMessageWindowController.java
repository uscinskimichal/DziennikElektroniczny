package Message;

import Database.Database;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShowMessageWindowController implements Initializable {

    private Message message;
    private MessageWindowController messageWindowController;

    void setController(MessageWindowController messageWindowController) {
        this.messageWindowController = messageWindowController;
    }

    void setMessage(Message message) {
        this.message = message;
    }

    @FXML
    private TextField topic;

    @FXML
    private TextField from;

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
            topic.setText(message.getTopic());
            from.setText(message.getSender() + " , <" + nameAndSurnameSender.get(0) + " " + nameAndSurnameSender.get(1) + ">");
            messageDisplay.setText(message.getMessage());
        });

    }

    @FXML
    private void replyMessage() throws IOException {
        Stage newMessage = new Stage();
        newMessage.initModality(Modality.APPLICATION_MODAL);
        Platform.setImplicitExit(false);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Message/NewMessageWindow.fxml"));
        Parent root = fxmlLoader.load();
        NewMessageWindowController controller = fxmlLoader.getController();
        controller.setReceiver(message.getSender());
        controller.setController(messageWindowController);

        Scene scene = new Scene(root);
        newMessage.setScene(scene);
        newMessage.setTitle("Nowa wiadomość");
        newMessage.setResizable(false);
        newMessage.show();
    }


}
