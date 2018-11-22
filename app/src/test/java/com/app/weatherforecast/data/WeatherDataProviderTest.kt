package com.app.weatherforecast.data

import android.util.Log
import io.mockk.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner


@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class,WeatherDataParser::class)
class WeatherDataProviderTest {

    private val weatherProvider = WeatherDataProvider()
    private val defaultForecast = WeatherForecast(city=City(id="4298960", name="London",
            coord=Coordinates(lon=-84.0833, lat=37.129), country="US", population=7993),
            cod="200", message=0.0047, cnt=38,
            list=arrayListOf(ForecastRecord(dt=1.542348E9, main=Main(temp=271.75, temp_min=271.75, temp_max=272.249, pressure=989.98, humidity=80.0, sea_level=1034.11, grnd_level=989.98, temp_kf=-0.5), pressure=0.0, humidity=0,
                    weather=arrayListOf(WeatherDescription(id=600, main="Snow", desc="light snow", icon="13n")),
                    wind=Wind(speed=2.87, deg=262.004), clouds=Clouds(all=76.0)),
                    ForecastRecord(dt=1.5423588E9, main=Main(temp=271.17, temp_min=271.17,
                            temp_max=271.546, pressure=990.62, humidity=82.0, sea_level=1034.9, grnd_level=990.62, temp_kf=-0.38), pressure=0.0, humidity=0,
                            weather=arrayListOf(WeatherDescription(id=800, main="Clear", desc="clear sky", icon="01n")),
                            wind=Wind(speed=2.68, deg=251.5), clouds=Clouds(all=44.0)),
                    ForecastRecord(dt=1.5423696E9, main=Main(temp=270.35, temp_min=270.35, temp_max=270.604, pressure=991.14, humidity=88.0, sea_level=1035.59, grnd_level=991.14, temp_kf=-0.25), pressure=0.0, humidity=0,
                            weather= arrayListOf(WeatherDescription(id=800, main="Clear", desc="clear sky", icon="01n")),
                            wind=Wind(speed=2.53, deg=226.001), clouds=Clouds(all=0.0))))
    @Before
    fun setUp() {
       PowerMockito.mockStatic(Log::class.java)
       mockkObject(WeatherDataParser)
    }

    @Test
    fun getWeatherByDayFromJsonReturnsWeatherForecastWhenJsonStringIsCorrectTest() {
        val forecastJsonString = "{\"cod\":\"200\",\"message\":0.0047,\"cnt\":38,\"list\":[{\"dt\":1542348000,\"main\":{\"temp\":271.75,\"temp_min\":271.75,\"temp_max\":272.249,\"pressure\":989.98,\"sea_level\":1034.11,\"grnd_level\":989.98,\"humidity\":80,\"temp_kf\":-0.5},\"weather\":[{\"id\":600,\"main\":\"Snow\",\"description\":\"light snow\",\"icon\":\"13n\"}],\"clouds\":{\"all\":76},\"wind\":{\"speed\":2.87,\"deg\":262.004},\"rain\":{},\"snow\":{\"3h\":0.048},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-11-16 06:00:00\"},{\"dt\":1542358800,\"main\":{\"temp\":271.17,\"temp_min\":271.17,\"temp_max\":271.546,\"pressure\":990.62,\"sea_level\":1034.9,\"grnd_level\":990.62,\"humidity\":82,\"temp_kf\":-0.38},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":44},\"wind\":{\"speed\":2.68,\"deg\":251.5},\"rain\":{},\"snow\":{\"3h\":0.013},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-11-16 09:00:00\"},{\"dt\":1542369600,\"main\":{\"temp\":270.35,\"temp_min\":270.35,\"temp_max\":270.604,\"pressure\":991.14,\"sea_level\":1035.59,\"grnd_level\":991.14,\"humidity\":88,\"temp_kf\":-0.25},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.53,\"deg\":226.001},\"rain\":{},\"snow\":{},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-11-16 12:00:00\"}],\"city\":{\"id\":4298960,\"name\":\"London\",\"coord\":{\"lat\":37.129,\"lon\":-84.0833},\"country\":\"US\",\"population\":7993}}"
        val expected = arrayListOf(
                InternalWeatherForecast(date=1542348000000, maxTemperature=272.249, minTemperature=271.75, description="Snow", dailyForecasts=arrayListOf(InternalDayWeatherForecast(weatherId=600, time=1542348000000, humidity=0, pressure=0.0, windSpeed=2.87, maxTemperature=272.249, minTemperature=271.75, description="light snow"))),
                InternalWeatherForecast(date=1542358800000, maxTemperature=271.546, minTemperature=270.35, description="Clear", dailyForecasts=arrayListOf(InternalDayWeatherForecast(weatherId=800, time=1542358800000, humidity=0, pressure=0.0, windSpeed=2.68, maxTemperature=271.546, minTemperature=271.17, description="clear sky"), InternalDayWeatherForecast(weatherId=800, time=1542369600000, humidity=0, pressure=0.0, windSpeed=2.53, maxTemperature=270.604, minTemperature=270.35, description="clear sky")))
        )

       //PowerMockito.`when`<WeatherForecast>(WeatherDataParser.getWeatherForecastFromJson(o.))
           //  .thenReturn(defaultForecast)
        every { WeatherDataParser.getWeatherForecastFromJson(any()) } returns defaultForecast
        val actual = weatherProvider.getWeatherByDayFromJson(forecastJsonString)
        Assert.assertEquals(expected, actual)

    }


}