/**
 * Copyright 2013 Gabriele Mariotti
 * Copyright 2015-2017 for improvement and extensions by Davide Steduto
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.davidea.starterapp.ui.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.TwoStatePreference;
import android.util.Log;

import java.util.Set;

/**
 * Preference Summary
 */
public abstract class PreferencesFragmentSummary extends PreferenceFragment
		implements OnSharedPreferenceChangeListener {

	private static final String TAG = PreferencesFragmentSummary.class.getSimpleName();

	RingtonePreference mRingtonePreference;

	/**
	 * Must override this method and add the following lines:
	 * <ul>
	 * <li>super.onCreate(savedInstanceState);</li>
	 * <li>addPreferencesFromResource(R.xml.your_preferences);</li>
	 * </ul>
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		initSummary();
	}

	/**
	 * Init summary
	 */
	protected void initSummary() {
		int pcsCount = getPreferenceScreen().getPreferenceCount();
		for (int i = 0; i < pcsCount; i++) {
			initPrefsSummary(getPreferenceManager().getSharedPreferences(),
					getPreferenceScreen().getPreference(i));
		}
	}

	/**
	 * Init single Preference
	 */
	protected void initPrefsSummary(SharedPreferences sharedPreferences,
									Preference p) {
		if (p instanceof PreferenceCategory) {
			PreferenceCategory pCat = (PreferenceCategory) p;
			int pcCatCount = pCat.getPreferenceCount();
			for (int i = 0; i < pcCatCount; i++) {
				initPrefsSummary(sharedPreferences, pCat.getPreference(i));
			}
		} else {
			updatePrefsSummary(sharedPreferences, p);
			if (p instanceof RingtonePreference) {
				p.setOnPreferenceChangeListener(new RingToneOnPreferenceChangeListener());
				mRingtonePreference = (RingtonePreference) p;
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		if (mRingtonePreference != null) {
			// Pay attention: it is just an example!
			mRingtonePreference.setOnPreferenceChangeListener(null);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// Update summary
		updatePrefsSummary(sharedPreferences, findPreference(key));
	}

	protected abstract int getSummaryResId(String key);

	protected abstract int getSummaryOnResId(String key);

	protected abstract int getSummaryOffResId(String key);

	protected abstract String getCustomSummary(String key);

	/**
	 * Update summary.
	 * <p>Summary can be a simple resource string, a parametrized string or simply value.</p>
	 *
	 * @param sharedPreferences
	 * @param pref
	 */
	protected void updatePrefsSummary(SharedPreferences sharedPreferences, Preference pref) {

		if (pref == null)
			return;

		int resId = getSummaryResId(pref.getKey());

		if (pref instanceof ListPreference) {
			// List Preference
			ListPreference listPref = (ListPreference) pref;
			listPref.setSummary(resId > 0 ?
					// Entry is parametrized into a Resource string
					getResources().getString(getSummaryResId(pref.getKey()), listPref.getEntry())
					// Entry is displayed into summary directly
					: listPref.getEntry());
			Log.d(TAG, pref.getKey() + " resId=" + resId + " Summary=" + pref.getSummary());

		} else if (pref instanceof TwoStatePreference) {
			// CheckBox Preference
			TwoStatePreference checkBox = (TwoStatePreference) pref;
			if (checkBox.isChecked()) {
				// Display a Positive resourceId String if a correspondence has
				// been assigned for this pref. Otherwise Nothing is displayed
				resId = getSummaryOnResId(pref.getKey());
				checkBox.setSummaryOn(resId > 0 ? getResources().getString(resId) : null);
				Log.d(TAG, pref.getKey() + " resIdOn=" + resId + " Summary=" + checkBox.getSummaryOn());
			} else {
				// If an extra specific resource exists this is added to the
				// Negative value otherwise nothing.
				resId = getSummaryResId(pref.getKey());
				int resIdOff = getSummaryOffResId(pref.getKey());
				if (resId > 0) {
					checkBox.setSummaryOff(resIdOff > 0 ?
							getResources().getString(resIdOff) + "\n" +
									getResources().getString(resId) : null);
				} else if (resIdOff > 0) {
					// Display a Negative resourceId String if a correspondence has
					// been assigned for this pref. Otherwise Nothing is displayed
					checkBox.setSummaryOff(resIdOff > 0 ?
							getResources().getString(resIdOff) : null);
				}
				Log.d(TAG, pref.getKey() + " resId=" + resId + " resIdOff=" + resIdOff + " Summary=" + checkBox.getSummaryOff());
			}

		} else if (pref instanceof EditTextPreference) {
			// Edit Preference
			EditTextPreference editTextPref = (EditTextPreference) pref;
			editTextPref.setSummary(resId > 0 ?
					// Entry is parametrized into a Resource string
					getResources().getString(resId, editTextPref.getText())
					// Entry is displayed into summary directly
					: editTextPref.getText());
			Log.d(TAG, pref.getKey() + " resId=" + resId + " Summary=" + pref.getSummary());

		} else if (pref instanceof MultiSelectListPreference) {
			// MultiSelectList Preference
			MultiSelectListPreference listPref = (MultiSelectListPreference) pref;
			String summaryMListPref = "";
			String and = "";

			// Retrieve values
			Set<String> values = listPref.getValues();
			for (String value : values) {
				// For each value retrieve index
				int index = listPref.findIndexOfValue(value);
				// Retrieve entry from index
				CharSequence mEntry = index >= 0
						&& listPref.getEntries() != null ? listPref
						.getEntries()[index] : null;
				if (mEntry != null) {
					// Add summary
					summaryMListPref = summaryMListPref + and + mEntry;
					and = "; ";
				}
			}
			// Set summary
			listPref.setSummary(summaryMListPref);

			// Check https://github.com/Microsoft/ProjectOxford-Apps-MimickerAlarm/blob/master/Mimicker/app/src/main/java/com/microsoft/mimickeralarm/settings/RingtonePreference.java

		} else if (pref instanceof RingtonePreference) {
			// Ringtone Preference
			RingtonePreference rtPref = (RingtonePreference) pref;
			String uri;
			uri = sharedPreferences.getString(rtPref.getKey(), null);
			if (uri != null) {
				Ringtone ringtone = RingtoneManager.getRingtone(
						getActivity(), Uri.parse(uri));
				//Pay attention: it is just an example!
				pref.setSummary(ringtone.getTitle(getActivity()));
			}

//		} else if (pref instanceof NumberPickerPreference) {
//			// MyNumberPickerPreference
//			NumberPickerPreference nPickerPref = (NumberPickerPreference) pref;
//			nPickerPref.setSummary(nPickerPref.getValue());

		} else {// It's already a Preference Class, therefore no cast!
			// For custom preferences
			pref.setSummary(resId > 0 ?
					// Entry is parametrized into a Resource string
					getResources().getString(getSummaryResId(pref.getKey()), getCustomSummary(pref.getKey()))
					// Entry is displayed into summary directly
					: getCustomSummary(pref.getKey()));
			Log.d(TAG, pref.getKey() + " resId=" + resId + " Summary=" + pref.getSummary());
		}
	}

	/**
	 * Listener for RingTonePreference It does not fire
	 * onSharedPreferenceChanged when a Ringtone is selected
	 */
	class RingToneOnPreferenceChangeListener implements
			OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference pref, Object newValue) {
			if (newValue != null && newValue instanceof String) {
				String uri = (String) newValue;
				Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), Uri.parse(uri));
				pref.setSummary(ringtone.getTitle(getActivity()));
			}
			return true;
		}
	}

}