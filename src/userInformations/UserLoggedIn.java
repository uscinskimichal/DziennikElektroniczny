package userInformations;

public class UserLoggedIn {
    public static String Login;
    public static String Password;
    public static String Name;
    public static String Surname;
    public static String Sex;
    public static int Permission;
    public static String Status;
    public static String Class;
    public static String ID_Klasy;
    public static String IP;
    public static String City;
    public static String Country;


    public static void eraseData(){
        Login=null;
        Password=null;
        Name=null;
        Surname=null;
        Sex=null;
        Permission=-1;
        Status=null;
        Class=null;
        ID_Klasy=null;
        IP=null;
        City=null;
        Country=null;
    }
}
