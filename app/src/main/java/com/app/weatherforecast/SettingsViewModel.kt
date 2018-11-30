package com.app.weatherforecast

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.app.weatherforecast.utils.NotificationUtils

class SettingsViewModel : ViewModel() {
    private val TAG = MainViewModel::class.java.simpleName

    /**
     * Email client intent to send support mail
     * Appends the necessary device information to email body
     * useful when providing support
     */
    fun sendFeedback(context: Context) {
        Log.v(TAG, "sendFeedback()")
        var body: String? = null
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " + Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND + "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER
        } catch (e: PackageManager.NameNotFoundException) {
        }

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ektitova10@gnail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app")
        intent.putExtra(Intent.EXTRA_TEXT, body)
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)))
    }

    /**
     * Method to test notifications
     */
    fun sendNotififcation(context: Context) {
        NotificationUtils.notifyUserOfWeatherUpdate(context)
    }


}