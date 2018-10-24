package com.app.weatherforecast.data

import android.util.Log
import com.app.weatherforecast.utils.WeatherDateUtils
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.*

object WeatherDataProvider {
    val TAG = WeatherDataProvider::class.java.simpleName

    //whole weather foresasts structure
    var weatherForecast: ArrayList<InternalWeatherForecast>? = null

    /**
     * returns and store whole weather forecast structure
     */
    fun getWeatherByDayFromJson(forecastJsonStr: String): ArrayList<InternalWeatherForecast>? {
        Log.v(TAG, "getWeatherByDayFromJson")
        val forecast = getWeatherForecastFromJson(forecastJsonStr)
        val weatherForecast = arrayListOf<InternalWeatherForecast>()
        var position = 0
        while (position < forecast.list.size) {

            val date = forecast.list[position].dateTime
            val dayNumber = WeatherDateUtils.getDayNumber(forecast.list[position].dateTime)
            var dailyMax = forecast.list[position].main.temp_max
            var dailyMin = forecast.list[position].main.temp_min
            val dailyForecasts = arrayListOf<InternalDayWeatherForecast>()

            do {
                dailyMax = if (forecast.list[position].main.temp_max > dailyMax) forecast.list[position].main.temp_max else dailyMax
                dailyMin = if (forecast.list[position].main.temp_min < dailyMin) forecast.list[position].main.temp_min else dailyMin
                val dailyForecast = InternalDayWeatherForecast(forecast.list[position].weather.first().id, forecast.list[position].dateTime, forecast.list[position].humidity, forecast.list[position].pressure, forecast.list[position].wind.speed, forecast.list[position].wind.deg, forecast.list[position].main.temp_min, forecast.list[position].main.temp_max, forecast.list[position].weather.first().desc)
                dailyForecasts.add(dailyForecast)
                if (++position >= forecast.list.size) break
                var nextDate = WeatherDateUtils.getDayNumber(forecast.list[position].dateTime)
            } while (dayNumber == nextDate)
            var dailyWeatherId = dailyForecasts[dailyForecasts.size / 2].weatherId
            var dailyDesc = dailyForecasts[dailyForecasts.size / 2].description
            weatherForecast.add(InternalWeatherForecast(dailyWeatherId, date, dailyMax, dailyMin, dailyDesc, dailyForecasts))
        }
        this.weatherForecast = weatherForecast
        return weatherForecast
    }

    /**
     * parse json using WeatherForecast data class
     */
    private fun getWeatherForecastFromJson(forecastJsonStr: String): WeatherForecast {
        Log.v(TAG, "getWeatherForecastFromJson")
        val mapper = ObjectMapper().registerModule(KotlinModule())
        return mapper.readValue(forecastJsonStr)
    }

}

/**
 * data class to store detailed weather forecast for the day
 */
data class InternalDayWeatherForecast(val weatherId: Int, val time: Long, val humidity: Int, val pressure: Double, val windSpeed: Double, val windDirection: Double, val maxTemperature: Double, val minTemperature: Double, val description: String?)

/**
 * data class to store average weather forecast for the day and detailed weather forecast
 */
data class InternalWeatherForecast(val weatherId: Int, val date: Long, val maxTemperature: Double, val minTemperature: Double, val description: String?, val dailyForecasts: List<InternalDayWeatherForecast>)
