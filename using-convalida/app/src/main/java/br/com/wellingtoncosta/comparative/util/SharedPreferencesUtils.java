package br.com.wellingtoncosta.comparative.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Wellington Costa on 14/09/2017.
 */
public class SharedPreferencesUtils {

    public static boolean hasUserLogged(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_logged", 0) != 0;
    }

    public static int getUserLogged(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_logged", 0);
    }

    public static void setUserLogged(Context context, int userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_logged", userId);
        editor.apply();
    }
}