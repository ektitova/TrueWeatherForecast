package com.app.weatherforecast.ui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.app.weatherforecast.R
import com.app.weatherforecast.data.InternalWeatherForecast
import com.app.weatherforecast.data.WeatherDataProvider
import com.app.weatherforecast.data.WeatherSharedPreferences

import android.support.v4.widget.SwipeRefreshLayout
import com.app.weatherforecast.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {
    private val TAG = MainActivity::class.java.simpleName

    private var mErrorTextView: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var mForecastAdapter: ForecastAdapter? = null
    private var mSwipeContainer: SwipeRefreshLayout? = null
    private var viewModel:MainViewModel ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate()")
        setContentView(R.layout.activity_main)
        mErrorTextView = findViewById(R.id.error_message)
        // mSwipeContainer = findViewById<View>(R.id.swipeContainer) as SwipeRefreshLayout
        // Setup refresh listener which triggers new data loading
        swipeContainer?.setOnRefreshListener {
            viewModel?.loadWeatherList()
        }
        // Configure the refreshing colors
        swipeContainer?.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light)
        initData()
    }

    private fun updateView(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack(null).commit()
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "onResume()")
        if (WeatherDataProvider.weatherForecast == null) return
      //  setDataToAdapter()
    }

    private fun initData() {
        Log.v(TAG, "init data")
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel?.initialize()
        viewModel?.weatherForecast?.observe(this, Observer { weatherForecast: List<InternalWeatherForecast>? ->
            onDataLoaded(weatherForecast)
        })
        viewModel?.loadWeatherList()
        mSwipeContainer?.isRefreshing = true
    }


    private fun onDataLoaded(weatherForecast: List<InternalWeatherForecast>?) {
        mSwipeContainer?.isRefreshing = false
        if (weatherForecast != null) {
            Log.v(TAG, "Car list is loaded, number of items: " + weatherForecast.size)
            displayForecastList()
        } else {
            recyclerView?.visibility = View.INVISIBLE
            mErrorTextView?.visibility = View.VISIBLE
        }
    }


    private fun displayForecastList() {
        Log.v(TAG, "display car list")
        val fragment = ForecastListFragment.newInstance()
        updateView(fragment)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.weather_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_map -> {
                val location = WeatherSharedPreferences.getWeatherLocation(this@MainActivity)
                openMap(location)
                true
            }
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
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


    override fun onFragmentMessage(TAG: String, data: Any) {
        Log.v(TAG, "fragment message from: " + TAG)
        if (TAG == ForecastListFragment.TAG) {
            val bundle = Bundle()
            bundle.putInt(ForecastDetailsFragment.SELECTED_ITEM_NUMBER, data as Int)
            val detailFragment = ForecastDetailsFragment.newInstance()
            detailFragment.arguments = bundle
            updateView(detailFragment)
        }
    }

}
