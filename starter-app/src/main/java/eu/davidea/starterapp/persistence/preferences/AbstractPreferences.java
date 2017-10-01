package eu.davidea.starterapp.persistence.preferences;

import android.content.SharedPreferences;

import java.util.Map;

abstract class AbstractPreferences {

    private final SharedPreferences sharedPreferences;

	/*-------------*/
    /* CONSTRUCTOR */
    /*-------------*/

    /**
     * Instantiates a new Preference Service.
     * <p>User Dagger Injection.</p>
     */
    AbstractPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

	/*----------------*/
    /* HELPER METHODS */
    /*----------------*/

    /**
     * Gets boolean value.
     *
     * @return the boolean
     */
    boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Puts boolean.
     *
     */
    void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Gets string value.
     *
     * @return the string value
     */
    String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * Puts string.
     */
    void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Gets long value.
     *
     * @return the long value
     */
    long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * Puts long.
     */
    void putLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * Puts strings from map.
     *
     * @param map the map
     */
    void putStringsFromMap(Map<String, String> map) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Map.Entry<String, String> pair : map.entrySet()) {
            editor.putString(pair.getKey(), pair.getValue());
        }
        editor.apply();
    }

}