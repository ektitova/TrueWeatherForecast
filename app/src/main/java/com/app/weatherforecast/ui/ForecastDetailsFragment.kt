package com.app.weatherforecast.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.weatherforecast.*
import com.app.weatherforecast.data.InternalWeatherForecast
import com.app.weatherforecast.databinding.ForecastByTimeItemBinding
import com.app.weatherforecast.databinding.ForecastDetailsFragmentBinding
import kotlinx.android.synthetic.main.forecast_details_fragment.*


class ForecastDetailsFragment : Fragment() {

    private var mDailyForecastAdapter: DailyForecastAdapter? = null
    private var mMetrics: DisplayMetrics? = null
    private lateinit var mWeatherData: InternalWeatherForecast

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: ForecastDetailsFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.forecast_details_fragment, container, false)
        binding.root.layoutParams.width = container!!.width
        binding.root.requestLayout()
        mWeatherData = getData()
        val itemVM = WeatherViewModelFactory(context!!, mWeatherData, true).create(WeatherByDayItemViewModel::class.java)
        binding.details?.viewModel = itemVM
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun getData(): InternalWeatherForecast {
        Log.v(ForecastListFragment.TAG, "init data")
        val viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        val bundle = arguments
        val position = bundle!!.getInt(SELECTED_ITEM_NUMBER)
        Log.v(TAG, "detail screen loaded with item number $position")
        return viewModel.weatherForecast.value!![position]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailsByTimeView!!.layoutManager = CenterZoomLayoutManager(activity!!.applicationContext, LinearLayoutManager.HORIZONTAL, false)
        setDataToAdapter()
        mMetrics = DisplayMetrics()
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val SELECTED_ITEM_NUMBER = "selected item"
        fun newInstance() = ForecastDetailsFragment()
        val TAG = ForecastDetailsFragment::class.java.simpleName
    }

    private fun setDataToAdapter() {
        Log.v(TAG, "setDataToAdapter()")
        mDailyForecastAdapter = DailyForecastAdapter()
        detailsByTimeView.adapter = mDailyForecastAdapter
        detailsByTimeView.setHasFixedSize(true)
    }

    inner class DailyForecastAdapter : RecyclerView.Adapter<DailyForecastAdapter.DailyForecastViewHolder>() {
        private var mDailyWeatherData = mWeatherData.dailyForecasts
        private lateinit var context: Context

        override fun onBindViewHolder(holderDaily: DailyForecastViewHolder, position: Int) {
            val itemVM = WeatherViewModelFactory(context, mDailyWeatherData[position]).create(WeatherByTimeItemViewModel::class.java)
            holderDaily.itemViewBinding.viewModel = itemVM
        }

        override fun getItemCount(): Int {
            return mDailyWeatherData.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): DailyForecastViewHolder {
            val binding: ForecastByTimeItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.forecast_by_time_item, parent, false)
            context = parent.context
            return DailyForecastViewHolder(binding)
        }

        inner class DailyForecastViewHolder(val itemViewBinding: ForecastByTimeItemBinding) : RecyclerView.ViewHolder(itemViewBinding.root)

    }


}
