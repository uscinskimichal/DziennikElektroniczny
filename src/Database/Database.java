package Database;

import Absences.Absence;
import Notes.Note;
import MessageWindow.Message;
import UserInformations.UserLoggedIn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;


public class Database implements Runnable {

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    public static boolean isConnected;

    @Override
    public void run() {
    }

    static public void connectToTheDatabase() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String user = "databaseuser";
            String password = "prz_wat1";
            connection = DriverManager.getConnection(
                    "jdbc:mysql://db4free.net:3306/dziennik_elektr?verifyServerCertificate=false&useSSL=false", user, password);
            System.out.println("Connected!");
            Database.isConnected = true;
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

    public static ObservableList<Message> returnReceivedMessages() {
        ObservableList<Message> messages = FXCollections.observableArrayList();
        ArrayList<String> message = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from Wiadomosc where Odbiorca='" + UserLoggedIn.Login + "' and " + "StatusOdbiorcy='" + "T'");
            while (resultSet.next()) {

                message.add(resultSet.getString("ID_Wiadomosci"));
                message.add(resultSet.getString("Nadawca"));
                message.add(resultSet.getString("Odbiorca"));
                message.add(resultSet.getString("Temat"));
                message.add(resultSet.getString("Tresc"));
                message.add(resultSet.getString("StatusOdbiorcy"));
                message.add(resultSet.getString("StatusNadawcy"));
                message.add(resultSet.getString("Data"));
                messages.add(new Message(Integer.parseInt(message.get(0)), message.get(1), message.get(2), message.get(3), message.get(4), message.get(5), message.get(6), message.get(7)));
                message.clear();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return messages;
    }

    public static ObservableList<Message> returnSentMessages() {
        ObservableList<Message> messages = FXCollections.observableArrayList();
        ArrayList<String> message = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from Wiadomosc where Nadawca='" + UserLoggedIn.Login + "' and " + "StatusNadawcy='" + "T'");
            while (resultSet.next()) {

                message.add(resultSet.getString("ID_Wiadomosci"));
                message.add(resultSet.getString("Nadawca"));
                message.add(resultSet.getString("Odbiorca"));
                message.add(resultSet.getString("Temat"));
                message.add(resultSet.getString("Tresc"));
                message.add(resultSet.getString("StatusOdbiorcy"));
                message.add(resultSet.getString("StatusNadawcy"));
                message.add(resultSet.getString("Data"));
                messages.add(new Message(Integer.parseInt(message.get(0)), message.get(1), message.get(2), message.get(3), message.get(4), message.get(5), message.get(6), message.get(7)));
                message.clear();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return messages;
    }

    public static void deleteMessageReceived(int messageID) {
        try {
            statement.executeUpdate("UPDATE Wiadomosc SET StatusOdbiorcy ='N' where ID_Wiadomosci='" + messageID + "'");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void deleteMessageSent(int messageID) {
        try {
            statement.executeUpdate("UPDATE Wiadomosc SET StatusNadawcy ='N' where ID_Wiadomosci='" + messageID + "'");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static ArrayList<String> getNameAndSurname(String login) {
        ArrayList<String> userInfo = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select Imie,Nazwisko from Osoba where Login='" + login + "'");
            while (resultSet.next()) {
                userInfo.add(resultSet.getString("Imie"));
                userInfo.add(resultSet.getString("Nazwisko"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userInfo;
    }

    public static ArrayList<String> getUserClass() {
        ArrayList userClass = new ArrayList();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select k.Skrot, k.ID_Klasy from Klasa k inner join Osoba_Klasa ok on k.ID_Klasy=ok.ID_Klasy where ok.Login='" + UserLoggedIn.Login + "'");
            if (resultSet.next()){
                userClass.add(resultSet.getString("Skrot"));
                userClass.add(resultSet.getString("ID_Klasy"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userClass;
    }

    public static void changePassword(String login, String newPassword) {
        try {
            statement.executeUpdate("UPDATE Osoba SET Haslo = '" + newPassword + "' where Login='" + login + "'");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static boolean checkIfLoginExist(String login) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select Login from Osoba where Login='" + login + "'");
            if (resultSet.next())
                return login.equals(resultSet.getString("Login"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private static int countMessages() {
        int number = 0;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select count( DISTINCT ID_Wiadomosci) + 1 from Wiadomosc");
            if (resultSet.next())
                number = resultSet.getInt("count( DISTINCT ID_Wiadomosci) + 1");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return number;
    }

    public static void addMessage(String receiver, String topic, String message) {
        {
            try {
                statement.executeUpdate("INSERT INTO Wiadomosc VALUES (" + countMessages() + ",'" + UserLoggedIn.Login + "','" + receiver + "','" + topic + "','" + message + "','T','T'" + ",(select CURRENT_TIMESTAMP)  )");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public static ObservableList<String> getSubjects(int IdKlasy){
        ObservableList<String> subjects = FXCollections.observableArrayList();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select P.Nazwa from Przedmiot P inner join Przedmiot_Klasy pk on P.ID_Przedmiotu=pk.ID_Przedmiotu inner join Klasa k on k.ID_Klasy=pk.ID_Klasy where k.ID_Klasy='" + IdKlasy +"'");
            while (resultSet.next())
                subjects.add(resultSet.getString("Nazwa"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return subjects;
    }

    public static ObservableList<Note> getNotes(String subject){
        ObservableList<Note> notes = FXCollections.observableArrayList();
        ArrayList<String> note = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select o.ID_Oceny, o.ID_Przedmiotu, o.Login, o.Wartosc, o.Typ, o.Data, o.Uwagi from Ocena o inner join Przedmiot p on o.ID_Przedmiotu=p.ID_Przedmiotu where o.Login='" + UserLoggedIn.Login + "' and " + "p.Nazwa='" +subject +"' and o.Typ!='Końcowa'");
            while (resultSet.next()) {

                note.add(resultSet.getString("ID_Oceny"));
                note.add(resultSet.getString("ID_Przedmiotu"));
                note.add(resultSet.getString("Login"));
                note.add(resultSet.getString("Wartosc"));
                note.add(resultSet.getString("Typ"));
                note.add(resultSet.getString("Data"));
                note.add(resultSet.getString("Uwagi"));
                notes.add(new Note(Integer.parseInt(note.get(0)), Integer.parseInt(note.get(1)), note.get(2), Double.parseDouble(note.get(3)), note.get(4), note.get(5), note.get(6)));
                note.clear();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return notes;
    }

    public static ObservableList<Absence> getAbsences(){
        ObservableList<Absence> absences = FXCollections.observableArrayList();
        ArrayList<String> absence = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select ID_Nieobecnosci, DataNB, Usprawiedliwienie from Nieobecnosc where Login='" + UserLoggedIn.Login + "'");
            while (resultSet.next()) {
                absence.add(resultSet.getString("ID_Nieobecnosci"));
                absence.add(resultSet.getString("DataNB"));
                absence.add(resultSet.getString("Usprawiedliwienie"));
                absences.add(new Absence(Integer.parseInt(absence.get(0)), absence.get(1), absence.get(2)));
                absence.clear();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return absences;

    }

    public static String getFinalNote(String subject){
        String note=null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select o.Wartosc from Ocena o inner join Przedmiot p on o.ID_Przedmiotu=p.ID_Przedmiotu where o.Login='" + UserLoggedIn.Login + "' and " + "p.Nazwa='" +subject +"' and o.Typ='Końcowa'");
            if (resultSet.next())
            note=resultSet.getString("Wartosc");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return note;
    }
}