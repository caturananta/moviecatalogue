package co.id.dicoding.moviecatalogueapi.util;

import android.content.Context;
import android.content.SharedPreferences;

import co.id.dicoding.moviecatalogueapi.constant.Constant;

public class PreferencesUtil {

    public static void save(Context context, String key, Boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.KEY_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static Boolean getBoolean(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.KEY_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public static void save(Context context, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.KEY_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.KEY_PREFS, Context.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

    private static final String LEN_PREFIX = "Count_";
    private static final String VAL_PREFIX = "IntValue_";

    public static void  saveIntArray(Context context, String key, int[] value) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.KEY_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(LEN_PREFIX + key, value.length);
        int count = 0;
        for (int i : value) {
            editor.putInt(VAL_PREFIX + key + count++, i);
        }
        editor.apply();
    }

    public static int[] getIntArray(Context context, String key) {
        int[] result;
        SharedPreferences prefs = context.getSharedPreferences(Constant.KEY_PREFS, Context.MODE_PRIVATE);
        int count = prefs.getInt(LEN_PREFIX + key, 0);
        result = new int[count];
        for (int i = 0; i < count; i++){
            result[i] = prefs.getInt(VAL_PREFIX+key+i, i);
        }
        return result;
    }

    public static void remove(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.KEY_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }
}
