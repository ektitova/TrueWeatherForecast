package com.app.weatherforecast.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.app.weatherforecast.MainViewModel
import com.app.weatherforecast.R
import com.app.weatherforecast.data.InternalWeatherForecast
import kotlinx.android.synthetic.main.forecast_list.*

class ForecastListFragment : Fragment(), ForecastAdapterOnClickHandler {

    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.forecast_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeContainer.setOnRefreshListener {
            viewModel.reloadWeatherList()
        }
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light)
        initData()
    }

    /**
     * get weather forecast list
     */
    private fun initData() {
        Log.v(TAG, "init data")
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        viewModel.initialize()
        viewModel.weatherForecast.observe(this, Observer { weatherForecast: List<InternalWeatherForecast>? ->
            onDataLoaded(weatherForecast)
        })
        viewModel.loadWeatherList()
        swipeContainer?.isRefreshing = true
    }

    /**
     * update view once weather forecast is loaded, display error if not
     */
    private fun onDataLoaded(weatherForecast: List<InternalWeatherForecast>?) {
        swipeContainer?.isRefreshing = false
        if (weatherForecast != null) {
            Log.v(TAG, "Car list is loaded, number of items: " + weatherForecast.size)
            displayForecastList()
        } else {
            listResults.visibility = View.INVISIBLE
            errorMessage.visibility = View.VISIBLE
        }
    }

    /**
     * display weather forecast list once it is loaded
     */
    private fun displayForecastList() {
        Log.v(TAG, "display weather list")
        listResults.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        val adapter = ForecastAdapter(this, context, viewModel.weatherForecast.value!!)
        listResults.adapter = adapter
        listResults.setHasFixedSize(true)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }


    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {
        val TAG = ForecastListFragment::class.java.simpleName
        fun newInstance() = ForecastListFragment()
    }

    override fun onItemClick(position: Int) {
        Log.v(TAG, "onItemClick on position: $position")
        mListener!!.onFragmentMessage(TAG, position)
    }

}
