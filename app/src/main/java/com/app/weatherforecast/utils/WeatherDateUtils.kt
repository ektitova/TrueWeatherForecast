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
    fun getDayNumber(dateInMillis: Long): Long {
        Log.v(TAG, "get day number from the date: " + Date(dateInMillis))
        val tz = TimeZone.getDefault()
        val gmtOffset = tz.getOffset(dateInMillis).toLong()
        return (dateInMillis + gmtOffset) / DAY_IN_MILLIS
    }

    /**
     * This method returns formatted time to display in HH:mm
     */
    fun getFormattedTime(dateInMillis: Long): String {
        Log.v(TAG, "get formatted time from the date: " + Date(dateInMillis))
        if (dateInMillis < 0) return ""
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(dateInMillis)
    }

    /**
     * This method returns formatted date in Date
     */
    fun getFormattedDate(context: Context, dateInMillis: Long, showFullDate: Boolean): String {
        Log.v(TAG, "get formatted date from the ms: $dateInMillis with details $showFullDate")
        //convert the given date in UTC timezone to the date in the local timezone
        val dayNumber = getDayNumber(dateInMillis)
        val currentDayNumber = getDayNumber(System.currentTimeMillis())
        if (dayNumber == currentDayNumber || showFullDate) {
            val readableDate = DateUtils.formatDateTime(context, dateInMillis, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NO_YEAR or DateUtils.FORMAT_SHOW_WEEKDAY)
            //insert Today
            if (dayNumber - currentDayNumber < 1) {
                val localizedDayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(dateInMillis)
                return readableDate.replace(localizedDayName, context.getString(R.string.today))
            } else {
                return readableDate
            }
        } else {
            val flags = (DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NO_YEAR or DateUtils.FORMAT_ABBREV_ALL or DateUtils.FORMAT_SHOW_WEEKDAY)
            return DateUtils.formatDateTime(context, dateInMillis, flags)
        }
    }

}