package com.app.weatherforecast.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.app.weatherforecast.R
import com.app.weatherforecast.data.InternalWeatherForecast
import com.app.weatherforecast.data.WeatherDataProvider
import com.app.weatherforecast.utils.WeatherDateUtils
import com.app.weatherforecast.utils.WeatherUtils
import java.util.*


class ForecastAdapter(clickHandler: ForecastAdapterOnClickHandler) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    val TAG = ForecastAdapter::class.java.simpleName
    private var mWeatherData = WeatherDataProvider.weatherForecast
    private val mClickHandler = clickHandler
    private var context: Context? = null

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder on pos $position")
        holder.bind(mWeatherData!![position])
    }

    override fun getItemCount(): Int {
        val size = 5
        Log.v(TAG, "getItemCount() $size")
        return size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): ForecastViewHolder {
        Log.v(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_by_day_item, parent, false)
        view.layoutParams.height = parent.measuredHeight/5
        context = parent.context
        return ForecastViewHolder(view)
    }


    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val TAG = ForecastViewHolder::class.java.simpleName

        private val mDateTextView: TextView = itemView.findViewById(R.id.weather_date)
        private val mDescriptionTextView: TextView = itemView.findViewById(R.id.description)
        private val mWeatherIcon: ImageView = itemView.findViewById(R.id.weather_icon)
        private val mHighTextView: TextView = itemView.findViewById(R.id.high)
        private val mLowTextView: TextView = itemView.findViewById(R.id.low)

        init {
            Log.v(TAG, "init")
            mDescriptionTextView.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            itemView.setOnClickListener(this)
        }

        override fun onClick(itemView: View?) {
            Log.v(TAG, "onClick() $adapterPosition")
            mClickHandler.onItemClick(adapterPosition)
        }

        fun bind(value: InternalWeatherForecast) {
            Log.v(TAG, "bind " + Date(value.date))
            mDateTextView.text = WeatherDateUtils.getFormattedDate(context!!, value.date, false)
            mDescriptionTextView.text = value.description
           // val smallArtResourceId = WeatherUtils.getArtResourceForWeatherCondition(value.weatherId)
            mWeatherIcon.setImageResource(WeatherUtils.getArtResourceForMainWeatherCondition(value.description))
            val roundedHigh = Math.round(value.maxTemperature)
            val roundedLow = Math.round(value.minTemperature)
            val formattedHigh = WeatherUtils.formatTemperature(context!!, roundedHigh.toDouble())
            val formattedLow = WeatherUtils.formatTemperature(context!!, roundedLow.toDouble())
            mHighTextView.text = formattedHigh
            mLowTextView.text = formattedLow
        }
    }
}

interface ForecastAdapterOnClickHandler {
    fun onItemClick(position: Int)
}