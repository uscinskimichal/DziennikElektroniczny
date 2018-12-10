package LoginWindow;


import Database.Database;
import Main.Main;
import UserInformations.UserLoggedIn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.ArrayList;


public class LoginController {


    @FXML
    private Button LogIn;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private TextField LoginField;

    @FXML
    void logIn(ActionEvent event) {

        ArrayList<String> userInfo = Database.getUserInfo(LoginField.getText());
        System.out.println(userInfo.size());
        if (userInfo.size() != 0) {
            if (userInfo.get(1).equals(PasswordField.getText())) {

                UserLoggedIn.Login = userInfo.get(0);
                UserLoggedIn.Haslo = userInfo.get(1);
                UserLoggedIn.Imie = userInfo.get(2);
                UserLoggedIn.Nazwisko = userInfo.get(3);
                UserLoggedIn.Plec = userInfo.get(4);
                UserLoggedIn.Uprawnienia = userInfo.get(5);
                UserLoggedIn.Status = userInfo.get(6);
                Main.changeScene("/LoginWindow/Login.fxml", "Witaj!");
                System.out.println("Success");
            }
        } else {
            System.out.println("Error!");
        }


    }

}


