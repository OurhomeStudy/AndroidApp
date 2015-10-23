package Items;

/**
 * Created by cho on 2015-09-21.
 */
public class StaticVariable {


    private static boolean userLogined = false;
    private static String user_id = "NULL";
    private static String user_name = "NULL";

    private static String connectUrl = "https://220.72.126.199:15443/";

    public static boolean getUserLogined() {
        return userLogined;
    }

    public static void setUserLogined(boolean userLogined) {
        StaticVariable.userLogined = userLogined;
    }

    public static String getUser_id() {
        return user_id;
    }

    public static void setUser_id(String user_id) {
        StaticVariable.user_id = user_id;
    }

    public static String getUser_name() {
        return user_name;
    }

    public static void setUser_name(String user_name) {
        StaticVariable.user_name = user_name;
    }

    public static String getConnectUrl() {
        return connectUrl;
    }

}
