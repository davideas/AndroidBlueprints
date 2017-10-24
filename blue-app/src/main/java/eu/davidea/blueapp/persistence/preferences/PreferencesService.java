package eu.davidea.blueapp.persistence.preferences;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * @author Davide
 * @since 30/09/2017
 */
public class PreferencesService extends AbstractPreferences {

    private static final String LOGGED_RESTAURANT = "logged_restaurant";

    /**
     * Instantiates a new Preference Service.
     *
     * @param sharedPreferences default sharedPreferences injected by Dagger
     */
    @Inject
    public PreferencesService(SharedPreferences sharedPreferences) {
        super(sharedPreferences);
    }

}