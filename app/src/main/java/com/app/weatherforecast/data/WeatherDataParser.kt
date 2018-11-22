package com.app.weatherforecast.data

import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

object WeatherDataParser {

    val TAG = WeatherDataParser::class.java.simpleName

    /**
     * parse json using WeatherForecast data class
     */
    fun getWeatherForecastFromJson(forecastJsonStr: String): WeatherForecast {
        Log.v(TAG, "getWeatherForecastFromJson")
        val mapper = ObjectMapper().registerModule(KotlinModule())
        return mapper.readValue(forecastJsonStr)
    }
}