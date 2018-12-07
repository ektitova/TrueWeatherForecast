package com.app.weatherforecast.data

import android.content.Context
import com.app.weatherforecast.TestConstants
import com.app.weatherforecast.utils.NetworkUtils
import com.app.weatherforecast.utils.NotificationUtils
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.URL


class WeatherDataProviderTest {

    private val weatherProvider = WeatherDataProvider()
    private val defaultForecast = WeatherForecast(city = City(id = "4298960", name = "London", coord = Coordinates(lon = -84.0833, lat = 37.129), country = "US", population = 7993), cod = "200", message = 0.0047, cnt = 38, list = arrayListOf(ForecastRecord(dt = 1.542348E9, main = Main(temp = 271.75, temp_min = 271.75, temp_max = 272.249, pressure = 989.98, humidity = 80.0, sea_level = 1034.11, grnd_level = 989.98, temp_kf = -0.5), pressure = 0.0, humidity = 0, weather = arrayListOf(WeatherDescription(id = 600, main = "Snow", desc = "light snow", icon = "13n")), wind = Wind(speed = 2.87, deg = 262.004), clouds = Clouds(all = 76.0)), ForecastRecord(dt = 1.5423588E9, main = Main(temp = 271.17, temp_min = 271.17, temp_max = 271.546, pressure = 990.62, humidity = 82.0, sea_level = 1034.9, grnd_level = 990.62, temp_kf = -0.38), pressure = 0.0, humidity = 0, weather = arrayListOf(WeatherDescription(id = 800, main = "Clear", desc = "clear sky", icon = "01n")), wind = Wind(speed = 2.68, deg = 251.5), clouds = Clouds(all = 44.0)), ForecastRecord(dt = 1.5423696E9, main = Main(temp = 270.35, temp_min = 270.35, temp_max = 270.604, pressure = 991.14, humidity = 88.0, sea_level = 1035.59, grnd_level = 991.14, temp_kf = -0.25), pressure = 0.0, humidity = 0, weather = arrayListOf(WeatherDescription(id = 800, main = "Clear", desc = "clear sky", icon = "01n")), wind = Wind(speed = 2.53, deg = 226.001), clouds = Clouds(all = 0.0))))
    private val contextMock = mockk<Context>()



    @Before
    fun setUp() {
        mockkObject(WeatherSharedPreferences)
        mockkObject(WeatherDataParser)
        mockkObject(NetworkUtils)
        mockkObject(NotificationUtils)
    }

    /**
     * test getWeatherByDayFromJson returns correct data
     */
    @Test
    fun getWeatherByDayFromJsonReturnsWeatherForecastWhenJsonStringIsCorrectTest() {
        val expected = TestConstants.WeatherForecastArray
        every { WeatherDataParser.getWeatherForecastFromJson(any()) } returns defaultForecast
        val actual = weatherProvider.getWeatherByDayFromJson(TestConstants.ForecastJsonString)
        verify(exactly = 1) { WeatherDataParser.getWeatherForecastFromJson(eq(TestConstants.ForecastJsonString)) }
        Assert.assertEquals(expected, actual)
    }

    /**
     * test syncWeather returns correct data
     */
    @Test
    fun syncWeatherReturnsWeatherForecastWhenJsonStringIsCorrectTest() {
        every { WeatherSharedPreferences.getWeatherLocation(any())} returns "Vancouver,ca"
        every { NetworkUtils.buildUrl(any())} returns URL("http://127.0.0.1")
        every { NetworkUtils.requestByUrl(any())} returns TestConstants.ForecastJsonString
        every { WeatherSharedPreferences.saveWeatherForecastJson(any(), any())} answers{}
        every { WeatherSharedPreferences.getNotificationTime(contextMock)} returns 0
        every { NotificationUtils.notifyUserOfWeatherUpdate(contextMock)} answers{}
        every { WeatherSharedPreferences.isNotificationsEnabled(contextMock)} returns true

        val actual = weatherProvider.syncWeather(contextMock)
        Assert.assertEquals(TestConstants.WeatherForecastArray, actual)

        verify(exactly = 1) { WeatherSharedPreferences.getWeatherLocation(any()) }
        verify(exactly = 1) { NetworkUtils.buildUrl(eq("Vancouver,ca")) }
        verify(exactly = 1) { NetworkUtils.requestByUrl(eq(URL("http://127.0.0.1"))) }
        verify(exactly = 1) { WeatherSharedPreferences.saveWeatherForecastJson(any(), eq(TestConstants.ForecastJsonString)) }
    }

    /**
     * test syncWeather returns null if exception
     */
    @Test
    fun syncWeatherReturnsNullWhenExceptionTest() {
        val expected = null
        every { WeatherSharedPreferences.getWeatherLocation(any())} returns "Vancouver,ca"
        every { NetworkUtils.buildUrl(any())} returns URL("http://127.0.0.1")
        every { NetworkUtils.requestByUrl(any())} throws IOException()
        val actual = weatherProvider.syncWeather(contextMock)
        Assert.assertEquals(expected, actual)
    }
}