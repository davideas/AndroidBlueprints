package eu.davidea.starterapp.ui.preferences;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;

/**
 * @author Davide
 * @since 17/06/2017
 * AndroidStarterApp
 */

public class ListPreference extends android.support.v7.preference.ListPreference implements IPreference {

	public ListPreference(Context context) {
		super(context);
	}

	public ListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void setSummary(@StringRes int resId) {
		// List Preference
		setSummary(resId > 0 ?
				// Entry is parametrized into a Resource string
				getContext().getResources().getString(resId, getEntry())
				// Entry is displayed into summary directly
				: getEntry());
	}

}