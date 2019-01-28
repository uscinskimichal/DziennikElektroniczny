package userInformations;

public class UserLoggedIn {
    public static String LOGIN;
    public static String PASSWORD;
    public static String NAME;
    public static String SURNAME;
    public static String SEX;
    public static int PERMISSION;
    public static String STATUS;
    public static String CLASS;
    public static String ID_KLASY;
    public static String IP;
    public static String CITY;
    public static String COUNTRY;


    public static void eraseData(){
        LOGIN =null;
        PASSWORD =null;
        NAME =null;
        SURNAME =null;
        SEX =null;
        PERMISSION =-1;
        STATUS =null;
        CLASS =null;
        ID_KLASY =null;
        IP=null;
        CITY =null;
        COUNTRY =null;
    }
}
