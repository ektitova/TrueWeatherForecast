package com.app.weatherforecast.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.app.weatherforecast.R
import com.app.weatherforecast.data.WeatherSharedPreferences


class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {
    private val TAG = MainActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate()")
        setContentView(R.layout.activity_main)
        val mainFragment = ForecastListFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.contentFrame, mainFragment).commit()
    }

    /**
     * update fragment view
     */
    private fun updateView(fragment: Fragment, TAG: String) {
        supportFragmentManager.beginTransaction().replace(R.id.contentFrame, fragment,TAG).addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "onResume()")
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
                val settingsFragmentFragment = SettingsFragment.newInstance()
                updateView(settingsFragmentFragment, SettingsFragment.TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * open map with current location
     */
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
        Log.v(TAG, "fragment message from: $TAG")
        if (TAG == ForecastListFragment.TAG) {
            val bundle = Bundle()
            bundle.putInt(ForecastDetailsFragment.SELECTED_ITEM_NUMBER, data as Int)
            val detailFragment = ForecastDetailsFragment.newInstance()
            detailFragment.arguments = bundle
            updateView(detailFragment, ForecastDetailsFragment.TAG)
        }
    }

}
