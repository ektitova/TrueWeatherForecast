package com.app.weatherforecast.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.app.weatherforecast.R
import com.app.weatherforecast.WeatherSyncTask
import com.app.weatherforecast.WeatherUpdater
import com.app.weatherforecast.data.InternalWeatherForecast
import com.app.weatherforecast.data.WeatherDataProvider
import com.app.weatherforecast.data.WeatherDataProvider.getWeatherByDayFromJson
import com.app.weatherforecast.data.WeatherSharedPreferences
import java.util.*


class MainActivity : AppCompatActivity(), ForecastAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<InternalWeatherForecast>?> {
    private val TAG = MainActivity::class.java.simpleName

    private val WEATHER_SEACH_LOADER: Int = 22
    private var mErrorTextView: TextView? = null
    private var mLoadingIndicator: ProgressBar? = null
    private var recyclerView: RecyclerView? = null
    private var mForecastAdapter: ForecastAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate()")
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.list_results)
        mLoadingIndicator = findViewById(R.id.loading_indicator)
        mErrorTextView = findViewById(R.id.error_message)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        initData()
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "onResume()")
        if (WeatherDataProvider.weatherForecast == null) return
        setDataToAdapter()
    }

    private fun initData() {
        Log.v(TAG, "initData")
        val weatherJson = WeatherSharedPreferences.getWeatherForecastJson(this@MainActivity)
        WeatherUpdater.initialize(this)
        if (weatherJson.isNullOrEmpty()) {
            supportLoaderManager.initLoader(WEATHER_SEACH_LOADER, null, this)
        } else {
            getWeatherByDayFromJson(weatherJson!!)
            setDataToAdapter()
        }
    }

    private fun setDataToAdapter() {
        Log.v(TAG, "setDataToAdapter()")
        if (WeatherDataProvider.weatherForecast == null) {
            mForecastAdapter = null
        } else {
            mForecastAdapter = ForecastAdapter(this, this@MainActivity)
            recyclerView!!.adapter = mForecastAdapter
            recyclerView!!.setHasFixedSize(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.weather_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                supportLoaderManager.restartLoader(WEATHER_SEACH_LOADER, null, this)
                true
            }
            R.id.action_map -> {
                val location = WeatherSharedPreferences.getWeatherLocation(this@MainActivity)
                openMap(location)
                true
            }
            R.id.action_settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openMap(location: String?) {
        Log.v(TAG, "open map for location: $location")
        val geoLocation = Uri.parse("geo:0,0?q=$location")
        intent = Intent(Intent.ACTION_VIEW)
        intent.data = geoLocation
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onItemClick(position: Int) {
        Log.v(TAG, "onItemClick on position: $position")
        val detailsIntent = Intent(this@MainActivity, DetailsActivity::class.java)
        detailsIntent.putExtra(DetailsActivity.INTENT_WEATHER_DATA, position)
        startActivity(detailsIntent)
    }

    override fun onLoaderReset(p0: Loader<ArrayList<InternalWeatherForecast>?>) {
        //not implement it
    }

    override fun onLoadFinished(loader: Loader<ArrayList<InternalWeatherForecast>?>, data: ArrayList<InternalWeatherForecast>?) {
        mLoadingIndicator!!.visibility = View.INVISIBLE
        if (data != null) {
            recyclerView!!.visibility = View.VISIBLE
            setDataToAdapter()
        } else {
            mErrorTextView!!.visibility = View.VISIBLE
        }
    }

    override fun onCreateLoader(id: Int, loaderArgs: Bundle?): Loader<ArrayList<InternalWeatherForecast>?> {

        return object : AsyncTaskLoader<ArrayList<InternalWeatherForecast>?>(this) {
            var mWeatherData: ArrayList<InternalWeatherForecast>? = null

            override fun onStartLoading() {
                Log.v(TAG, "onStartLoading weather data")
                if (mWeatherData != null) {
                    deliverResult(mWeatherData)
                } else {
                    mLoadingIndicator!!.visibility = View.VISIBLE
                    forceLoad()
                }
            }

            override fun loadInBackground(): ArrayList<InternalWeatherForecast>? {
                Log.v(TAG, "loadInBackground() in the background thread")
                return WeatherUpdater.startImmediateSync(this@MainActivity)
            }

            override fun deliverResult(data: ArrayList<InternalWeatherForecast>?) {
                Log.v(TAG, "deliverResult() after loading weather data")
                mWeatherData = data
                super.deliverResult(data)
            }
        }
    }

}
