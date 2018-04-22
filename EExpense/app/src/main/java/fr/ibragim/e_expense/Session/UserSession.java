package fr.ibragim.e_expense.Session;

import android.content.SharedPreferences;


import java.util.Map;

/**
 * Created by ibrah on 20/03/2018.
 */

public class UserSession {



    public static void setSharedPrefs(SharedPreferences session, String USER_EMAIL, String USER_PASS) {
        SharedPreferences.Editor editor = session.edit();
        editor.putString("USER_EMAIL", USER_EMAIL);
        editor.putString("USER_PASS", USER_PASS);
        editor.apply();
    }

    public static Map<String, ?> getUserSession(SharedPreferences session){
        Map<String, ?> res = session.getAll();
        return res;
    }


    public static Boolean CheckSession(SharedPreferences userPrefs) {
        if (userPrefs.contains("USER_EMAIL") && userPrefs.contains("USER_PASS")){
            return true;
        }else{
            return false;
        }
    }





}