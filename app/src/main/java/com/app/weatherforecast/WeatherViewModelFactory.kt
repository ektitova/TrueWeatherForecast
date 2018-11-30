package com.app.weatherforecast

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.app.weatherforecast.data.InternalDayWeatherForecast
import com.app.weatherforecast.data.InternalWeatherForecast


class WeatherViewModelFactory(private val context: Context, vararg params: Any) : ViewModelProvider.NewInstanceFactory() {

    private val mParams: List<Any> = params.asList()

    /**
     *  creates instance of the model class depending on class type
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass == WeatherByTimeItemViewModel::class.java) {
            WeatherByTimeItemViewModel(context, mParams[0] as InternalDayWeatherForecast) as T
        } else if (modelClass == WeatherByDayItemViewModel::class.java) {
            WeatherByDayItemViewModel(context, mParams[0] as InternalWeatherForecast, mParams[1] as Boolean) as T
        } else {
            super.create(modelClass)
        }
    }
}


