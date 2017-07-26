package eu.davidea.starterapp.helpers;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * The type My prefs helper.
 */
@SuppressWarnings("All")
public class PrefsHelper
{
    /* General obj */
    private Activity mActivity;


    /*
     * =================
     * Constructor
     * =================
     */

    /**
     * Instantiates a new My prefs helper.
     *
     * @param activity the activity
     */
    public PrefsHelper (Activity activity)
    {
        this.mActivity = activity;
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
    public void putBoolean(String key, boolean value)
    {
        SharedPreferences sharedPreferences = mActivity.getPreferences(MODE_PRIVATE);
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
    public void putBoolean(String prefsFile, String key, boolean value)
    {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(prefsFile, MODE_PRIVATE);
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
    public boolean checkBoolean(String key, boolean defaultValue)
    {
        return mActivity.getPreferences(MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    /**
     * Check boolean boolean.
     *
     * @param prefsFile    the prefs file
     * @param key          the key
     * @param defaultValue the default value
     * @return the boolean
     */
    public boolean checkBoolean(String prefsFile, String key, boolean defaultValue)
    {
        return mActivity.getSharedPreferences(prefsFile, MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    /**
     * Put string.
     *
     * @param key   the key
     * @param value the value
     */
    public void putString(String key, String value)
    {
        SharedPreferences sharedPreferences = mActivity.getPreferences(MODE_PRIVATE);
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
    public void putString(String prefsFile, String key, String value)
    {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(prefsFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Put strings from map.
     *
     * @param map the map
     */
    public void putStringsFromMap(Map<String, String> map)
    {
        SharedPreferences sharedPreferences = mActivity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (Map.Entry<String, String> pair : map.entrySet())
        {
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
    public void putStringsFromMap(String prefsFile, Map<String, String> map)
    {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(prefsFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (Map.Entry<String, String> pair : map.entrySet())
        {
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
    public String checkString(String key, String defaultValue)
    {
        return mActivity.getPreferences(MODE_PRIVATE).getString(key, defaultValue);
    }

    /**
     * Check string string.
     *
     * @param prefsFile    the prefs file
     * @param key          the key
     * @param defaultValue the default value
     * @return the string
     */
    public String checkString(String prefsFile, String key, String defaultValue)
    {
        return mActivity.getSharedPreferences(prefsFile, MODE_PRIVATE).getString(key, defaultValue);
    }
}
