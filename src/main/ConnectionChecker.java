package main;

import database.Database;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import userInformations.UserLoggedIn;

public class ConnectionChecker implements Runnable {
    public static boolean isInternetConnected;
    private Stage noInternet = null;
    private boolean isPopped=false;

    ConnectionChecker() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Process process = java.lang.Runtime.getRuntime().exec("ping www.google.com");
                int x = process.waitFor();
                if (x == 0) {
                    isInternetConnected = true;
                    System.out.println("Connection Successful, "
                            + "Output was " + x);

                    if (isPopped) {
                        isPopped=false;
                        Platform.runLater(
                                () -> Database.reconnectingThread(noInternet)
                        );
                    }
                } else {
                    isInternetConnected = false;
                    System.out.println("Internet Not Connected, "
                            + "Output was " + x);
                    if (UserLoggedIn.Login != null && !isPopped) {
                        isPopped=true;
                        Platform.runLater(
                                () -> {
                                    noInternet = new Stage();
                                    noInternet.initStyle(StageStyle.UNDECORATED);
                                    noInternet.initModality(Modality.APPLICATION_MODAL);
                                    noInternet.setOnCloseRequest(Event::consume);
                                    Platform.setImplicitExit(false);
                                    Main.changeScene("/alerts/NoInternet.fxml", "Please wait...", noInternet);
                                    noInternet.showAndWait();
                                }
                        );
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
