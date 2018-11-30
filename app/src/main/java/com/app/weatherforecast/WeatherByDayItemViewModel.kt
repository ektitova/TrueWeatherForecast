package com.app.weatherforecast

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.databinding.ObservableField
import android.graphics.drawable.Drawable
import com.app.weatherforecast.data.InternalWeatherForecast
import com.app.weatherforecast.utils.WeatherDateUtils
import com.app.weatherforecast.utils.WeatherUtils

class WeatherByDayItemViewModel(val context: Context, private val weatherData: InternalWeatherForecast, private val isFormatted: Boolean) : ViewModel() {
    // observable field to update view via binding
    private var tempMax: ObservableField<String> = ObservableField()
    // observable field to update view via binding
    private var tempMin: ObservableField<String> = ObservableField()

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
        val iconRes = WeatherUtils.getArtResourceForMainWeatherCondition(weatherData.description)
        icon = ObservableField(context.getDrawable(iconRes))
        return icon
    }

    /**
     *  returns max temperature
     */
    fun getTempMax(): ObservableField<String> {
        val roundedHigh = Math.round(weatherData.maxTemperature)
        tempMax = ObservableField(WeatherUtils.formatTemperature(context, roundedHigh.toDouble()))
        return tempMax
    }

    /**
     *  returns min temperature
     */
    fun getTempMin(): ObservableField<String> {
        val roundedLow = Math.round(weatherData.minTemperature)
        tempMin = ObservableField(WeatherUtils.formatTemperature(context, roundedLow.toDouble()))
        return tempMin
    }

    /**
     *  returns date
     */
    fun getDate(): ObservableField<String> {
        date = ObservableField(WeatherDateUtils.getFormattedDate(context, weatherData.date, isFormatted))
        return date
    }

}