package main.api;

import javafx.application.Platform;
import javafx.scene.control.Label;
import main.utilities.userInformations.UserLoggedIn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Semaphore;

public class IpInformation {

    public IpInformation(Label userIp, Label userCity, Label userCountry){
        new Thread(() -> {
            try {
                UserLoggedIn.IP = getIp();

                Semaphore semaphore = new Semaphore(-1);

                new Thread(() -> {
                    try {
                        UserLoggedIn.CITY = getData(UserLoggedIn.IP, "city");
                        semaphore.release();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                new Thread(() -> {
                    try {
                        UserLoggedIn.COUNTRY = getData(UserLoggedIn.IP, "country_name");
                        semaphore.release();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                semaphore.acquire();

                Platform.runLater(() -> {
                    userIp.setText(UserLoggedIn.IP);
                    userCity.setText(UserLoggedIn.CITY);
                    userCountry.setText(UserLoggedIn.COUNTRY);
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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
        String myIp;
        URL url = new URL("https://ipapi.co/ip");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        myIp = br.readLine();
        System.out.println(myIp);
        return myIp;
    }

}
