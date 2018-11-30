package com.app.weatherforecast.data

import android.content.Context
import android.util.Log
import com.app.weatherforecast.utils.NetworkUtils
import com.app.weatherforecast.utils.NotificationUtils
import com.app.weatherforecast.utils.WeatherDateUtils
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import java.util.*
import java.util.concurrent.TimeUnit


class WeatherDataProvider {
    val TAG = WeatherDataProvider::class.java.simpleName

    private val NOTIFICATIONS_INTERVAL_MINUTES: Int = 1440
    private val NOTIFICATIONS_INTERVAL_SECOND: Long = TimeUnit.MINUTES.toSeconds(NOTIFICATIONS_INTERVAL_MINUTES.toLong())

    /**
     * perform http request to get weather data
     */
    fun syncWeather(context: Context): ArrayList<InternalWeatherForecast>? {
        Log.v(TAG, "syncWeather")
        val location = WeatherSharedPreferences.getWeatherLocation(context)
        val weatherRequestUrl = NetworkUtils.buildUrl(location)
        try {
            val jsonWeatherResponse = NetworkUtils.requestByUrl(weatherRequestUrl)
            if (jsonWeatherResponse != null) {
                WeatherSharedPreferences.saveWeatherForecastJson(context, jsonWeatherResponse)
                saveNotificationTime(context)
                return getWeatherByDayFromJson(jsonWeatherResponse)
            } else return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun saveNotificationTime(context: Context) {
        Log.v(TAG, "save last notification time and notify if it is needed")
        val timePassed = Calendar.getInstance().get(Calendar.SECOND)
        -WeatherSharedPreferences.getNotificationTime(context)
        if (WeatherSharedPreferences.isNotificationsEnabled(context) && timePassed > NOTIFICATIONS_INTERVAL_SECOND) {
            NotificationUtils.notifyUserOfWeatherUpdate(context)
        }
    }

    /**
     * returns and store whole weather forecast structure
     */
    @Throws(KotlinNullPointerException::class, MismatchedInputException::class)
    fun getWeatherByDayFromJson(forecastJsonStr: String): ArrayList<InternalWeatherForecast>? {
        Log.v(TAG, "getWeatherByDayFromJson")
        val forecast = WeatherDataParser.getWeatherForecastFromJson(forecastJsonStr)
        val weatherForecast = arrayListOf<InternalWeatherForecast>()
        var position = 0
        while (position < forecast.list.size) {

            val date = forecast.list[position].dateTime
            val dayNumber = WeatherDateUtils.getDayNumber(forecast.list[position].dateTime)
            var dailyMax = forecast.list[position].main!!.temp_max
            var dailyMin = forecast.list[position].main!!.temp_min
            val dailyForecasts = arrayListOf<InternalDayWeatherForecast>()
            val mainWeather = mutableMapOf<String?, Int>()
            do {
                dailyMax = if (forecast.list[position].main!!.temp_max > dailyMax) forecast.list[position].main!!.temp_max else dailyMax
                dailyMin = if (forecast.list[position].main!!.temp_min < dailyMin) forecast.list[position].main!!.temp_min else dailyMin
                val dailyForecast = InternalDayWeatherForecast(forecast.list[position].weather.first().id, forecast.list[position].dateTime, forecast.list[position].humidity, forecast.list[position].pressure, forecast.list[position].wind.speed, forecast.list[position].main!!.temp_max, forecast.list[position].main!!.temp_min, forecast.list[position].weather.first().desc)
                dailyForecasts.add(dailyForecast)
                val desc = forecast.list[position].weather.first().main
                if (mainWeather.containsKey(desc)) mainWeather[desc] = mainWeather[desc]!! + 1
                else mainWeather[desc] = 1
                if (++position >= forecast.list.size) break
                val nextDate = WeatherDateUtils.getDayNumber(forecast.list[position].dateTime)
            } while (dayNumber == nextDate)
            val weather = mainWeather.toList().sortedByDescending { (_, value) -> value }.toMap()
            val dailyDesc = weather.keys.first()
            weatherForecast.add(InternalWeatherForecast(date, dailyMax, dailyMin, dailyDesc!!, dailyForecasts))
        }
        return weatherForecast
    }


}


