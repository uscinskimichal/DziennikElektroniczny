package Database;

import UserInformations.Message;
import UserInformations.UserLoggedIn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class Database implements Runnable {

    private static Connection connection ;
    private static Statement statement;
    private static ResultSet resultSet;
    public static boolean isConnected;

    @Override
    public void run() {
    }

    static public void connectToTheDatabase() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://db4free.net:3306/dziennik_elektr?verifyServerCertificate=false&useSSL=false", "databaseuser", "prz_wat1");
            System.out.println("Connected!");
            Database.isConnected=true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void disconnectFromTheDatabase() {
        try {
            if (resultSet != null)
                resultSet.close();
            if (resultSet != null)
                resultSet.close();
            if (connection != null)
                connection.close();
                isConnected=false;
            System.out.println("Disconnected!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addPerson(String login, String password, String name, String surname, String sex, String permission, String status) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO Osoba VALUES ('" + login + "','"
                    + password + "','" + name + "','" + surname + "','" + sex + "','" + permission + "','" + status + "')");
            System.out.println("Person added!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ArrayList<String> getUserInfo(String login) {
        ArrayList<String> userInfo = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from Osoba where Login='" + login + "'");
            while (resultSet.next()) {
                userInfo.add(resultSet.getString("Login"));
                userInfo.add(resultSet.getString("Haslo"));
                userInfo.add(resultSet.getString("Imie"));
                userInfo.add(resultSet.getString("Nazwisko"));
                userInfo.add(resultSet.getString("Plec"));
                userInfo.add(resultSet.getString("Uprawnienia"));
                userInfo.add(resultSet.getString("Status"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userInfo;
    }

    public static ObservableList<Message> returnMessages(String login){
        ObservableList<Message> messages = FXCollections.observableArrayList();
        ArrayList<String> message = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from Wiadomosc where Odbiorca='" + login + "'");
            while (resultSet.next()) {

                message.add(resultSet.getString("ID_Wiadomosci"));
                message.add(resultSet.getString("Nadawca"));
                message.add(resultSet.getString("Odbiorca"));
                message.add(resultSet.getString("Temat"));
                message.add(resultSet.getString("Tresc"));
                message.add(resultSet.getString("StatusOdbiorcy"));
                message.add(resultSet.getString("StatusNadawcy"));
                message.add(resultSet.getString("Data"));
                messages.add(new Message(Integer.parseInt(message.get(0)),message.get(1),message.get(2),message.get(3),message.get(4),message.get(5),message.get(6),message.get(7)));
                message.clear();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return messages;
    }


}