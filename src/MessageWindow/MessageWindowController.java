package MessageWindow;

import Database.Database;
import UserInformations.*;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class MessageWindowController implements Initializable {

    public ObservableList<Message> messagesReceived = Database.returnReceivedMessages();
    public ObservableList<Message> messagesSent = Database.returnSentMessages();
    public Message selectedItem = null;

    @FXML
    private Label header;

    @FXML
    private JFXButton deleteMessageButton;

    @FXML
    private Tab ReceivedMessages;

    @FXML
    private Tab SentMessages;

    @FXML
    private TabPane tabPane;

    @FXML
    private JFXButton newMessageButton;

    @FXML
    private JFXButton returnButton;

    @FXML
    private TableView<Message> tableViewReceived, tableViewSent;

    @FXML
    private TableColumn<Message, String> senderColumnReceived, senderColumnSent;

    @FXML
    private TableColumn<Message, String> topicColumnReceived, topicColumnSent;

    @FXML
    private TableColumn<Message, String> dateColumnReceived, dateColumnSent;

    @FXML
    private void newMessage() {
    }

    private Stage createMessageWindow(Message message) throws IOException {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MessageWindow/ShowMessage.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ShowMessageController controller = fxmlLoader.<ShowMessageController>getController();
        controller.setMessage(message);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Wiadomość");
        stage.show();
        return stage;
    }

    @FXML
    private void showMessage() {

        if (SentMessages == tabPane.getSelectionModel().getSelectedItem()) {
            tableViewSent.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    selectedItem = tableViewSent.getSelectionModel().getSelectedItem();
                    if (selectedItem == null) return;
                    else
                        try {
                            createMessageWindow(selectedItem);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            });
        } else {
            tableViewReceived.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    selectedItem = tableViewReceived.getSelectionModel().getSelectedItem();
                    if (selectedItem == null) return;
                    else {
                        try {
                            createMessageWindow(selectedItem);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private boolean popAlertConfirmation(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.CANCEL) {
            return false;
        } else {
            return true;
        }
    }

    @FXML
    private void deleteMessage() {

        if (SentMessages == tabPane.getSelectionModel().getSelectedItem()) {
            selectedItem = tableViewSent.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz usunąć tę wiadomość?", "Usunięcie wiadomości")) {
                    Database.deleteMessageSent(selectedItem.getId());
                    tableViewSent.getItems().remove(selectedItem);
                }

            }

        } else if (ReceivedMessages == tabPane.getSelectionModel().getSelectedItem()) {
            selectedItem = tableViewReceived.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz usunąć tę wiadomość?", "Usunięcie wiadomości")) {
                    Database.deleteMessageReceived(selectedItem.getId());
                    tableViewReceived.getItems().remove(selectedItem);
                }
            }

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        senderColumnReceived.setCellValueFactory(new PropertyValueFactory<Message, String>("sender"));
        topicColumnReceived.setCellValueFactory(new PropertyValueFactory<Message, String>("topic"));
        dateColumnReceived.setCellValueFactory(new PropertyValueFactory<Message, String>("data"));
        tableViewReceived.setItems(messagesReceived);

        senderColumnSent.setCellValueFactory(new PropertyValueFactory<Message, String>("receiver"));
        topicColumnSent.setCellValueFactory(new PropertyValueFactory<Message, String>("topic"));
        dateColumnSent.setCellValueFactory(new PropertyValueFactory<Message, String>("data"));
        tableViewSent.setItems(messagesSent);

        header.setText(UserLoggedIn.Name + " , " + UserLoggedIn.Surname + " <" + UserLoggedIn.Class + ">");
    }

}
