package com.app.weatherforecast.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


/**
 * data class to represent internal structure of json object
 */
data class Coordinates(val lon: Double, val lat: Double)

/**
 * data class to represent internal structure of json object
 */
data class City(val id: String, val name: String, val coord: Coordinates, val country: String, val population: Int)

/**
 * data class to represent internal structure of json object
 */
data class Main(val temp: Double, val temp_min: Double, val temp_max: Double, val pressure: Double, val humidity: Double, val sea_level: Double, val grnd_level: Double, val temp_kf: Double)

/**
 * data class to represent internal structure of json object
 */
// id int or long?
data class WeatherDescription(val id: Int, val main: String, @JsonProperty("description") val desc: String, val icon: String)

/**
 * data class to represent internal structure of json object
 */
data class Wind(val speed: Double, val deg: Double)

/**
 * data class to represent internal structure of json object
 */
data class Clouds(val all: Double)

/**
 * data class to parse json in to data
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ForecastRecord(private val dt: Double, val main: Main?, val pressure: Double, val humidity: Int, val weather: List<WeatherDescription>, val wind: Wind, val clouds: Clouds) {
    val dateTime: Long
        get() {
            return (dt * 1000).toLong()
        }
}

/**
 * data class to parse json in to data
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherForecast(val city: City, val cod: String, val message: Double, val cnt: Int, val list: List<ForecastRecord>)


/**
 * data class to store detailed weather forecast for the day
 */
data class InternalDayWeatherForecast(val weatherId: Int, val time: Long, val humidity: Int, val pressure: Double, val windSpeed: Double, val maxTemperature: Double, val minTemperature: Double, val description: String)

/**
 * data class to store average weather forecast for the day and detailed weather forecast
 */
data class InternalWeatherForecast(val date: Long, val maxTemperature: Double, val minTemperature: Double, val description: String, val dailyForecasts: List<InternalDayWeatherForecast>)