package Menu;

import Alerts.PopUpAlerts;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class MenuWindowController implements Initializable {

    private void printNetwork() {
        userIp.setText(UserLoggedIn.IP);
        userCity.setText(UserLoggedIn.City);
        userCountry.setText(UserLoggedIn.Country);
    }

    private void threadStart() {
        new Thread(() -> {
            try {
                UserLoggedIn.IP = getIp();

                Semaphore s1 = new Semaphore(-1);

                new Thread(() -> {
                    try {
                        UserLoggedIn.City = getData(UserLoggedIn.IP, "city");
                        s1.release();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                new Thread(() -> {
                    try {
                        UserLoggedIn.Country = getData(UserLoggedIn.IP, "country_name");
                        s1.release();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                s1.acquire();

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
        if (UserLoggedIn.Permission.equals("Uczen")) {
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

    @FXML
    private void backToMenu() {
        Main.changeScene("/Menu/MenuWindow.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
    }

    @FXML
    private void goToAbsences() {
        if (UserLoggedIn.Permission.equals("Uczen"))
            Main.changeScene("/Absences/AbsenceWindow.fxml", "Nieobecności", Main.getPrimaryStage());
        else if (UserLoggedIn.Permission.equals("Rodzic"))
            Main.changeScene("/Absences/AbsenceWindowParent.fxml", "Nieobecności", Main.getPrimaryStage());
        else
            Main.changeScene("/Absences/CheckAbsenceWindow.fxml", "Nieobecności", Main.getPrimaryStage());
    }

    @FXML
    private void goToChangePassword() {
        Stage changePassword = new Stage();
        changePassword.initModality(Modality.APPLICATION_MODAL);
        changePassword.getIcons().add(new Image("file:./resources/images/password_icon.png"));
        Platform.setImplicitExit(false);
        Main.changeScene("/Menu/ChangePasswordWindow.fxml", "Zmień hasło", changePassword);
        changePassword.show();
    }

    @FXML
    private void goToMessages() {
        Main.changeScene("/Message/MessageWindow.fxml", "Wiadomości", Main.getPrimaryStage());
    }

    @FXML
    private void goToNotes() {
        if (UserLoggedIn.Permission.equals("Uczen"))
            Main.changeScene("/Notes/NotesWindow.fxml", "Twoje oceny", Main.getPrimaryStage());
        else if (UserLoggedIn.Permission.equals("Rodzic"))
            Main.changeScene("/Notes/NotesWindowParent.fxml", "Oceny", Main.getPrimaryStage());
        else
            Main.changeScene("/Notes/AddNoteWindow.fxml", "Oceny", Main.getPrimaryStage());
    }

    @FXML
    private void goToSchedule() {
        if (UserLoggedIn.Permission.equals("Rodzic"))
            Main.changeScene("/Schedule/ScheduleWindowParent.fxml", "Plan zajęć", Main.getPrimaryStage());
        else
            Main.changeScene("/Schedule/ScheduleWindow.fxml", "Plan zajęć", Main.getPrimaryStage());
    }

    @FXML
    private void logout() {

        if (PopUpAlerts.popAlertConfirmation("Czy jesteś pewien?", "Czy na pewno chcesz się wylogować?", "Wyloguj")) {
            Main.changeScene("/Login/LoginWindowController.fxml", "Dziennik Elektroniczny", Main.getPrimaryStage());
            UserLoggedIn.eraseData();
        }
    }

    @FXML
    private void changePassword() {
        Stage changePassword = new Stage();
        changePassword.initModality(Modality.APPLICATION_MODAL);
        changePassword.getIcons().add(new Image("file:./resources/images/password_icon.png"));
        Platform.setImplicitExit(false);
        Main.changeScene("/Menu/ChangePasswordWindow.fxml", "Zmień hasło", changePassword);
        changePassword.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (UserLoggedIn.IP == null)
            threadStart();
        else
            printNetwork();
        printUser();
    }
}
