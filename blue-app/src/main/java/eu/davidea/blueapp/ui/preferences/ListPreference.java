package eu.davidea.blueapp.ui.preferences;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;

/**
 * @author Davide
 * @since 17/06/2017
 * AndroidStarterApp
 */

public class ListPreference extends android.preference.ListPreference implements IPreference {

    public ListPreference(Context context) {
        super(context);
    }

    public ListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
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