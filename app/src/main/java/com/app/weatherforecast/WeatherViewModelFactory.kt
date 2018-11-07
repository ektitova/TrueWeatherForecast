package com.app.weatherforecast

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.app.weatherforecast.data.InternalWeatherForecast

class WeatherViewModelFactory(private val weatherData: InternalWeatherForecast) : ViewModelProvider.Factory {
    private val TAG = WeatherViewModelFactory::class.java.simpleName

    /**
     * create instance of CarItemViewModel
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherDetailsViewModel(weatherData) as T
    }
}

