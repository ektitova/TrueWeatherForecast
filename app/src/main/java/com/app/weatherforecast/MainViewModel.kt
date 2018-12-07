package com.app.weatherforecast

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import android.util.Log
import com.app.weatherforecast.data.InternalWeatherForecast
import com.app.weatherforecast.data.WeatherDataProvider
import com.app.weatherforecast.data.WeatherSharedPreferences
import com.firebase.jobdispatcher.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel(val context: Application) : AndroidViewModel(context) {
    private val TAG = MainViewModel::class.java.simpleName

    //whole weather foresasts structure
    var weatherForecast = MutableLiveData<ArrayList<InternalWeatherForecast>?>()

    private val SYNC_JOB_TAG = "sync_job_tag"
    private val SYNC_INTERVAL_HOURS = 3
    private val SYNC_INTERVAL_SECONDS = TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS.toLong()).toInt()
    private val SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3
    private val weatherDataProvider: WeatherDataProvider = WeatherDataProvider()

    private var sInitialized = false

    /**
     * execute async task to fetch weather data
     */
    fun loadWeatherList(): ArrayList<InternalWeatherForecast>? {
        Log.v(TAG, "load car list")
        val weatherJson = WeatherSharedPreferences.getWeatherForecastJson(context)
        if (weatherJson.isNullOrEmpty()) {
            FetchWeatherListTask().execute()
        } else {
            weatherForecast.value = weatherDataProvider.getWeatherByDayFromJson(weatherJson!!)
            return weatherForecast.value
        }
        return null
    }

    /**
     * reload weather list if requires
     */
    fun reloadWeatherList() {
        FetchWeatherListTask().execute()
    }

    /**
     * class to fetch weather list data
     */
    inner class FetchWeatherListTask : AsyncTask<Void, Void, ArrayList<InternalWeatherForecast>?>() {

        override fun doInBackground(vararg param: Void): ArrayList<InternalWeatherForecast>? {
            return weatherDataProvider.syncWeather(context)
        }

        override fun onPostExecute(weatherList: ArrayList<InternalWeatherForecast>?) {
            weatherForecast.postValue(weatherList)
        }
    }

    /**
     * initialize scheduler job service to update weather for the first time
     */
    fun initialize() {
        Log.v(TAG, "initialize ${sInitialized}")
        if (sInitialized) return
        sInitialized = true
        scheduleSyncWeather()
    }

    /**
     * schedule job service to update weather every 3 hours
     */
    private fun scheduleSyncWeather() {
        Log.v(TAG, "scheduleSyncWeather")
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context.applicationContext))
        val syncJob = dispatcher.newJobBuilder().setService(UpdateWeatherJobService::class.java).setTag(SYNC_JOB_TAG).setConstraints(Constraint.ON_ANY_NETWORK).setLifetime(Lifetime.FOREVER).setRecurring(true).setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS, SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS)).setReplaceCurrent(true).build()
        dispatcher.mustSchedule(syncJob)
    }


}