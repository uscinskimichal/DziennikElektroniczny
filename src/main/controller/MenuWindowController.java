package main.controller;

import main.api.IpInformation;
import main.utilities.navigator.Navigator;
import main.utilities.userInformations.UserLoggedIn;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

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
