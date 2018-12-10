package Database;

import java.sql.*;
import java.util.ArrayList;

public class Database implements Runnable {

    private static Connection connection ;

    private static Statement statement;

    private static ResultSet resultSet;



    @Override
    public void run() {
    }


    static public void connectToTheDatabase() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://db4free.net:3306/dziennik_elektr?verifyServerCertificate=false&useSSL=false", "databaseuser", "prz_wat1");
            System.out.println("Connected!");
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
            System.out.println("Disconnected!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addPerson(String login, String password, String name, String surname, String sex, String access, String status) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO Osoba VALUES ('" + login + "','"
                    + password + "','" + name + "','" + surname + "','" + sex + "','" + access + "','" + status + "')");
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


}