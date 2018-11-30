package com.app.weatherforecast.utils

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL


class NetworkUtilsTest {

    /**
     * test request by url failed id URL is empty
     */
    @Test(expected = IOException::class)
    fun requestByUrlFailedWhenUrlIsIncorrectTest() {
        val url = URL("")
        NetworkUtils.requestByUrl(url)
    }

    /**
     * test request by url successful connect/disconnect
     */
    @Test
    fun requestByUrlCallOpenConnectionConnectDisconnectSuccessTest() {
        val url = mockk<URL>()
        val testStream = "test".byteInputStream()
        val connectionMock = mockk<HttpURLConnection>()
        every { connectionMock.connect() } answers {}
        every { connectionMock.disconnect() } answers {}
        every { connectionMock.inputStream } returns testStream
        every { url.openConnection() } returns connectionMock
        mockkConstructor(InputStreamReader::class)
        mockkConstructor(BufferedReader::class)
        every { anyConstructed<BufferedReader>().readLine() } returns null

        val expected = ""
        val actual = NetworkUtils.requestByUrl(url)
        Assert.assertEquals(expected, actual)

        verify(exactly = 1) { url.openConnection() }
        verify(exactly = 1) { connectionMock.connect() }
        verify(exactly = 1) { connectionMock.disconnect() }
        verify { anyConstructed<BufferedReader>().readLine() }
    }

    /**
     * test request by url throw exception id URL unreachable
     */
    @Test(expected = ConnectException::class)
    fun requestByUrlThrowsExceptionWhenUrlIsInvalidTest() {
        val url = URL("http://0.0.0.0:123") //assuming we don't have any service on this port
        NetworkUtils.requestByUrl(url)
    }


}