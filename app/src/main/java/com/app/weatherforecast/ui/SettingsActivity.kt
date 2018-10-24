package com.app.weatherforecast.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v14.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.util.Log
import android.view.MenuItem
import com.app.weatherforecast.R
import com.app.weatherforecast.WeatherUpdater
import com.app.weatherforecast.utils.NotificationUtils


class SettingsActivity : AppCompatActivity() {
    val TAG = SettingsActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate()")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        fragmentManager.beginTransaction().replace(android.R.id.content, WeatherSettings()).commit()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    class WeatherSettings : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
        val TAG = WeatherSettings::class.java.simpleName

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            Log.v(TAG, "onSharedPreferenceChanged $key")
            if (key == getString(R.string.pref_location_key)) {
                WeatherUpdater.startImmediateSync(activity)
            }
            val preference = findPreference(key)
            if (key == getString(R.string.pref_location_key)) run {
                WeatherUpdater.startImmediateSync(context)
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

            val sharedPreferences = getPreferenceScreen().getSharedPreferences()
            val prefScreen = getPreferenceScreen()
            val count = prefScreen.getPreferenceCount()
            for (i in 0 until count) {
                val p = prefScreen.getPreference(i)
                if (p !is CheckBoxPreference) {
                    val value = sharedPreferences.getString(p.getKey(), "")
                    setPreferenceSummary(p, value!!)
                }
            }

            // feedback preference click listener
            val sendFeedBack = findPreference(getString(R.string.key_send_feedback))
            sendFeedBack!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                sendFeedback(context.applicationContext)
                true
            }
            // send notification click listener
            val sendNotification = findPreference(getString(R.string.key_send_notification))
            sendNotification!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                NotificationUtils.notifyUserOfWeatherUpdate(context.applicationContext)
                true
            }
        }

        override fun onStop() {
            super.onStop()
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this)
            Log.v(TAG, "onStop() listener is unregustered")
        }

        override fun onStart() {
            super.onStart()
            /* Register the preference change listener */
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this)
            Log.v(TAG, "onStart() listener is register ")
        }

        /**
         * Email client intent to send support mail
         * Appends the necessary device information to email body
         * useful when providing support
         */
        fun sendFeedback(context: Context) {
            Log.v(TAG, "sendFeedback()")
            var body: String? = null
            try {
                body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName
                body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " + Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND + "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER
            } catch (e: PackageManager.NameNotFoundException) {
            }

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ektitova10@gnail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app")
            intent.putExtra(Intent.EXTRA_TEXT, body)
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)))
        }


    }


}
