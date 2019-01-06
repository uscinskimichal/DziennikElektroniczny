package MessageWindow;

import Alerts.PopUpAlerts;
import Database.Database;
import Main.Main;
import UserInformations.*;
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
import java.util.ResourceBundle;


public class MessageWindowController implements Initializable {

    private ObservableList<Message> messagesReceived = Database.returnReceivedMessages();
    private ObservableList<Message> messagesSent = Database.returnSentMessages();
    private Message selectedItem = null;

    @FXML
    private Label header;

    @FXML
    private Tab ReceivedMessages;

    @FXML
    private Tab SentMessages;

    @FXML
    private TabPane tabPane;

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
        Stage newMessageStage = new Stage();
        newMessageStage.initModality(Modality.APPLICATION_MODAL);
        Platform.setImplicitExit(false);
        Main.changeScene("/MessageWindow/NewMessageWindow.fxml", "Nowa wiadomość", newMessageStage);
        newMessageStage.show();
    }

    private void createMessageWindow(Message message) throws IOException {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MessageWindow/ShowMessage.fxml"));
        Parent root = fxmlLoader.load();
        ShowMessageController controller = fxmlLoader.getController();
        controller.setMessage(message);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Wiadomość");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void showMessage() {

        if (SentMessages == tabPane.getSelectionModel().getSelectedItem()) {
            tableViewSent.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    selectedItem = tableViewSent.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        try {
                            createMessageWindow(selectedItem);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            tableViewReceived.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    selectedItem = tableViewReceived.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
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

    @FXML
    private void deleteMessage() {

        if (SentMessages == tabPane.getSelectionModel().getSelectedItem()) {
            selectedItem = tableViewSent.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (PopUpAlerts.popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz usunąć tę wiadomość?", "Usunięcie wiadomości")) {
                    Database.deleteMessageSent(selectedItem.getId());
                    tableViewSent.getItems().remove(selectedItem);
                }

            }

        } else if (ReceivedMessages == tabPane.getSelectionModel().getSelectedItem()) {
            selectedItem = tableViewReceived.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (PopUpAlerts.popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz usunąć tę wiadomość?", "Usunięcie wiadomości")) {
                    Database.deleteMessageReceived(selectedItem.getId());
                    tableViewReceived.getItems().remove(selectedItem);
                }
            }

        }

    }

    @FXML
    private void refreshMessages() {
        messagesReceived = Database.returnReceivedMessages();
        messagesSent = Database.returnSentMessages();
        tableViewReceived.setItems(messagesReceived);
        tableViewSent.setItems(messagesSent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        senderColumnReceived.setCellValueFactory(new PropertyValueFactory<>("sender"));
        topicColumnReceived.setCellValueFactory(new PropertyValueFactory<>("topic"));
        dateColumnReceived.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableViewReceived.setItems(messagesReceived);

        senderColumnSent.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        topicColumnSent.setCellValueFactory(new PropertyValueFactory<>("topic"));
        dateColumnSent.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableViewSent.setItems(messagesSent);

        header.setText(UserLoggedIn.Name + " , " + UserLoggedIn.Surname + " <" + UserLoggedIn.Class + ">");
    }

    @FXML
    private void goToMenu() {
        Main.changeScene("/Menu/MenuWindow.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
    }


}
