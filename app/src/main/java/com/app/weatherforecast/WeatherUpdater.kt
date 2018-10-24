package com.app.weatherforecast

import android.content.Context
import android.content.Intent
import android.util.Log


import com.firebase.jobdispatcher.Constraint
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.firebase.jobdispatcher.Lifetime
import java.util.concurrent.TimeUnit


object WeatherUpdater {
    val TAG = WeatherUpdater::class.java.simpleName

    const val SYNC_JOB_TAG = "sync_job_tag"
    private val SYNC_INTERVAL_HOURS = 3
    private val SYNC_INTERVAL_SECONDS = TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS.toLong()).toInt()
    private val SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3

    var sInitialized = false

    /**
     * schedule job service to update weather every 3 hours
     */
    private fun scheduleSyncWeather(context: Context) {
        Log.v(TAG, "scheduleSyncWeather")
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context.applicationContext))
        val syncJob = dispatcher.newJobBuilder().setService(UpdateWeatherJobService::class.java).setTag(SYNC_JOB_TAG).setConstraints(Constraint.ON_ANY_NETWORK).setLifetime(Lifetime.FOREVER)
//             .setRecurring(true)
//                .setTrigger(Trigger.executionWindow(
//                        SYNC_INTERVAL_SECONDS,
//                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true).build()
        dispatcher.schedule(syncJob)
    }

    /**
     * initialize scheduler job service to update weather for the first time
     */
    @Synchronized
    fun initialize(context: Context) {
        Log.v(TAG, "initialize $sInitialized")
        if (sInitialized) return
        sInitialized = true
        scheduleSyncWeather(context)
    }

    /**
     * sync weather data if it is needed
     */
    fun startImmediateSync(context: Context) {
        Log.v(TAG, "startImmediateSync")
        val intentToSyncImmediately = Intent(context, WeatherBackgroundSyncService::class.java)
        context.startService(intentToSyncImmediately)
    }
}