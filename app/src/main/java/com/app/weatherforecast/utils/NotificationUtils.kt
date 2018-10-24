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
import com.app.weatherforecast.WeatherUpdater
import com.app.weatherforecast.data.WeatherDataProvider
import com.app.weatherforecast.data.WeatherSharedPreferences
import com.app.weatherforecast.ui.DetailsActivity

object NotificationUtils {
    private val TAG = NotificationUtils::class.java.simpleName

    val WEATHER_NOTIFICATION_ID = 4242

    //The channel is requied for Android Oreo
    val WEATHER_NOTIFICATION_CHANNEL_ID = "weather_notification_channel"


    fun notifyUserOfWeatherUpdate(context: Context) {
        val data = WeatherDataProvider.weatherForecast
        if (data == null) {
            WeatherUpdater.startImmediateSync(context)
        } else {
            Log.v(TAG, "make notification today weather")
            val weatherId = data[0].weatherId
            val description = data[0].description
            val high = WeatherUtils.formatTemperature(context, data[0].maxTemperature)
            val low = WeatherUtils.formatTemperature(context, data[0].minTemperature)

            val resources = context.resources
            val largeArtResourceId = WeatherUtils.getArtResourceForWeatherCondition(weatherId)
            val largeIcon = BitmapFactory.decodeResource(resources, largeArtResourceId)

            val notificationText = "Forecast: $description \n" + "High: $high \n" + "Low: $low"
            val smallArtResourceId = WeatherUtils.getIconResourceForWeatherCondition(weatherId)

            val notificationTitle = context.getString(R.string.app_name)

            var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val startActivityIntent = Intent(context.applicationContext, DetailsActivity::class.java)
            var pendingIntent = PendingIntent.getActivity(context, WEATHER_NOTIFICATION_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val builder = NotificationCompat.Builder(context).setColor(ContextCompat.getColor(context, R.color.colorPrimary)).setSmallIcon(smallArtResourceId).setLargeIcon(largeIcon).setContentTitle(notificationTitle).setContentText(notificationText).setContentIntent(pendingIntent).setAutoCancel(true)

            //The channel is requied for Android Oreo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(WEATHER_NOTIFICATION_CHANNEL_ID)
                var notificationChannel = NotificationChannel(WEATHER_NOTIFICATION_CHANNEL_ID, "Weather update", NotificationManager.IMPORTANCE_HIGH)
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
