package Message;

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

    public ObservableList<Message> messagesReceived = Database.getReceivedMessages();
    public ObservableList<Message> messagesSent = Database.getSentMessages();
    public Message selectedItem = null;

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
        Main.changeScene("/Message/NewMessageWindow.fxml", "Nowa wiadomość", newMessageStage);
        newMessageStage.show();
    }

    private Stage createMessageWindow(Message message) throws IOException {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Message/ShowMessageWindow.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ShowMessageWindowController controller = fxmlLoader.<ShowMessageWindowController>getController();
        controller.setMessage(message);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Wiadomość");
        stage.setResizable(false);
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

    @FXML
    private void deleteMessage() {

        if (SentMessages == tabPane.getSelectionModel().getSelectedItem()) {
            selectedItem = tableViewSent.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (PopUpAlerts.popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz usunąć tę wiadomość?", "Usunięcie wiadomości")) {
                    Database.deletesSentMessage(selectedItem.getId());
                    tableViewSent.getItems().remove(selectedItem);
                }

            }

        } else if (ReceivedMessages == tabPane.getSelectionModel().getSelectedItem()) {
            selectedItem = tableViewReceived.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (PopUpAlerts.popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz usunąć tę wiadomość?", "Usunięcie wiadomości")) {
                    Database.deleteReceivedMessage(selectedItem.getId());
                    tableViewReceived.getItems().remove(selectedItem);
                }
            }

        }

    }

    @FXML
    private void refreshMessages() {
        messagesReceived = Database.getReceivedMessages();
        messagesSent = Database.getSentMessages();
        tableViewReceived.setItems(messagesReceived);
        tableViewSent.setItems(messagesSent);
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

        header.setText(UserLoggedIn.Name + " , " + UserLoggedIn.Surname);

        if (messagesReceived.isEmpty())
            tableViewReceived.setPlaceholder(new Label("Brak wiadomości"));
        if (messagesSent.isEmpty()) tableViewSent.setPlaceholder(new Label("Brak wiadomości"));
    }

    @FXML
    private void goToMenu() {
        Main.changeScene("/Menu/MenuWindow.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
    }


}
