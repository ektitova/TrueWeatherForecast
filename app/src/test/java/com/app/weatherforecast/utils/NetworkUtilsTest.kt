package com.app.weatherforecast.utils

import android.net.Uri
import android.util.Log
import com.app.weatherforecast.data.InternalDayWeatherForecast
import com.app.weatherforecast.data.InternalWeatherForecast
import com.app.weatherforecast.data.WeatherDataParser
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.io.IOException
import java.net.URL


@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class,Uri::class)
class NetworkUtilsTest {

    @Before
    fun setUp() {
        PowerMockito.mockStatic(Log::class.java)
        PowerMockito.mockStatic(Uri::class.java)
    }

    @Test(expected = IOException::class)
    fun requestByUrlFailedWhenUrlIsIncorrectTest() {
        val url = URL("")
        NetworkUtils.requestByUrl(url)
     }



    @Test
    fun buildUrlReturnsUrlIfLocationIsCorrect() {
        val location = "London,uk"
      // every {Uri.parse(any())} returns Uri.parse("https://api.openweathermap.org/data/2.5/forecast")
        val expected =URL("https://api.openweathermap.org/data/2.5/forecast?q=Vancouver%2Cca&mode=json&APPID=0b60d3c4a689126c33b469b947fc3c6e")
        val actual = NetworkUtils.buildUrl(location)
        Assert.assertEquals(expected, actual)
    }

    @Test(expected = MismatchedInputException::class)
    fun getWeatherForecastFromJsonFailedWhenJsonStringStructureIsIncorrectTest() {
        val forecastJsonString = "{\"cod\":\"200\",\"cnt\":38,\"list\":\"sea_level\":1034.11,\"grnd_level\":989.98,\"humidity\":80,\"temp_kf\":-0.5},\"weather\":[{\"id\":600,\"main\":\"Snow\",\"description\":\"light snow\",\"icon\":\"13n\"}],\"clouds\":{\"all\":76},\"wind\":{\"speed\":2.87,\"deg\":262.004},\"rain\":{},\"snow\":{\"3h\":0.048},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-11-16 06:00:00\"},{\"dt\":1542358800,\"main\":{\"temp\":271.17,\"temp_min\":271.17,\"temp_max\":271.546,\"pressure\":990.62,\"sea_level\":1034.9,\"grnd_level\":990.62,\"humidity\":82,\"temp_kf\":-0.38},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":44},\"wind\":{\"speed\":2.68,\"deg\":251.5},\"rain\":{},\"snow\":{\"3h\":0.013},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-11-16 09:00:00\"},{\"dt\":1542369600,\"main\":{\"temp\":270.35,\"temp_min\":270.35,\"temp_max\":270.604,\"pressure\":991.14,\"sea_level\":1035.59,\"grnd_level\":991.14,\"humidity\":88,\"temp_kf\":-0.25},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.53,\"deg\":226.001},\"rain\":{},\"snow\":{},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-11-16 12:00:00\"}],\"city\":{\"id\":4298960,\"name\":\"London\",\"coord\":{\"lat\":37.129,\"lon\":-84.0833},\"country\":\"US\",\"population\":7993}}"
    }


}