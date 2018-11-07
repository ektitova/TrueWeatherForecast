package com.app.weatherforecast.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.forecast_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listResults.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        val adapter = ForecastAdapter(this, context)
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
