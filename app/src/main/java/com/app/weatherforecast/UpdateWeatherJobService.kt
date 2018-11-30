package com.app.weatherforecast

import android.os.AsyncTask
import android.util.Log
import com.app.weatherforecast.data.WeatherDataProvider
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService

class UpdateWeatherJobService : JobService() {
    val TAG = UpdateWeatherJobService::class.java.simpleName
    private var mloadWeatherTask: AsyncTask<Void, Void, Void>? = null

    /**
     * Stop to update weather by schedule
     */
    override fun onStopJob(job: JobParameters?): Boolean {
        Log.v(TAG, "onStopJob")
        mloadWeatherTask?.cancel(true)
        return true
    }

    /**
     * job service update weather every 3 hours
     */
    override fun onStartJob(job: JobParameters): Boolean {
        Log.v(TAG, "onStartJob")
        mloadWeatherTask = object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                WeatherDataProvider().syncWeather(applicationContext)
                return null
            }

            override fun onPostExecute(aVoid: Void) {
                jobFinished(job, false)
            }
        }
        (mloadWeatherTask as AsyncTask<Void, Void, Void>).execute()
        return true
    }
}