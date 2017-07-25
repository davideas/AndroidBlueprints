package eu.davidea.starterapp.ui.preferences;

import android.support.annotation.StringRes;

/**
 * Interface that provides a set of methods to simplify the configuration for summary text
 * of each preference.
 *
 * @author Davide Steduto
 * @since 16/06/2017
 */
public interface IPreference {

	void setSummary(@StringRes int resId);

}