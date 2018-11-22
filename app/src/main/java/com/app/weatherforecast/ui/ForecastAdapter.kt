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
import com.app.weatherforecast.WeatherByDayItemViewModel
import com.app.weatherforecast.WeatherByTimeItemViewModel
import com.app.weatherforecast.WeatherViewModelFactory
import com.app.weatherforecast.data.InternalWeatherForecast
import com.app.weatherforecast.data.WeatherDataProvider
import com.app.weatherforecast.databinding.ForecastByDayItemBinding
import com.app.weatherforecast.databinding.ForecastDetailsBinding
import com.app.weatherforecast.utils.WeatherDateUtils
import com.app.weatherforecast.utils.WeatherUtils
import kotlinx.android.synthetic.main.forecast_details.view.*
import java.util.*


class ForecastAdapter(clickHandler: ForecastAdapterOnClickHandler,
                      val context: Context?,
                      val weatherForecast:ArrayList<InternalWeatherForecast>) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    val TAG = ForecastAdapter::class.java.simpleName
    private val mClickHandler = clickHandler
    private val VIEW_TYPE_TODAY = 0
    private val VIEW_TYPE_FUTURE_DAY = 1

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder on pos $position")
        val itemVM = WeatherViewModelFactory(context!!, weatherForecast[position], false)
                .create(WeatherByDayItemViewModel::class.java)
        when (holder.viewtype) {
            VIEW_TYPE_TODAY -> {
               (holder.itemViewBinding as ForecastDetailsBinding).viewModel = itemVM
            }
            VIEW_TYPE_FUTURE_DAY -> {
               (holder.itemViewBinding as ForecastByDayItemBinding).viewModel = itemVM
            }
            else ->{
                throw IllegalArgumentException("Illegal view type")
            }
        }
    }

    override fun getItemCount(): Int {
        val size = 5
        Log.v(TAG, "getItemCount() $size")
        return size
    }

    override fun getItemViewType(position: Int): Int {
        val value =  context?.resources?.configuration?.orientation;
        return if (value == Configuration.ORIENTATION_PORTRAIT
                && position == 0) {
            return VIEW_TYPE_TODAY
        } else VIEW_TYPE_FUTURE_DAY
    }

    private fun getScreenOrientation(): Int? {
        return  context?.resources?.configuration?.orientation;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): ForecastViewHolder {
        Log.v(TAG, "onCreateViewHolder")
        var binding: ViewDataBinding?=null
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
        return ForecastViewHolder(binding, viewtype)
    }


    inner class ForecastViewHolder(val itemViewBinding: ViewDataBinding?, val viewtype: Int) : RecyclerView.ViewHolder(itemViewBinding!!.root), View.OnClickListener {
        val TAG = ForecastViewHolder::class.java.simpleName

        init {
            Log.v(TAG, "init")
            itemViewBinding!!.root.setOnClickListener(this)
        }

        override fun onClick(itemView: View?) {
            Log.v(TAG, "onClick() $adapterPosition")
            mClickHandler.onItemClick(adapterPosition)
        }
    }
}

interface ForecastAdapterOnClickHandler {
    fun onItemClick(position: Int)
}