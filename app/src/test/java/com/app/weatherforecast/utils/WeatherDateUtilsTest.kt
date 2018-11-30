package com.app.weatherforecast.utils

import org.junit.Assert
import org.junit.Test


class WeatherDateUtilsTest {

    /**
     * test getDayNumber returns correct value
     */
    @Test
    fun getDayNumberReturnsCorrectTest() {
        val dateInMilliSec: Long = 24 * 60 * 60 * 1000 * 2
        val expected: Long = 1
        val actual = WeatherDateUtils.getDayNumber(dateInMilliSec)
        Assert.assertEquals(expected, actual)
    }

    /**
     * test getDayNumber returns correct value if parameter is negative
     */
    @Test
    fun getDayNumberReturnsCorrectWhenNegativeTest() {
        val dateInMilliSec: Long = -1
        val expected: Long = 0
        val actual = WeatherDateUtils.getDayNumber(dateInMilliSec)
        Assert.assertEquals(expected, actual)
    }

    /**
     * test getFormattedTime returns correct value
     */
    @Test
    fun getFormattedTimeReturnsCorrectTest() {
        val dateInMilliSec: Long = 24 * 60 * 60 * 1000 + 60 * 1000
        val expected = "00:01"
        val actual = WeatherDateUtils.getFormattedTime(dateInMilliSec)
        Assert.assertEquals(expected, actual)
    }

    /**
     * test getFormattedTime returns correct value if parameter is negative
     */
    @Test
    fun getFormattedTimeReturnsCorrectWhenNegativeTest() {
        val dateInMilliSec: Long = -1
        val expected = ""
        val actual = WeatherDateUtils.getFormattedTime(dateInMilliSec)
        Assert.assertEquals(expected, actual)
    }

}