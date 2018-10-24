package com.app.weatherforecast

import android.content.Context
import android.util.Log
import com.app.weatherforecast.data.InternalWeatherForecast
import com.app.weatherforecast.data.WeatherDataProvider
import com.app.weatherforecast.data.WeatherSharedPreferences
import com.app.weatherforecast.utils.NetworkUtils
import com.app.weatherforecast.utils.NotificationUtils
import java.util.*
import java.util.concurrent.TimeUnit


object WeatherSyncTask {
    val TAG = WeatherSyncTask::class.java.simpleName

    const val NOTIFICATIONS_INTERVAL_MINUTES: Int = 1440
    val NOTIFICATIONS_INTERVAL_SECOND: Long = TimeUnit.MINUTES.toSeconds(NOTIFICATIONS_INTERVAL_MINUTES.toLong())

    /**
     * perform http request to get weather data
     */
    fun syncWeather(context: Context): ArrayList<InternalWeatherForecast>? {
        Log.v(TAG, "syncWeather")
        val location = WeatherSharedPreferences.getWeatherLocation(context)
        val weatherRequestUrl = NetworkUtils.buildUrl(location)
        if (weatherRequestUrl != null) {
            try {
                val jsonWeatherResponse = NetworkUtils.requestBYUrl(weatherRequestUrl)
                if (jsonWeatherResponse != null) {
                    WeatherSharedPreferences.saveWeatherForecastJson(context, jsonWeatherResponse)
                    return WeatherDataProvider.getWeatherByDayFromJson(jsonWeatherResponse)
                } else return null
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        } else return null
        val timePassed = Calendar.getInstance().get(Calendar.SECOND)
        -WeatherSharedPreferences.getNotificationTime(context)
        if (WeatherSharedPreferences.isNotificationsEnabled(context) && timePassed > NOTIFICATIONS_INTERVAL_SECOND) {
            NotificationUtils.notifyUserOfWeatherUpdate(context)
        }
    }

}