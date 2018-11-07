package com.app.weatherforecast.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.util.Log
import android.view.MenuItem
import com.app.weatherforecast.R
import com.app.weatherforecast.SettingsViewModel
import com.app.weatherforecast.WeatherSyncTask



class SettingsActivity : AppCompatActivity() {
    val TAG = SettingsActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate()")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction().replace(android.R.id.content, WeatherSettingsFragment()).commit()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    class WeatherSettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
        val TAG = WeatherSettingsFragment::class.java.simpleName
        private var viewModel:SettingsViewModel ?= null


        private fun initModel() {
            Log.v(TAG, "init data")
            viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        }

           override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            Log.v(TAG, "onSharedPreferenceChanged $key")
            val preference = findPreference(key)
            if (key == getString(R.string.pref_location_key)) run {
                WeatherSyncTask.syncWeather(context!!)
            } else if (null != preference) {
                if (preference !is CheckBoxPreference) {
                    setPreferenceSummary(preference, sharedPreferences!!.getString(key, "")!!)
                }
            }
        }


        private fun setPreferenceSummary(preference: Preference, value: Any) {
            val stringValue = value.toString()
            val key = preference.key
            Log.v(TAG, "setPreferenceSummary  of $key to $stringValue")
            if (preference is ListPreference) {
                // For list preferences
                val prefIndex = preference.findIndexOfValue(value.toString())
                if (prefIndex >= 0) {
                    preference.setSummary(preference.entries[prefIndex])
                }
            } else {
                // For other preferences, set the summary to the value's simple string representation.
                preference.summary = value.toString()
            }
        }

        override fun onCreatePreferences(bundle: Bundle?, s: String?) {
            Log.v(TAG, "onCreatePreference()")
            addPreferencesFromResource(R.xml.pref_screen)
            initModel()
            val sharedPreferences = preferenceScreen.sharedPreferences
            val prefScreen = preferenceScreen
            val count = prefScreen.preferenceCount
            for (i in 0 until count) {
                val p = prefScreen.getPreference(i)
                if (p !is CheckBoxPreference) {
                    val value = sharedPreferences.getString(p.key, "")
                    setPreferenceSummary(p, value!!)
                }
            }

            // feedback preference click listener
            val sendFeedBack = findPreference(getString(R.string.key_send_feedback))
            sendFeedBack!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                viewModel?.sendFeedback(context!!.applicationContext)
                true
            }
            // send notification click listener
            val sendNotification = findPreference(getString(R.string.key_send_notification))
            sendNotification!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                viewModel?.sendNotififcation(context!!.applicationContext)
                true
            }
        }

        override fun onStop() {
            super.onStop()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
            Log.v(TAG, "onStop() listener is unregustered")
        }

        override fun onStart() {
            super.onStart()
            /* Register the preference change listener */
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
            Log.v(TAG, "onStart() listener is register ")
        }

    }


}
