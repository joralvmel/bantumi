package es.upm.miw.bantumi.ui.fragmentos;

import android.os.Bundle;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import es.upm.miw.bantumi.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        EditTextPreference initialSeedsPreference = findPreference("initial_number_of_seeds");
        if (initialSeedsPreference != null) {
            initialSeedsPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                return true;
            });
        }
    }
}