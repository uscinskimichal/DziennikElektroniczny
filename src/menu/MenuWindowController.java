package menu;

import main.IpInformation;
import navigator.Navigator;
import userInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class MenuWindowController extends Navigator implements Initializable {

    private void printNetwork() {
        userIp.setText(UserLoggedIn.IP);
        userCity.setText(UserLoggedIn.CITY);
        userCountry.setText(UserLoggedIn.COUNTRY);
    }

    private void shootAPI() {
        new IpInformation(userIp,userCity,userCountry);
    }

    private void printUser() {
        if (UserLoggedIn.PERMISSION ==1) {
            clsLabel.setVisible(true);
            classLabel.setVisible(true);
            classLabel.setText(UserLoggedIn.CLASS);
        }
        userLabel.setText(UserLoggedIn.NAME + " " + UserLoggedIn.SURNAME);
    }

    @FXML
    private Label userCountry;

    @FXML
    private Label userCity;

    @FXML
    private Label userIp;

    @FXML
    private Label userLabel;

    @FXML
    private Label classLabel;

    @FXML
    private Text clsLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        printUser();
        if (UserLoggedIn.IP == null)
            shootAPI();
        else
            printNetwork();
    }
}
