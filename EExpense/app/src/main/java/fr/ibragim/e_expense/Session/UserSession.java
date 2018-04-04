package fr.ibragim.e_expense.Session;

import android.content.SharedPreferences;


import java.util.Map;

/**
 * Created by ibrah on 20/03/2018.
 */

public class UserSession {



    public static void setSharedPrefs(SharedPreferences session, String USER_TOKEN, String USER_EMAIL) {
        SharedPreferences.Editor editor = session.edit();
        editor.putString("USER_TOKEN", USER_TOKEN);
        editor.putString("USER_EMAIL", USER_EMAIL);
        editor.apply();
    }

    public static Map<String, ?> getUserSession(SharedPreferences session){
        Map<String, ?> res = session.getAll();
        return res;
    }


    public static Boolean CheckSession(SharedPreferences userPrefs) {
        if (userPrefs.contains("USER_EMAIL") && userPrefs.contains("USER_TOKEN")){
            return true;
        }else{
            return false;
        }
    }





}