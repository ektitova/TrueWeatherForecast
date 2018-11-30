package com.app.weatherforecast.utils

import org.junit.Assert
import org.junit.Test


class WeatherDateUtilsTest {

    @Test
    fun getDayNumberReturnsCorrectTest() {
        val dateInMilliSec: Long = 24 * 60 * 60 * 1000 * 2
        val expected: Long = 1
        val actual = WeatherDateUtils.getDayNumber(dateInMilliSec)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getDayNumberReturnsCorrectWhenNegativeTest() {
        val dateInMilliSec: Long = -1
        val expected: Long = 0
        val actual = WeatherDateUtils.getDayNumber(dateInMilliSec)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getFormattedTimeReturnsCorrectTest() {
        val dateInMilliSec: Long = 24 * 60 * 60 * 1000 + 60 * 1000
        val expected = "00:01"
        val actual = WeatherDateUtils.getFormattedTime(dateInMilliSec)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getFormattedTimeReturnsCorrectWhenNegativeTest() {
        val dateInMilliSec: Long = -1
        val expected = ""
        val actual = WeatherDateUtils.getFormattedTime(dateInMilliSec)
        Assert.assertEquals(expected, actual)
    }

}