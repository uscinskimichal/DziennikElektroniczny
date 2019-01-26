package database;

import absences.Absence;
import alerts.PopUpAlerts;
import javafx.application.Platform;
import javafx.stage.Stage;
import login.LoginWindowController;
import main.ConnectionChecker;
import main.Main;
import notes.Note;
import message.Message;
import schedule.Schedule;
import userInformations.UserLoggedIn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public class Database {

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    public static boolean isConnected;


    public static Connection getMySQLConnection() {
        return connection;
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

    public static void connectingThread() {
        new Thread( () -> {
            Database.connectToTheDatabase();
            Platform.runLater(
                    () -> LoginWindowController.pleaseWaitWindow.close()
            );
        }).start();
    }

    public static void reconnectingThread(Stage noInternet) {
        new Thread( () -> {
            Database.connectToTheDatabase();
            Platform.runLater(
                    () -> noInternet.close()
            );
        }).start();
    }

    public static ArrayList<String> getUserInfo(String login) {
        ArrayList<String> userInfo = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from Osoba where Login='" + login + "'");
            while (resultSet.next()) {
                userInfo.add(resultSet.getString("login"));
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

    public static ObservableList<Message> getReceivedMessages() {
        ObservableList<Message> messages = FXCollections.observableArrayList();
        ArrayList<String> message = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(Main.getResourceBundle().getString("DatabaseReciveMessages1") + UserLoggedIn.Login + Main.getResourceBundle().getString("DatabaseReciveMessages2") + Main.getResourceBundle().getString("DatabaseReciveMessages3") + Main.getResourceBundle().getString("DatabaseReciveMessages4"));
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

    public static ObservableList<Message> getSentMessages() {
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

    public static void deleteReceivedMessage(int messageID) {
        try {
            statement.executeUpdate("UPDATE Wiadomosc SET StatusOdbiorcy ='N' where ID_Wiadomosci='" + messageID + "'");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void deletesSentMessage(int messageID) {
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
            if (resultSet.next()) {
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
                return login.equals(resultSet.getString("login"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void addMessage(String receiver, String topic, String message) {
        try {
            statement.executeUpdate("INSERT INTO Wiadomosc VALUES (" + null + ",'" + UserLoggedIn.Login + "','" + receiver + "','" + topic + "','" + message + "','T','T'" + ",(select CURRENT_TIMESTAMP)  )");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ObservableList<String> getSubjects(int IdKlasy) {
        ObservableList<String> subjects = FXCollections.observableArrayList();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select P.Nazwa from Przedmiot P inner join Przedmiot_Klasy pk on P.ID_Przedmiotu=pk.ID_Przedmiotu inner join Klasa k on k.ID_Klasy=pk.ID_Klasy where k.ID_Klasy='" + IdKlasy + "'");
            while (resultSet.next())
                subjects.add(resultSet.getString("Nazwa"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return subjects;
    }

    public static ObservableList<Note> getNotes(String subject, String login) {
        ObservableList<Note> notes = FXCollections.observableArrayList();
        ArrayList<String> note = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select o.ID_Oceny, o.ID_Przedmiotu, o.Login, o.Wartosc, o.Typ, o.Data, o.Uwagi from Ocena o inner join Przedmiot p on o.ID_Przedmiotu=p.ID_Przedmiotu where o.Login='" + login + "' and " + "p.Nazwa='" + subject + "'");
            while (resultSet.next()) {

                note.add(resultSet.getString("ID_Oceny"));
                note.add(resultSet.getString("ID_Przedmiotu"));
                note.add(resultSet.getString("login"));
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

    public static ObservableList<Absence> getAbsences(String login) {
        ObservableList<Absence> absences = FXCollections.observableArrayList();
        ArrayList<String> absence = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select ID_Nieobecnosci, DataNB, Usprawiedliwienie from Nieobecnosc where Login='" + login + "'");
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

    public static int getMaxLessonNumber(int classID) {
        int number = 0;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select max(Numer_godziny) from Plan_Zajec where ID_Przedmiotu is not NULL and ID_Klasy='" + classID + "'");
            if (resultSet.next())
                number = resultSet.getInt("max(Numer_godziny)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return number;
    }

    public static ObservableList<Schedule> getSchedule(int classID) {
        ObservableList<Schedule> schedule = FXCollections.observableArrayList();
        ArrayList<String> hours = new ArrayList<>(Arrays.asList("8:00 - 8:45", "8:55 - 9:40", "9:50 - 10:35", "10:45 - 11:30", "11:45 - 12:30", "12:40 - 13:25", "13:35 - 14:20", "14:30 - 15:15"));
        int maxHours = getMaxLessonNumber(classID);
        ArrayList<String> singlehour = new ArrayList<>();
        for (int i = 0; i < maxHours; i++) {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery("select " +
                        "(select ifnull((select P.Nazwa from Przedmiot P inner join Plan_Zajec PZ on P.ID_Przedmiotu=PZ.ID_Przedmiotu where PZ.ID_Klasy='" + classID + "' and PZ.Numer_godziny='" + (i + 1) + "' and PZ.Dzien_tygodnia='1'), null)) as d1,\n" +
                        "(select ifnull((select P.Nazwa from Przedmiot P inner join Plan_Zajec PZ on P.ID_Przedmiotu=PZ.ID_Przedmiotu where PZ.ID_Klasy='" + classID + "'  and PZ.Numer_godziny='" + (i + 1) + "' and PZ.Dzien_tygodnia='2'), null)) as d2,\n" +
                        "(select ifnull((select P.Nazwa from Przedmiot P inner join Plan_Zajec PZ on P.ID_Przedmiotu=PZ.ID_Przedmiotu where PZ.ID_Klasy='" + classID + "'  and PZ.Numer_godziny='" + (i + 1) + "' and PZ.Dzien_tygodnia='3'), null)) as d3,\n" +
                        "(select ifnull((select P.Nazwa from Przedmiot P inner join Plan_Zajec PZ on P.ID_Przedmiotu=PZ.ID_Przedmiotu where PZ.ID_Klasy='" + classID + "' and PZ.Numer_godziny='" + (i + 1) + "' and PZ.Dzien_tygodnia='4'), null)) as d4,\n" +
                        "(select ifnull((select P.Nazwa from Przedmiot P inner join Plan_Zajec PZ on P.ID_Przedmiotu=PZ.ID_Przedmiotu where PZ.ID_Klasy='" + classID + "' and PZ.Numer_godziny='" + (i + 1) + "' and PZ.Dzien_tygodnia='5'), null)) as d5");
                while (resultSet.next()) {
                    singlehour.add(resultSet.getString("d1"));
                    singlehour.add(resultSet.getString("d2"));
                    singlehour.add(resultSet.getString("d3"));
                    singlehour.add(resultSet.getString("d4"));
                    singlehour.add(resultSet.getString("d5"));
                }
                schedule.add((new Schedule(singlehour.get(0), singlehour.get(1), singlehour.get(2), singlehour.get(3), singlehour.get(4), hours.get(i))));
                singlehour.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return schedule;

    }

    public static ObservableList<Schedule> getTeacherSchedule(String login) {
        ObservableList<Schedule> schedule = FXCollections.observableArrayList();
        ArrayList<String> hours = new ArrayList<>(Arrays.asList("8:00 - 8:45", "8:55 - 9:40", "9:50 - 10:35", "10:45 - 11:30", "11:45 - 12:30", "12:40 - 13:25", "13:35 - 14:20", "14:30 - 15:15"));
        int maxHours = 8;
        ArrayList<String> singlehour = new ArrayList<>();
        for (int i = 0; i < maxHours; i++) {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery("select " +
                        "(select ifnull((select concat(P.Nazwa,' ',K.Skrot) from Przedmiot P inner join Przedmiot_Klasy PK on PK.ID_Przedmiotu=P.ID_Przedmiotu inner join Klasa K on K.ID_Klasy=PK.ID_Klasy where PK.ID_Nauczyciela='" + login + "' and K.ID_Klasy=(select PZ.ID_Klasy from Plan_Zajec PZ where PZ.Numer_godziny='" + (i + 1) + "' and PZ.Dzien_tygodnia='1' and PK.ID_Klasy=PZ.ID_Klasy and PK.ID_Przedmiotu=PZ.ID_Przedmiotu)), null)) as d1," +
                        "(select ifnull((select concat(P.Nazwa,' ',K.Skrot) from Przedmiot P inner join Przedmiot_Klasy PK on PK.ID_Przedmiotu=P.ID_Przedmiotu inner join Klasa K on K.ID_Klasy=PK.ID_Klasy where PK.ID_Nauczyciela='" + login + "' and K.ID_Klasy=(select PZ.ID_Klasy from Plan_Zajec PZ where PZ.Numer_godziny='" + (i + 1) + "' and PZ.Dzien_tygodnia='2' and PK.ID_Klasy=PZ.ID_Klasy and PK.ID_Przedmiotu=PZ.ID_Przedmiotu)), null)) as d2," +
                        "(select ifnull((select concat(P.Nazwa,' ',K.Skrot) from Przedmiot P inner join Przedmiot_Klasy PK on PK.ID_Przedmiotu=P.ID_Przedmiotu inner join Klasa K on K.ID_Klasy=PK.ID_Klasy where PK.ID_Nauczyciela='" + login + "' and K.ID_Klasy=(select PZ.ID_Klasy from Plan_Zajec PZ where PZ.Numer_godziny='" + (i + 1) + "' and PZ.Dzien_tygodnia='3' and PK.ID_Klasy=PZ.ID_Klasy and PK.ID_Przedmiotu=PZ.ID_Przedmiotu)), null)) as d3," +
                        "(select ifnull((select concat(P.Nazwa,' ',K.Skrot) from Przedmiot P inner join Przedmiot_Klasy PK on PK.ID_Przedmiotu=P.ID_Przedmiotu inner join Klasa K on K.ID_Klasy=PK.ID_Klasy where PK.ID_Nauczyciela='" + login + "' and K.ID_Klasy=(select PZ.ID_Klasy from Plan_Zajec PZ where PZ.Numer_godziny='" + (i + 1) + "' and PZ.Dzien_tygodnia='4' and PK.ID_Klasy=PZ.ID_Klasy and PK.ID_Przedmiotu=PZ.ID_Przedmiotu)), null)) as d4," +
                        "(select ifnull((select concat(P.Nazwa,' ',K.Skrot) from Przedmiot P inner join Przedmiot_Klasy PK on PK.ID_Przedmiotu=P.ID_Przedmiotu inner join Klasa K on K.ID_Klasy=PK.ID_Klasy where PK.ID_Nauczyciela='" + login + "' and K.ID_Klasy=(select PZ.ID_Klasy from Plan_Zajec PZ where PZ.Numer_godziny='" + (i + 1) + "' and PZ.Dzien_tygodnia='5' and PK.ID_Klasy=PZ.ID_Klasy and PK.ID_Przedmiotu=PZ.ID_Przedmiotu)), null)) as d5");
                while (resultSet.next()) {
                    singlehour.add(resultSet.getString("d1"));
                    singlehour.add(resultSet.getString("d2"));
                    singlehour.add(resultSet.getString("d3"));
                    singlehour.add(resultSet.getString("d4"));
                    singlehour.add(resultSet.getString("d5"));
                }
                schedule.add((new Schedule(singlehour.get(0), singlehour.get(1), singlehour.get(2), singlehour.get(3), singlehour.get(4), hours.get(i))));
                singlehour.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return schedule;
    }

    public static ArrayList<ArrayList<String>> getChildren(String login) {
        ArrayList<ArrayList<String>> children = new ArrayList<>();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select O.Login, O.Imie, O.Nazwisko, OK.ID_Klasy, K.Skrot from Osoba O inner join RodzicOsoby RO on O.Login=RO.Oso_Login inner join Osoba_Klasa OK on O.Login=OK.Login inner join Klasa K on K.ID_Klasy=OK.ID_Klasy where RO.Login='" + login + "'");
            while (resultSet.next()) {
                ArrayList<String> child = new ArrayList<>();
                child.add(resultSet.getString("login"));
                child.add(resultSet.getString("Imie"));
                child.add(resultSet.getString("Nazwisko"));
                child.add(resultSet.getString("ID_Klasy"));
                child.add(resultSet.getString("Skrot"));
                children.add(child);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return children;

    }

    public static Map<Integer, String> getTeacherClasses(String login) {
        Map<Integer, String> classes = new LinkedHashMap<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select K.ID_Klasy, K.Skrot from Klasa K inner join Przedmiot_Klasy PK on PK.ID_Klasy=K.ID_Klasy where PK.ID_Nauczyciela='" + login + "' group by K.ID_Klasy, K.Skrot");
            while (resultSet.next()) {
                classes.put(resultSet.getInt("ID_Klasy"), resultSet.getString("Skrot"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return classes;
    }

    public static Map<Integer, String> getTeachesSubjects(int classId, String login) {
        Map<Integer, String> subjects = new LinkedHashMap<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select P.ID_Przedmiotu, P.Nazwa from Przedmiot P inner join Przedmiot_Klasy PK on P.ID_Przedmiotu=PK.ID_Przedmiotu where PK.ID_Klasy='" + classId + "' and PK.ID_Nauczyciela='" + login + "' group by P.ID_Przedmiotu, P.Nazwa");
            while (resultSet.next()) {
                subjects.put(resultSet.getInt("ID_Przedmiotu"), resultSet.getString("Nazwa"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return subjects;

    }

    public static ArrayList<ArrayList<String>> getClassMembers(int classId) {
        ArrayList<ArrayList<String>> members = new ArrayList<>();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select O.Login, O.Imie, O.Nazwisko from Osoba O inner join Osoba_Klasa OK on OK.Login=O.Login where OK.ID_Klasy='" + classId + "'");
            while (resultSet.next()) {
                ArrayList<String> member = new ArrayList<>();
                member.add(resultSet.getString("login"));
                member.add(resultSet.getString("Imie"));
                member.add(resultSet.getString("Nazwisko"));
                members.add(member);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return members;

    }

    public static void addNote(int subjectId, String login, double value, String type, String comment) {
        try {
            statement.executeUpdate("INSERT INTO Ocena VALUES (" + "NULL" + "," + subjectId + ",'" + login + "'," + value + ",'" + type + "'," + "(select CURRENT_TIMESTAMP)" + ",'" + comment + "')");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void addAbsence(String login, String teacherLogin) {
        try {
            statement.executeUpdate("INSERT INTO Nieobecnosc VALUES (" + "NULL" + ",'" + login + "','" + teacherLogin + "'," + "(select CURRENT_TIMESTAMP)" + ",'Nie')");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Map<Integer, String> getTeacherClassesILead(String login) {
        Map<Integer, String> classes = new LinkedHashMap<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select ID_Klasy, Skrot from Klasa  where ID_Wychowawcy='" + login + "' group by ID_Klasy, Skrot");
            while (resultSet.next()) {
                classes.put(resultSet.getInt("ID_Klasy"), resultSet.getString("Skrot"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return classes;
    }

    public static void justifyAbsence(int absenceID) {
        try {
            statement.executeUpdate("UPDATE Nieobecnosc SET Usprawiedliwienie = 'Tak' where ID_Nieobecnosci=" + absenceID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static ObservableList<Note> getNotesIPut(String studentId, String teacherId) {
        ObservableList<Note> notes = FXCollections.observableArrayList();
        ArrayList<String> note = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select o.ID_Oceny, o.ID_Przedmiotu, o.Login, o.Wartosc, o.Typ, o.Data, o.Uwagi from Ocena o inner join Przedmiot P on P.ID_Przedmiotu=o.ID_Przedmiotu inner join Przedmiot_Klasy PK on PK.ID_Przedmiotu=P.ID_Przedmiotu  where o.Login='" + studentId + "' and " + "PK.ID_Nauczyciela='" + teacherId + "'");
            while (resultSet.next()) {
                note.add(resultSet.getString("ID_Oceny"));
                note.add(resultSet.getString("ID_Przedmiotu"));
                note.add(resultSet.getString("login"));
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

    public static void editNote(double value, String comment, int noteId) {
        try {
            statement.executeUpdate("UPDATE Ocena SET Data=(select CURRENT_TIMESTAMP),Wartosc=" + value + ", Uwagi='" + comment + " - EDYTOWANA' where ID_Oceny=" + noteId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ArrayList<ArrayList<String>> getChildrenILead(String login) {
        ArrayList<ArrayList<String>> children = new ArrayList<>();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select O.Login, O.Imie, O.Nazwisko, K.ID_Klasy, K.Skrot from Osoba O inner join Osoba_Klasa OK on OK.Login=O.Login inner join Klasa K on K.ID_Klasy=OK.ID_Klasy WHERE K.ID_Wychowawcy='" + login + "'");
            while (resultSet.next()) {
                ArrayList<String> child = new ArrayList<>();
                child.add(resultSet.getString("login"));
                child.add(resultSet.getString("Imie"));
                child.add(resultSet.getString("Nazwisko"));
                child.add(resultSet.getString("ID_Klasy"));
                child.add(resultSet.getString("Skrot"));
                children.add(child);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return children;

    }
}