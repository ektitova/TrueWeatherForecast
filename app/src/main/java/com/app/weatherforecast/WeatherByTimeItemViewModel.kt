package com.app.weatherforecast

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.databinding.ObservableField
import android.graphics.drawable.Drawable
import com.app.weatherforecast.data.InternalDayWeatherForecast
import com.app.weatherforecast.utils.WeatherDateUtils
import com.app.weatherforecast.utils.WeatherUtils

class WeatherByTimeItemViewModel(val context: Context, private val weatherData: InternalDayWeatherForecast) : ViewModel() {

    // observable field to update view via binding
    private var temp: ObservableField<String> = ObservableField()

    // observable field to update view via binding
    var description: ObservableField<String> = ObservableField()

    // observable field to update view via binding
    private var date: ObservableField<String> = ObservableField()

    // observable field to update view via binding
    private var icon: ObservableField<Drawable> = ObservableField()

    init {
        description = ObservableField(weatherData.description.capitalize())
    }

    /**
     *  returns icon
     */
    fun getIcon(): ObservableField<Drawable> {
        val iconRes = WeatherUtils.getArtResourceForWeatherCondition(weatherData.weatherId)
        icon = ObservableField(context.getDrawable(iconRes))
        return icon
    }

    /**
     *  returns temperature
     */
    fun getTemp(): ObservableField<String> {
        temp = ObservableField(WeatherUtils.formatHighLowTemperature(context, weatherData.maxTemperature, weatherData.minTemperature))
        return temp
    }

    /**
     *  returns date
     */
    fun getDate(): ObservableField<String> {
        date = ObservableField(WeatherDateUtils.getFormattedTime(weatherData.time))
        return date
    }

}