/*
 * Copyright (C) 2017 The ABC rom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.abc.settings;

import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.abc.support.preferences.IconPackPreference;

public class RecentsSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private SwitchPreference mSlimToggle;
    private RecentsIconPackPreference mStockIconPacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.abc_recents_settings);

        mStockIconPacks = (RecentsIconPackPreference) findPreference("recents_icon_pack");
        mSlimToggle = (SwitchPreference) findPreference("use_slim_recents");
        boolean enabled = Settings.System.getIntForUser(
                getActivity().getContentResolver(), Settings.System.USE_SLIM_RECENTS, 0,
                UserHandle.USER_CURRENT) == 1;
        mSlimToggle.setChecked(enabled);
        mStockIconPacks.setEnabled(!enabled);
        mSlimToggle.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mSlimToggle) {
            boolean value = (Boolean) newValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.USE_SLIM_RECENTS, value ? 1 : 0,
                    UserHandle.USER_CURRENT);
            mSlimToggle.setChecked(value);
            mStockIconPacks.setEnabled(!value);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        IconPackPreference iconPackPref = (IconPackPreference) findPreference("recents_icon_pack");
        // Re-initialise preference
        iconPackPref.init();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.ABC;
    }
}
