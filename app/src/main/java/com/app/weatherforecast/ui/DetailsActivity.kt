package com.app.weatherforecast.ui

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.app.weatherforecast.R
import com.app.weatherforecast.data.InternalDayWeatherForecast
import com.app.weatherforecast.data.InternalWeatherForecast
import com.app.weatherforecast.data.WeatherDataProvider
import com.app.weatherforecast.utils.WeatherDateUtils
import com.app.weatherforecast.utils.WeatherUtils
import android.view.ViewGroup
import android.util.DisplayMetrics
import com.app.weatherforecast.databinding.ActivityDetailBinding



class DetailsActivity : AppCompatActivity() {
    val TAG = DetailsActivity::class.java.simpleName

    companion object {
        const val INTENT_WEATHER_DATA = "WEATHER_DATA"
    }

    var mWeatherData: InternalWeatherForecast? = null
    private var recyclerView: RecyclerView? = null
    private var mDailyForecastAdapter: DailyForecastAdapter? = null
    private var mMetrics: DisplayMetrics = DisplayMetrics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate()")
        setContentView(R.layout.activity_detail)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_detail)
        if (intent != null) {
            windowManager.defaultDisplay.getMetrics(mMetrics)
            val position = intent.getIntExtra(INTENT_WEATHER_DATA, -1)
            if (position != -1) mWeatherData = WeatherDataProvider.weatherForecast!![position]
            binding.details?.weather = mWeatherData
            recyclerView = findViewById(R.id.details_by_time)
            recyclerView!!.layoutManager = CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            setDataToAdapter()
            displayDetails(binding)
        }
    }

    private fun displayDetails(binding: ActivityDetailBinding){
        binding.details?.weatherDate?.text = WeatherDateUtils.getFormattedDate(this,mWeatherData!!.date, true )
        val smallArtResourceId = WeatherUtils
                .getArtResourceForMainWeatherCondition(mWeatherData!!.description)
        binding.details?.weatherIcon?.setImageResource(smallArtResourceId)
        binding.details?.high?.text = WeatherUtils.formatTemperature(this,
                mWeatherData!!.maxTemperature)
        binding.details?.low?.text = WeatherUtils.formatTemperature(this,
                mWeatherData!!.minTemperature)
    }

    private fun setDataToAdapter() {
        Log.v(TAG, "setDataToAdapter()")
        mDailyForecastAdapter = DailyForecastAdapter()
        recyclerView!!.adapter = mDailyForecastAdapter
        recyclerView!!.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.v(TAG, "onOptionsItemSelected " + item.itemId)
        return when (item.itemId) {
            R.id.action_share -> {
                val shareIntent = ShareCompat.IntentBuilder.from(this@DetailsActivity)
                shareIntent.setType("text/plain")
                startActivity(shareIntent.intent)
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

    inner class CenterZoomLayoutManager : LinearLayoutManager {

        private val mShrinkDistance = 0.9f
        private val mShrinkAmount = 0.15f

        constructor(context: Context) : super(context)

        constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)


        override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
            val orientation = orientation
            if (orientation == LinearLayoutManager.VERTICAL) {
                val scrolled = super.scrollVerticallyBy(dy, recycler, state)
                val midpoint = height / 2f
                val d0 = 0f
                val d1 = mShrinkDistance * midpoint
                val s0 = 1f
                val s1 = 1f - mShrinkAmount
                for (i in 0 until childCount) {
                    val child = getChildAt(i)
                    val childMidpoint = (getDecoratedBottom(child!!) + getDecoratedTop(child)) / 2f
                    val d = Math.min(d1, Math.abs(midpoint - childMidpoint))
                    val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                    child.scaleX = scale
                    child.scaleY = scale
                }
                return scrolled
            } else {
                return 0
            }
        }

        override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
            val orientation = orientation
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                val scrolled = super.scrollHorizontallyBy(dx, recycler, state)

                val midpoint = width / 2f
                val d0 = 0f
                val d1 = mShrinkDistance * midpoint
                val s0 = 1f
                val s1 = 1f - mShrinkAmount
                for (i in 0 until childCount) {
                    val child = getChildAt(i)
                    val childMidpoint = (getDecoratedRight(child!!) + getDecoratedLeft(child)) / 2f
                    val d = Math.min(d1, Math.abs(midpoint - childMidpoint))
                    val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                    child.scaleX = scale
                    child.scaleY = scale
                }
                return scrolled
            } else {
                return 0
            }

        }
    }

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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_by_time_item, parent, false)
            view.layoutParams.width = mMetrics!!.widthPixels/3
            view.requestLayout()
            context = parent.context
            return DailyForecastViewHolder(view)
        }


        inner class DailyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val mTimeTextView: TextView = itemView.findViewById(R.id.weather_time)
            private val mDescriptionTextView: TextView = itemView.findViewById(R.id.description)
            private val mWeatherIcon: ImageView = itemView.findViewById(R.id.weather_icon)
            private val mTempTextView: TextView = itemView.findViewById(R.id.temperature)

            init {
                mDescriptionTextView.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            }


            fun bind(value: InternalDayWeatherForecast) {
                mTimeTextView.text = WeatherDateUtils.getFormattedTime(value.time)
                mDescriptionTextView.inputType = InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                mDescriptionTextView.text = value.description!!.capitalize()
                val smallArtResourceId = WeatherUtils.getArtResourceForWeatherCondition(value.weatherId)
                mWeatherIcon.setImageResource(smallArtResourceId)
                mTempTextView.text = WeatherUtils.formatHighLowTemperature(context!!, value.maxTemperature, value.minTemperature)
            }
        }
    }


}
