package com.app.weatherforecast.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.app.weatherforecast.R
import com.app.weatherforecast.data.WeatherDataProvider
import com.app.weatherforecast.data.WeatherSharedPreferences
import com.app.weatherforecast.ui.ForecastDetailsFragment

object NotificationUtils {
    private val TAG = NotificationUtils::class.java.simpleName

    private const val WEATHER_NOTIFICATION_ID = 4242

    //The channel is required for Android Oreo
    private const val WEATHER_NOTIFICATION_CHANNEL_ID = "weather_notification_channel"

    /**
     * notify user of weather updated for today
     */
    fun notifyUserOfWeatherUpdate(context: Context) {
        val todayIndex = 0
        val weatherJson = WeatherSharedPreferences.getWeatherForecastJson(context)
        val weatherDataProvider = WeatherDataProvider()
        if (weatherJson.isNullOrEmpty()) {
            weatherDataProvider.syncWeather(context)
        } else {
            val data = weatherDataProvider.getWeatherByDayFromJson(weatherJson!!)
            Log.v(TAG, "make notification today weather")
            val description = data!![todayIndex].description
            val high = WeatherUtils.formatTemperature(context, data[todayIndex].maxTemperature)
            val low = WeatherUtils.formatTemperature(context, data[todayIndex].minTemperature)

            val resources = context.resources
            val largeArtResourceId = WeatherUtils.getArtResourceForMainWeatherCondition(data[todayIndex].description)
            val largeIcon = BitmapFactory.decodeResource(resources, largeArtResourceId)

            val notificationText = "Forecast: $description \nHigh: $high \nLow: $low"
            val notificationTitle = context.getString(R.string.app_name)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val startActivityIntent = Intent(context.applicationContext, ForecastDetailsFragment::class.java)
            //  startActivityIntent.putExtra(ForecastDetailsFragment.INTENT_WEATHER_DATA, todayIndex)
            val pendingIntent = PendingIntent.getActivity(context, WEATHER_NOTIFICATION_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val builder = NotificationCompat.Builder(context).setColor(ContextCompat.getColor(context, R.color.colorPrimary)).setSmallIcon(largeArtResourceId).setLargeIcon(largeIcon).setContentTitle(notificationTitle).setContentText(notificationText).setContentIntent(pendingIntent).setAutoCancel(true)


            //The channel is requied for Android Oreo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(WEATHER_NOTIFICATION_CHANNEL_ID)
                val notificationChannel = NotificationChannel(WEATHER_NOTIFICATION_CHANNEL_ID, "Weather update", NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                builder.setChannelId(WEATHER_NOTIFICATION_CHANNEL_ID)
                notificationManager.createNotificationChannel(notificationChannel)

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                builder.priority = NotificationCompat.PRIORITY_HIGH
            }
            notificationManager.notify(WEATHER_NOTIFICATION_ID, builder.build())
            WeatherSharedPreferences.saveNotificationTime(context)
        }
    }
}
