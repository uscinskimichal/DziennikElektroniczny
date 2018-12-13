package MainWindow;

import UserInformations.Message;
import Database.Database;
import UserInformations.UserLoggedIn;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {


    ObservableList<Message> messages = Database.returnMessages(UserLoggedIn.Login);
    @FXML
    private TableView<Message> tableView;

    @FXML
    private TableColumn<Message,String> senderColumn;

    @FXML
    private TableColumn<Message,String> topicColumn;

    @FXML
    private TableColumn<Message,String> dateColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        senderColumn.setCellValueFactory(new PropertyValueFactory<Message,String>("sender"));
        topicColumn.setCellValueFactory(new PropertyValueFactory<Message,String>("topic"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Message,String>("data"));
        tableView.setItems(messages);
    }

}
