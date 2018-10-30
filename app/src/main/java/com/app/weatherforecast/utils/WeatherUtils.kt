package com.app.weatherforecast.utils

import android.content.Context
import android.util.Log
import com.app.weatherforecast.R
import com.app.weatherforecast.data.WeatherSharedPreferences

object WeatherUtils {
    private val TAG = WeatherUtils::class.java.simpleName


    private fun kelvinToFahrenheit(temperatureInKelvin: Double): Double {
        return temperatureInKelvin - 459.67
    }

    private fun kelvinToCelsius(temperatureInKelvin: Double): Double {
        return temperatureInKelvin - 273.15
    }

    fun formatTemperature(context: Context, temperature: Double): String {
        var temperature = temperature
        var temperatureFormatResourceId = R.string.format_temperature_celsius

        if (WeatherSharedPreferences.isMetric(context)) {
            temperature = kelvinToCelsius(temperature)
            temperatureFormatResourceId = R.string.format_temperature_celsius
        } else {
            temperature = kelvinToFahrenheit(temperature)
            temperatureFormatResourceId = R.string.format_temperature_fahrenheit
        }

        return String.format(context.getString(temperatureFormatResourceId), temperature)
    }

    fun formatHighLowTemperature(context: Context, high: Double, low: Double): String {
        val roundedHigh = Math.round(high)
        val roundedLow = Math.round(low)

        val formattedHigh = formatTemperature(context, roundedHigh.toDouble())
        val formattedLow = formatTemperature(context, roundedLow.toDouble())

        return "$formattedHigh / $formattedLow"
    }


    /*
 * Based on weather code data found at:https://openweathermap.org/weather-conditions
 */
    fun getIconResourceForWeatherCondition(weatherId: Int): Int {

        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain
        } else if (weatherId == 511) {
            return R.drawable.ic_snow
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm
        } else if (weatherId == 800) {
            return R.drawable.ic_clear
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy
        }
        return -1
    }

    /*
 * Based on weather code data found at:
 * https://openweathermap.org/weather-conditions
 */
    fun getArtResourceForWeatherCondition(weatherId: Int): Int {

        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain
        } else if (weatherId == 511) {
            return R.drawable.art_snow
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog
        } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
            return R.drawable.art_storm
        } else if (weatherId == 800) {
            return R.drawable.art_clear
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds
        } else if (weatherId >= 900 && weatherId <= 906) {
            return R.drawable.art_storm
        } else if (weatherId >= 958 && weatherId <= 962) {
            return R.drawable.art_storm
        } else if (weatherId >= 951 && weatherId <= 957) {
            return R.drawable.art_clear
        }
        Log.e(TAG, "Unknown Weather: $weatherId")
        return R.drawable.art_storm
    }

    /*
* Based on weather code data found at:
* https://openweathermap.org/weather-conditions
*/
    fun getArtResourceForMainWeatherCondition(weatherDesc: String): Int {
        when(weatherDesc){
            "Thunderstorm" -> return R.drawable.art_storm
            "Drizzle" -> return R.drawable.art_light_rain
            "Rain"->return R.drawable.art_rain
            "Snow"->return R.drawable.art_snow
            "Atmosphere"->return R.drawable.art_fog
            "Clear"->return R.drawable.art_clear
            "Clouds"->return R.drawable.art_clouds
        }
        Log.e(TAG, "Unknown Weather: $weatherDesc")
        return R.drawable.art_storm
    }

}