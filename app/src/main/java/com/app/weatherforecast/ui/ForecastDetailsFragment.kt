package com.app.weatherforecast.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ShareCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.util.Log
import android.view.*
import com.app.weatherforecast.R
import com.app.weatherforecast.data.InternalDayWeatherForecast
import com.app.weatherforecast.data.InternalWeatherForecast
import com.app.weatherforecast.data.WeatherDataProvider
import com.app.weatherforecast.utils.WeatherDateUtils
import com.app.weatherforecast.utils.WeatherUtils
import android.view.ViewGroup
import android.util.DisplayMetrics

import com.app.weatherforecast.databinding.ForecastByTimeItemBinding
import com.app.weatherforecast.databinding.ForecastDetailsFragmentBinding
import kotlinx.android.synthetic.main.forecast_by_time_item.view.*
import kotlinx.android.synthetic.main.forecast_details.*
import kotlinx.android.synthetic.main.forecast_details_fragment.*



class ForecastDetailsFragment : Fragment(){
    val TAG = ForecastDetailsFragment::class.java.simpleName
    var mWeatherData: InternalWeatherForecast? = null
    private var mDailyForecastAdapter: DailyForecastAdapter? = null
    private var mMetrics: DisplayMetrics?= null
   // var carData = CarDataModel.carData

//    constructor ForecastDetailsFragment(): Fragment(){
//        setHasOptionsMenu(true)
//    }

    init{
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: ForecastDetailsFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.forecast_details_fragment, container, false)
        binding.root.layoutParams.width= container!!.width
        binding.root.requestLayout()
        val bundle = arguments
        val position = bundle!!.getInt(SELECTED_ITEM_NUMBER)
        mWeatherData = WeatherDataProvider.weatherForecast!![position]
        Log.v(TAG, "detail screen loaded with item number $position")
        binding.details?.weather = mWeatherData
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailsByTimeView!!.layoutManager = CenterZoomLayoutManager(activity!!.applicationContext, LinearLayoutManager.HORIZONTAL, false)
        displayDetails()
        setDataToAdapter()
        mMetrics = DisplayMetrics()
        super.onViewCreated(view, savedInstanceState)
     }

    companion object {
        const val SELECTED_ITEM_NUMBER = "selected item"
        fun newInstance() = ForecastDetailsFragment()
    }



    private fun displayDetails(){
        weather_date.text = WeatherDateUtils.getFormattedDate(activity!!.applicationContext ,mWeatherData!!.date, true )
        val smallArtResourceId = WeatherUtils
                .getArtResourceForMainWeatherCondition(mWeatherData!!.description)
        weather_icon.setImageResource(smallArtResourceId)
        high.text = WeatherUtils.formatTemperature(activity!!.applicationContext,
                mWeatherData!!.maxTemperature)
        low.text = WeatherUtils.formatTemperature(activity!!.applicationContext,
                mWeatherData!!.minTemperature)
        description.text = mWeatherData!!.description
    }

    private fun setDataToAdapter() {
        Log.v(TAG, "setDataToAdapter()")
        mDailyForecastAdapter = DailyForecastAdapter()
        detailsByTimeView.adapter = mDailyForecastAdapter
        detailsByTimeView.setHasFixedSize(true)
    }



//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.v(TAG, "onOptionsItemSelected " + item.itemId)
//        return when (item.itemId) {
//            R.id.action_share -> {
//                val shareIntent = ShareCompat.IntentBuilder.from(this@ForecastDetailsFragment)
//                shareIntent.setType("text/plain")
//                shareIntent.intent.putExtra(Intent.EXTRA_TEXT, mWeatherData.toString())
//                startActivity(shareIntent.intent)
//                true
//            }
//            R.id.action_settings -> {
//                val intent = Intent(this, SettingsActivity::class.java)
//                startActivity(intent)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }



    inner class DailyForecastAdapter : RecyclerView.Adapter<DailyForecastAdapter.DailyForecastViewHolder>() {
        private var mDailyWeatherData = mWeatherData!!.dailyForecasts
        private var context: Context? = null

        override fun onBindViewHolder(holderDaily: DailyForecastViewHolder, position: Int) {
            holderDaily.bind(mDailyWeatherData!![position])
        }

        override fun getItemCount(): Int {
            return mDailyWeatherData!!.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): DailyForecastViewHolder {
            val binding: ForecastByTimeItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.forecast_by_time_item, parent, false)
            context = parent.context
            return DailyForecastViewHolder(binding)
        }

        inner class DailyForecastViewHolder(itemView: ForecastByTimeItemBinding) : RecyclerView.ViewHolder(itemView.root) {

            fun bind(value: InternalDayWeatherForecast) {
                itemView.weather_time.text = WeatherDateUtils.getFormattedTime(value.time)
                itemView.description.text = value.description!!.capitalize()
                itemView.description.inputType =InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                val smallArtResourceId = WeatherUtils.getArtResourceForWeatherCondition(value.weatherId)
                itemView.weather_icon.setImageResource(smallArtResourceId)
                itemView.temperature.text = WeatherUtils.formatHighLowTemperature(context!!, value.maxTemperature, value.minTemperature)
            }
        }
    }


}
