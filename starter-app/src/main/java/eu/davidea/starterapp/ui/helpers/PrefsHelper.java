package eu.davidea.starterapp.ui.helpers;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public final class PrefsHelper {

    /*
     * =================
     * Constructor
     * =================
     */

    /**
     * Instantiates a new My prefs helper.
     */
    private PrefsHelper() {
    }

    /*
     * =================
     * Helper methods
     * =================
     */

    /**
     * Put boolean.
     *
     * @param key   the key
     * @param value the value
     */
    public static void putBoolean(Activity activity, String key, boolean value) {
        SharedPreferences sharedPreferences = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Put boolean.
     *
     * @param prefsFile the prefs file
     * @param key       the key
     * @param value     the value
     */
    public static void putBoolean(Activity activity, String prefsFile, String key, boolean value) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(prefsFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Check boolean boolean.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the boolean
     */
    public static boolean checkBoolean(Activity activity, String key, boolean defaultValue) {
        return activity.getPreferences(MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    /**
     * Check boolean boolean.
     *
     * @param prefsFile    the prefs file
     * @param key          the key
     * @param defaultValue the default value
     * @return the boolean
     */
    public static boolean checkBoolean(Activity activity, String prefsFile, String key, boolean defaultValue) {
        return activity.getSharedPreferences(prefsFile, MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    /**
     * Put string.
     *
     * @param key   the key
     * @param value the value
     */
    public static void putString(Activity activity, String key, String value) {
        SharedPreferences sharedPreferences = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Put string.
     *
     * @param prefsFile the prefs file
     * @param key       the key
     * @param value     the value
     */
    public static void putString(Activity activity, String prefsFile, String key, String value) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(prefsFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Put strings from map.
     *
     * @param map the map
     */
    public static void putStringsFromMap(Activity activity, Map<String, String> map) {
        SharedPreferences sharedPreferences = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (Map.Entry<String, String> pair : map.entrySet()) {
            editor.putString(pair.getKey(), pair.getValue());
        }

        editor.commit();
    }

    /**
     * Put strings from map.
     *
     * @param prefsFile the prefs file
     * @param map       the map
     */
    public static void putStringsFromMap(Activity activity, String prefsFile, Map<String, String> map) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(prefsFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (Map.Entry<String, String> pair : map.entrySet()) {
            editor.putString(pair.getKey(), pair.getValue());
        }

        editor.commit();
    }

    /**
     * Check string string.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the string
     */
    public static String checkString(Activity activity, String key, String defaultValue) {
        return activity.getPreferences(MODE_PRIVATE).getString(key, defaultValue);
    }

    /**
     * Check string string.
     *
     * @param prefsFile    the prefs file
     * @param key          the key
     * @param defaultValue the default value
     * @return the string
     */
    public static String checkString(Activity activity, String prefsFile, String key, String defaultValue) {
        return activity.getSharedPreferences(prefsFile, MODE_PRIVATE).getString(key, defaultValue);
    }
}
