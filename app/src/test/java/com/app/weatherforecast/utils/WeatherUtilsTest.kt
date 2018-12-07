package com.app.weatherforecast.utils

import android.app.Application
import android.content.Context
import com.app.weatherforecast.TestConstants
import com.app.weatherforecast.data.WeatherSharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WeatherUtilsTest {
    private val contextMock = mockk<Context>()

    @Before
    fun setUp() {
        mockkObject(WeatherSharedPreferences)
    }

    /**
     * test format temperature returns correct value in celsuis
     */
    @Test
    fun formatTemperatureReturnsCorrectInCelsiusTest() {
        val expected = "0\u00B0C"
        val temperatureInKelvin = 273.17
        every { WeatherSharedPreferences.isMetric(contextMock)} returns true
        val actual = WeatherUtils.formatTemperature(contextMock, temperatureInKelvin)
        Assert.assertEquals(expected, actual)
    }

    /**
     * test format temperature returns correct value in fahrenheit
     */
    @Test
    fun formatTemperatureReturnsCorrectInFahrenheitTest() {
        val expected = "50\u00B0F"
        val temperatureInKelvin = 283.18
        every { WeatherSharedPreferences.isMetric(contextMock)} returns false
        val actual = WeatherUtils.formatTemperature(contextMock, temperatureInKelvin)
        Assert.assertEquals(expected, actual)
    }

}