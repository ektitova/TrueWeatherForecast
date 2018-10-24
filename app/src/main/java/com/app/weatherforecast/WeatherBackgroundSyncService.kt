package com.app.weatherforecast

import android.app.IntentService
import android.content.Intent
import android.util.Log

class WeatherBackgroundSyncService(name: String?) : IntentService(name) {
    private val TAG = WeatherBackgroundSyncService::class.java!!.simpleName

    constructor() : this(WeatherBackgroundSyncService::class.java!!.simpleName)

    /**
     * service to update weather in the background
     */
    override fun onHandleIntent(intent: Intent?) {
        Log.v(TAG, "onHandleIntent")
        WeatherSyncTask.syncWeather(this)
    }

}