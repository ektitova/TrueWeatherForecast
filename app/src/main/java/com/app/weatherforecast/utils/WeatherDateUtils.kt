package com.app.weatherforecast.utils

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import com.app.weatherforecast.R
import java.text.SimpleDateFormat
import java.util.*

object WeatherDateUtils {
    private val TAG = WeatherDateUtils::class.java.simpleName

    private const val SECOND_IN_MILLIS: Long = 1000
    private const val MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60
    private const val HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60
    private const val DAY_IN_MILLIS = HOUR_IN_MILLIS * 24


    /**
     * This method returns the number of days since the epoch January 01, 1970
     */
    fun getDayNumber(date: Long): Long {
        Log.v(TAG, "get day number from the date: " + Date(date))
        val tz = TimeZone.getDefault()
        val gmtOffset = tz.getOffset(date).toLong()
        return (date + gmtOffset) / DAY_IN_MILLIS
    }

    /**
     * This method returns formatted time to display in HH:mm
     */
    fun getFormattedTime(date: Long): String {
        Log.v(TAG, "get formatted time from the date: " + Date(date))
        val localDateFormat = SimpleDateFormat("HH:mm")
        return localDateFormat.format(date)
    }

    /**
     * This method returns formatted date in Date
     */
    fun getFormattedDate(context: Context, dateInMillis: Long, showFullDate: Boolean): String {
        Log.v(TAG, "get formatted date from the ms: $dateInMillis with details $showFullDate")
        //convert the given date in UTC timezone to the date in the local timezone
        val localDate = dateInMillis - TimeZone.getDefault().getOffset(dateInMillis).toLong()
        val dayNumber = getDayNumber(localDate)
        val currentDayNumber = getDayNumber(System.currentTimeMillis())
        if (dayNumber == currentDayNumber || showFullDate) {
            val readableDate = DateUtils.formatDateTime(context, localDate, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NO_YEAR or DateUtils.FORMAT_SHOW_WEEKDAY)
            //insert Today
            if (dayNumber - currentDayNumber < 1) {
                val localizedDayName = SimpleDateFormat("EEEE").format(localDate)
                return readableDate.replace(localizedDayName, context.getString(R.string.today))
            } else {
                return readableDate
            }
        } else {
            val flags = (DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NO_YEAR or DateUtils.FORMAT_ABBREV_ALL or DateUtils.FORMAT_SHOW_WEEKDAY)
            return DateUtils.formatDateTime(context, localDate, flags)
        }
    }

}