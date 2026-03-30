package com.iiordanov.bVNC;

import android.app.Activity;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.undatech.remoteClientUi.R;

public class GlobalPreferencesFragment extends PreferenceFragmentCompat {

    private SwitchPreferenceCompat accessibilityPref;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        getPreferenceManager().setSharedPreferencesName(Constants.generalSettingsTag);
        setPreferencesFromResource(R.xml.global_preferences, s);
        if (Utils.isVnc(getContext())) {
            addPreferencesFromResource(R.xml.global_preferences_vnc);
        } else if (Utils.isRdp(getContext())) {
            addPreferencesFromResource(R.xml.global_preferences_rdp);
            setupUnicodeDisablesAccessibility();
        } else if (Utils.isSpice(getContext())) {
            addPreferencesFromResource(R.xml.global_preferences_spice);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accessibilityPref != null && accessibilityPref.isChecked()
                && !AccessibilityServiceHelper.isServiceEnabled(requireContext())) {
            Activity activity = getActivity();
            if (activity != null) {
                AccessibilityServiceHelper.showRequiredDialog(activity, () ->
                        accessibilityPref.setChecked(false));
            }
        }
    }

    private void setupUnicodeDisablesAccessibility() {
        accessibilityPref = findPreference(Constants.useAccessibilityKeyIntercept);
        SwitchPreferenceCompat unicodePref = findPreference(Constants.preferSendingUnicode);
        if (accessibilityPref != null && unicodePref != null) {
            boolean unicodeEnabled = unicodePref.isChecked();
            accessibilityPref.setEnabled(!unicodeEnabled);
            if (unicodeEnabled) {
                accessibilityPref.setChecked(false);
            }
            unicodePref.setOnPreferenceChangeListener((pref, newValue) -> {
                boolean enabled = (boolean) newValue;
                accessibilityPref.setEnabled(!enabled);
                if (enabled) {
                    accessibilityPref.setChecked(false);
                }
                return true;
            });

            accessibilityPref.setOnPreferenceChangeListener((pref, newValue) -> {
                if ((boolean) newValue && !AccessibilityServiceHelper.isServiceEnabled(requireContext())) {
                    Activity activity = getActivity();
                    if (activity != null) {
                        AccessibilityServiceHelper.showRequiredDialog(activity, () ->
                                accessibilityPref.setChecked(false));
                    }
                }
                return true;
            });
        }
    }
}
