package main.controller;

import main.utilities.alerts.PopUpAlerts;
import main.database.Database;
import main.utilities.data.Message;
import main.utilities.navigator.Navigator;
import main.utilities.userInformations.UserLoggedIn;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MessageWindowController extends Navigator implements Initializable {

    public ObservableList<Message> messagesReceived = Database.getReceivedMessages();
    public ObservableList<Message> messagesSent = Database.getSentMessages();
    public Message selectedItem = null;

    private void printUser() {
        if (UserLoggedIn.PERMISSION ==1) {
            clsLabel.setVisible(true);
            classLabel.setVisible(true);
            classLabel.setText(UserLoggedIn.CLASS);
        }
        userLabel.setText(UserLoggedIn.NAME + " " + UserLoggedIn.SURNAME);
    }

    @FXML
    private Label userLabel;

    @FXML
    private Label classLabel;

    @FXML
    private Text clsLabel;

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
    private void newMessage() throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/fxml/NewMessageWindow.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        NewMessageWindowController controller = fxmlLoader.<NewMessageWindowController>getController();
        controller.setController(this);
        stage.getIcons().add(new Image("file:./resources/images/message_icon.png"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(getResourceBundle().getString("NewMessage"));
        stage.setResizable(false);
        stage.show();
    }

    private Stage createMessageWindow(Message message) throws IOException {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/fxml/ShowMessageWindow.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ShowMessageWindowController controller = fxmlLoader.<ShowMessageWindowController>getController();
        controller.setMessage(message);
        controller.setController(this);
        stage.getIcons().add(new Image("file:./resources/images/message_icon.png"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(getResourceBundle().getString("NewMessage"));
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
                if (PopUpAlerts.popAlertConfirmation(getResourceBundle().getString("AreYouSureTitle"), getResourceBundle().getString("AreYouSureDeleteMessage"), getResourceBundle().getString("DelateMessageContent"))) {
                    new Thread(() -> Database.deletesSentMessage(selectedItem.getId())).start();
                    tableViewSent.getItems().remove(selectedItem);
                }

            }

        } else if (ReceivedMessages == tabPane.getSelectionModel().getSelectedItem()) {
            selectedItem = tableViewReceived.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (PopUpAlerts.popAlertConfirmation(getResourceBundle().getString("AreYouSureTitle"), getResourceBundle().getString("AreYouSureDeleteMessage"), getResourceBundle().getString("DelateMessageContent"))) {
                    new Thread(() -> Database.deleteReceivedMessage(selectedItem.getId())).start();
                    tableViewReceived.getItems().remove(selectedItem);
                }
            }

        }
        if (messagesReceived.isEmpty())
            tableViewReceived.setPlaceholder(new Label(getResourceBundle().getString("NoMessages")));
        if (messagesSent.isEmpty()) tableViewSent.setPlaceholder(new Label(getResourceBundle().getString("NoMessages")));

    }

    public void refreshMessages() {
        messagesReceived = Database.getReceivedMessages();
        messagesSent = Database.getSentMessages();
        tableViewReceived.setItems(messagesReceived);
        tableViewSent.setItems(messagesSent);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        printUser();
        senderColumnReceived.setCellValueFactory(new PropertyValueFactory<Message, String>("sender"));
        topicColumnReceived.setCellValueFactory(new PropertyValueFactory<Message, String>("topic"));
        dateColumnReceived.setCellValueFactory(new PropertyValueFactory<Message, String>("data"));
        tableViewReceived.setItems(messagesReceived);

        senderColumnSent.setCellValueFactory(new PropertyValueFactory<Message, String>("receiver"));
        topicColumnSent.setCellValueFactory(new PropertyValueFactory<Message, String>("topic"));
        dateColumnSent.setCellValueFactory(new PropertyValueFactory<Message, String>("data"));
        tableViewSent.setItems(messagesSent);

        if (messagesReceived.isEmpty())
            tableViewReceived.setPlaceholder(new Label(getResourceBundle().getString("NoMessages")));
        if (messagesSent.isEmpty()) tableViewSent.setPlaceholder(new Label(getResourceBundle().getString("NoMessages")));
    }
}
