package com.app.weatherforecast

import android.app.Application
import com.app.weatherforecast.data.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import android.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.*
import org.junit.rules.TestRule
import org.junit.Rule


class MainViewModelTest {
    private val applicationMock = mockk<Application>()
    private val mainViewModel = MainViewModel(applicationMock)
    @Rule @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        mockkObject(WeatherSharedPreferences)
    }

    /**
     * test loadWeatherListTest returns correct value if stored json is not null
     */
    @Test
    fun loadWeatherListReturnsWeatherArrayWhenJsonNotNullTest(){
        val expected = TestConstants.WeatherForecastArray
        val weatherDataProviderMock = mockk<WeatherDataProvider>()
        every { WeatherSharedPreferences.getWeatherForecastJson(any())} returns TestConstants.ForecastJsonString
        every { weatherDataProviderMock.getWeatherByDayFromJson(any())} returns TestConstants.WeatherForecastArray
        val actual = mainViewModel.loadWeatherList()
        Assert.assertEquals(expected, actual)
    }

    /**
     * test loadWeatherListTest returns null if stored json is null
     */
    @Test
    fun loadWeatherListReturnsNullWhenJsonNullTest(){
        val expected = null
        every { WeatherSharedPreferences.getWeatherForecastJson(any())} returns null
        val actual = mainViewModel.loadWeatherList()
        Assert.assertEquals(expected, actual)
    }

}