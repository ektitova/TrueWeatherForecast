package com.app.weatherforecast.data

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.app.weatherforecast.R
import java.util.*


object WeatherSharedPreferences {
    val TAG = WeatherSharedPreferences::class.java.simpleName

    /**
     * save weather json in preferences
     */
    fun saveWeatherForecastJson(context: Context, weatherJson: String) {
        Log.v(TAG, "saveWeatherForecastJson $weatherJson")
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val storedWearherJson = sp.getString("weatherJson", "")
        if (storedWearherJson != weatherJson) {
            val prefEditor = sp.edit()
            prefEditor.putString("weatherJson", weatherJson) //**syntax error on tokens**
            prefEditor.commit()
        }
    }

    /**
     * save last notification time
     */
    fun saveNotificationTime(context: Context) {
        val currentTime = Calendar.getInstance().get(Calendar.SECOND)
        Log.v(TAG, "saveNotificationTime in sec $currentTime")
        val prefEditor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        prefEditor.putInt("notificationTime", currentTime)
        prefEditor.commit()
    }


    /**
     * returns true if the notification option is enabled in preferences, false otherwise
     */
    fun isNotificationsEnabled(context: Context): Boolean {
        Log.v(TAG, "isNotificationsEnabled ")
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getBoolean("checkboxPref", false)
    }

    /**
     * returns stored last notification time
    */
    fun getNotificationTime(context: Context): Int {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val time = sp.getInt("notificationTime", 0)
        Log.v(TAG, "getNotificationTime $time")
        return time
    }

    /**
     * returns stored weather json in preferences
     */
    fun getWeatherForecastJson(context: Context): String? {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getString("weatherJson", "")
    }

    /**
     * returns stored weather location in preferences
     */
    fun getWeatherLocation(context: Context): String {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val keyForLocation = context.getString(R.string.pref_location_key)
        val defaultLocation = context.getString(R.string.pref_location_default)

        return sp.getString(keyForLocation, defaultLocation)
    }

    /**
     * returns true if the metric temperature format was selected, false otherwise
     */
    fun isMetric(context: Context): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val keyForUnits = context.getString(R.string.pref_units_key)
        val defaultUnits = context.getString(R.string.pref_units_metric)
        val preferredUnits = sp.getString(keyForUnits, defaultUnits)
        val metric = context.getString(R.string.pref_units_metric)
        var userPrefersMetric = false
        if (metric == preferredUnits) {
            userPrefersMetric = true
        }
        Log.v(TAG, "is metric $userPrefersMetric")
        return userPrefersMetric
    }
}