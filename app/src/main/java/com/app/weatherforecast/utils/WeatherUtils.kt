package com.app.weatherforecast.utils

import android.content.Context
import android.util.Log
import com.app.weatherforecast.R
import com.app.weatherforecast.data.WeatherSharedPreferences

object WeatherUtils {
    private val TAG = WeatherUtils::class.java.simpleName

    /**
     * convert kelvins in which temperature comes to fahrenheits
     */
    private fun kelvinToFahrenheit(temperatureInKelvin: Double): Double {
        return temperatureInKelvin * 9 / 5 - 459.67
    }

    /**
     * convert kelvins in which temperature comes to celsius
     */
    private fun kelvinToCelsius(temperatureInKelvin: Double): Double {
        return temperatureInKelvin - 273.15
    }

    /**
     * format temperature in Metric or Imperial system depends on settings
     */
    fun formatTemperature(context: Context, temp: Double): String {
        val temperature: Double
        val temperatureFormat: String

        if (WeatherSharedPreferences.isMetric(context)) {
            temperature = kelvinToCelsius(temp)
            temperatureFormat = "%d\u00B0C"
        } else {
            temperature = kelvinToFahrenheit(temp)
            temperatureFormat = "%d\u00B0F"
        }

        return String.format(temperatureFormat, Math.round(temperature))
    }

    /**
     * format temperature in F/F or C/C
     */
    fun formatHighLowTemperature(context: Context, high: Double, low: Double): String {
        val formattedHigh = formatTemperature(context, high)
        val formattedLow = formatTemperature(context, low)
        return "$formattedHigh / $formattedLow"
    }


 /**
 * get picture of weather on weather code data found at:
 * https://openweathermap.org/weather-conditions
 */
    fun getArtResourceForWeatherCondition(weatherId: Int): Int {
        if (weatherId in 200..232) {
            return R.drawable.art_storm
        } else if (weatherId in 300..321) {
            return R.drawable.art_light_rain
        } else if (weatherId in 500..504) {
            return R.drawable.art_rain
        } else {
            if (weatherId == 511) {
                return R.drawable.art_snow
            } else if (weatherId in 520..531) {
                return R.drawable.art_rain
            } else if (weatherId in 600..622) {
                return R.drawable.art_snow
            } else if (weatherId in 701..761) {
                return R.drawable.art_fog
            } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
                return R.drawable.art_storm
            } else if (weatherId == 800) {
                return R.drawable.art_clear
            } else if (weatherId == 801) {
                return R.drawable.art_light_clouds
            } else if (weatherId in 802..804) {
                return R.drawable.art_clouds
            } else if (weatherId in 900..906) {
                return R.drawable.art_storm
            } else if (weatherId in 958..962) {
                return R.drawable.art_storm
            } else if (weatherId in 951..957) {
                return R.drawable.art_clear
            }
        }
        Log.e(TAG, "Unknown Weather: $weatherId")
        return R.drawable.art_storm
    }

 /**
* get picture of weather based on weather code data found at:
* https://openweathermap.org/weather-conditions
*/
    fun getArtResourceForMainWeatherCondition(weatherDesc: String): Int {
        when (weatherDesc) {
            "Thunderstorm" -> return R.drawable.art_storm
            "Drizzle" -> return R.drawable.art_light_rain
            "Rain" -> return R.drawable.art_rain
            "Snow" -> return R.drawable.art_snow
            "Atmosphere" -> return R.drawable.art_fog
            "Clear" -> return R.drawable.art_clear
            "Clouds" -> return R.drawable.art_clouds
        }
        Log.e(TAG, "Unknown Weather: $weatherDesc")
        return R.drawable.art_storm
    }

}