package Items;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Song on 2015-10-23.
 */
public class GeneralPreferences {           //공유프리퍼런스를 사용하기 위한 클래스

    private static final String PREF_FILENAME = "UserSession";

    static Context mContext;

    public GeneralPreferences(Context c){   //각 Activity의 Context를 받아옴.
        mContext = c;
    }

    public static void putString(String key, String value){
        SharedPreferences getPref = mContext.getSharedPreferences(PREF_FILENAME, 0);
        SharedPreferences.Editor editor = getPref.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static void putInt(String key, int value){
        SharedPreferences getPref = mContext.getSharedPreferences(PREF_FILENAME, 0);
        SharedPreferences.Editor editor = getPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putBoolean(String key, boolean value){
        SharedPreferences getPref = mContext.getSharedPreferences(PREF_FILENAME, 0);
        SharedPreferences.Editor editor = getPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(String key){
        SharedPreferences getPref = mContext.getSharedPreferences(PREF_FILENAME, 0);
        return getPref.getString(key,"");
    }

    public static int getInt(String key, int defalutValue){
        SharedPreferences getPref = mContext.getSharedPreferences(PREF_FILENAME, 0);
        return getPref.getInt(key, defalutValue);
    }

    public static Boolean getBoolean(String key, boolean defalutValue){
        SharedPreferences getPref = mContext.getSharedPreferences(PREF_FILENAME, 0);
        return getPref.getBoolean(key, defalutValue);
    }

    public static void removeData(String key){
        SharedPreferences getPref = mContext.getSharedPreferences(PREF_FILENAME, 0);
        SharedPreferences.Editor editor = getPref.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void removeAll(){
        SharedPreferences getPref = mContext.getSharedPreferences(PREF_FILENAME, 0);
        SharedPreferences.Editor editor = getPref.edit();
        editor.clear();
        editor.commit();
    }

}
