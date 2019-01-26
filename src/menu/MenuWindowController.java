package menu;

import alerts.PopUpAlerts;
import javafx.scene.control.Button;
import main.Main;
import navigator.Navigator;
import userInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.jws.soap.SOAPBinding;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class MenuWindowController extends Navigator implements Initializable {

    private void printNetwork() {
        userIp.setText(UserLoggedIn.IP);
        userCity.setText(UserLoggedIn.City);
        userCountry.setText(UserLoggedIn.Country);
    }

    private void threadsAPI() {
        new Thread(() -> {
            try {
                UserLoggedIn.IP = getIp();

                Semaphore semaphore = new Semaphore(-1);

                new Thread(() -> {
                    try {
                        UserLoggedIn.City = getData(UserLoggedIn.IP, "city");
                        semaphore.release();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                new Thread(() -> {
                    try {
                        UserLoggedIn.Country = getData(UserLoggedIn.IP, "country_name");
                        semaphore.release();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                semaphore.acquire();

                Platform.runLater(() -> {
                    userIp.setText(UserLoggedIn.IP);
                    userCity.setText(UserLoggedIn.City);
                    userCountry.setText(UserLoggedIn.Country);
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void printUser() {
        if (UserLoggedIn.Permission==1) {
            clsLabel.setVisible(true);
            classLabel.setVisible(true);
            classLabel.setText(UserLoggedIn.Class);
        }
        userLabel.setText(UserLoggedIn.Name + " " + UserLoggedIn.Surname);
    }

    private String getData(String myIp, String data) throws IOException {

        String output;
        URL city = new URL("https://ipapi.co/" + myIp + "/" + data + "/");
        BufferedReader cityBuffer = new BufferedReader(new InputStreamReader(city.openStream()));
        output = cityBuffer.readLine();
        System.out.println(output);
        return output;
    }

    private String getIp() throws IOException {
        String myIp = null;
        URL url = new URL("https://ipapi.co/ip");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        myIp = br.readLine();
        System.out.println(myIp);
        return myIp;
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
            threadsAPI();
        else
            printNetwork();
    }
}
