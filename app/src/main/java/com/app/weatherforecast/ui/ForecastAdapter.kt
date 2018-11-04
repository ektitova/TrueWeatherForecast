package com.app.weatherforecast.ui

import android.content.Context
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.weatherforecast.R
import com.app.weatherforecast.data.InternalWeatherForecast
import com.app.weatherforecast.data.WeatherDataProvider
import com.app.weatherforecast.utils.WeatherDateUtils
import com.app.weatherforecast.utils.WeatherUtils
import kotlinx.android.synthetic.main.forecast_details.view.*
import java.util.*


class ForecastAdapter(clickHandler: ForecastAdapterOnClickHandler, context: Context) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    val TAG = ForecastAdapter::class.java.simpleName
    private var mWeatherData = WeatherDataProvider.weatherForecast
    private val mClickHandler = clickHandler
    private var mContext = context
    private val VIEW_TYPE_TODAY = 0
    private val VIEW_TYPE_FUTURE_DAY = 1

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder on pos $position")
        holder.bind(mWeatherData!![position])
    }

    override fun getItemCount(): Int {
        val size = 5
        Log.v(TAG, "getItemCount() $size")
        return size
    }

    override fun getItemViewType(position: Int): Int {
        val value =  mContext.resources.configuration.orientation;
        return if (value == Configuration.ORIENTATION_PORTRAIT
                && position == 0) {
            return VIEW_TYPE_TODAY
        } else VIEW_TYPE_FUTURE_DAY
    }

    fun getScreenOrientation(): Int {
        return  mContext.resources.configuration.orientation;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): ForecastViewHolder {
        Log.v(TAG, "onCreateViewHolder")
        var binding: ViewDataBinding
        val screenOrientation = getScreenOrientation()
        when (viewtype) {
            VIEW_TYPE_TODAY -> {
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                        R.layout.forecast_details, parent, false)
                if (screenOrientation == Configuration.ORIENTATION_PORTRAIT)
                    binding.root.layoutParams.height = parent.measuredHeight / 3
            }
            VIEW_TYPE_FUTURE_DAY -> {
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                        R.layout.forecast_by_day_item, parent, false)
                if (screenOrientation == Configuration.ORIENTATION_PORTRAIT)
                    binding.root.layoutParams.height = parent.measuredHeight / 6
            }
            else ->{
                throw IllegalArgumentException("Illegal view type")
            }
        }
        return ForecastViewHolder(binding)
    }


    inner class ForecastViewHolder(itemView: ViewDataBinding) : RecyclerView.ViewHolder(itemView.root), View.OnClickListener {
        val TAG = ForecastViewHolder::class.java.simpleName

        init {
            Log.v(TAG, "init")
            itemView.root.setOnClickListener(this)
        }

        override fun onClick(itemView: View?) {
            Log.v(TAG, "onClick() $adapterPosition")
            mClickHandler.onItemClick(adapterPosition)
        }

        fun bind(value: InternalWeatherForecast) {
            Log.v(TAG, "bind " + Date(value.date))
            itemView.weather_date.text = WeatherDateUtils.getFormattedDate( mContext, value.date, false)
            itemView.description.text = value.description
            itemView.weather_icon.setImageResource(WeatherUtils.getArtResourceForMainWeatherCondition(value.description))
            val roundedHigh = Math.round(value.maxTemperature)
            val roundedLow = Math.round(value.minTemperature)
            val formattedHigh = WeatherUtils.formatTemperature( mContext, roundedHigh.toDouble())
            val formattedLow = WeatherUtils.formatTemperature( mContext, roundedLow.toDouble())
            itemView.high.text = formattedHigh
            itemView.low.text = formattedLow
        }
    }
}

interface ForecastAdapterOnClickHandler {
    fun onItemClick(position: Int)
}