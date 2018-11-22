package com.app.weatherforecast.utils


import android.net.Uri
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


object NetworkUtils {

    private val TAG = NetworkUtils::class.java.simpleName

    private val OPEN_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast"
    private val format = "json"
    private val QUERY_PARAM = "q"
    private val FORMAT_PARAM = "mode"
    private val APPID_PARAM = "APPID"

    //my app id from api.openweathermap.org
    private const val APPID = "0b60d3c4a689126c33b469b947fc3c6e"


    fun buildUrl(locationQuery: String): URL {
        Log.v(TAG, "build URL request string for location: $locationQuery")
        val builtUri = Uri.parse(OPEN_WEATHER_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, locationQuery)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(APPID_PARAM, APPID).build()
        var url = URL(builtUri.toString())
        Log.v(TAG, "Built URI $url")
        return url
    }


    @Throws(IOException::class)
    fun requestByUrl(url: URL): String? {
        val conn = url.openConnection() as HttpURLConnection
        var stream: InputStream? = null
        try {
            conn.connect()
            stream = conn.inputStream

            val stringBuilder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(stream))
            var line = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = reader.readLine()
            }
            return stringBuilder.toString()
        } finally {
            conn.disconnect()
            if (stream != null) {
                stream.close()
            }
        }
    }
}