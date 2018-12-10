package com.app.weatherforecast.ui

import android.app.Activity
import android.app.Instrumentation
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.app.weatherforecast.R
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.onView
import android.content.Context
import android.content.Intent
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.v7.preference.PreferenceManager
import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.matcher.IntentMatchers.*
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.preference.Preference

import org.junit.Test
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matchers.*
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue


@LargeTest
@RunWith(AndroidJUnit4::class)
class SettingsFragmentTest {



    @Rule
    @JvmField
    var rule = IntentsTestRule(MainActivity::class.java)
  //  var rule = ActivityTestRule(MainActivity::class.java)
    private lateinit var mainActivity: MainActivity
    private lateinit var preferencesEditor: SharedPreferences.Editor
    private lateinit var targetContext: Context
    private lateinit var uiDevice:UiDevice

    @Before
    fun setFragment() {
        mainActivity = rule.activity
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        mainActivity.supportFragmentManager.beginTransaction()
        Espresso.onView(ViewMatchers.withId(R.id.action_bar))
                .perform(ViewActions.click())
        // Click the settings item.
        Espresso.onView(ViewMatchers.withId(R.id.action_settings))
                .perform(ViewActions.click())
        targetContext = getInstrumentation().targetContext
        preferencesEditor = PreferenceManager.getDefaultSharedPreferences(targetContext).edit()
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }


    @Test
    fun checkSettingsDisplayedCorrectlyTest() {
        onView(withText(R.string.pref_location_label)).check(matches(isDisplayed()))
        onView(withText(R.string.pref_units_label)).check(matches(isDisplayed()))
        onView(withText(R.string.send_feedback)).check(matches(isDisplayed()))
        onView(withText(R.string.show_notification)).check(matches(isDisplayed()))
        onView(withText(R.string.send_notification)).check(matches(isDisplayed()))
    }

    @Test
    fun testLocationPreferences_checkPopulateLocationFromSharedPrefs() {
        val res = targetContext.resources
        preferencesEditor.putString(res.getString(R.string.pref_location_key), "Moscow,ru")
        preferencesEditor.commit()

        onView(withText("Moscow,ru"))
                .check(matches(isDisplayed()))

//        onData(allOf(`is`(instanceOf(Preference::class.java)),
//                withKey(res.getString(R.string.pref_location_key)),
//                withTitle(R.string.pref_location_label)))
//                .inAdapterView(allOf(withId(android.R.id.list)))
//                .check(matches(isDisplayed()))
    }

    @Test
    fun testUnitsPreferences_checkPopulateUnitsFromSharedPrefs() {
        val res = targetContext.resources
        preferencesEditor.putString(res.getString(R.string.pref_units_key), res.getString(R.string.pref_units_metric))
        preferencesEditor.commit()

        onView(withText(res.getString(R.string.pref_units_metric)))
                .check(matches(isDisplayed()))
//        onData(allOf(`is`(instanceOf(Preference::class.java)),
//                withKey(res.getString(R.string.pref_units_key)),
//                withSummary(R.string.pref_units_metric)))
//                .inAdapterView(allOf(withId(android.R.id.list)))
//                .check(matches(isDisplayed()))
    }

    @Test
    fun testNotificationsPreferences_checkPopulateIsNotificationsEnabledFromSharedPrefs() {
        val res = targetContext.resources
        preferencesEditor.putBoolean(res.getString(R.string.pref_is_notifications_enabled_key), true)
        preferencesEditor.commit()

        onView(withText(res.getString(R.string.pref_show_notification_summary)))
                .check(matches(isDisplayed()))
//        onData(allOf(`is`(instanceOf(Preference::class.java)),
//                withKey(res.getString(R.string.pref_is_notifications_enabled_key)),
//                withSummary(R.string.pref_show_notification_summary)))
//                .inAdapterView(allOf(withId(android.R.id.list)))
//                .check(matches(isDisplayed()))
    }


    @Test
    fun testSendNotificationPreferences_shouldContainCorrectTitleAndText() {
        val res = targetContext.resources
        val expectedAppName = mainActivity.getString(R.string.app_name)
        val expectedTitle = mainActivity.getString(R.string.today)
        val expectedText = "Forecast:"

        //click on send notification
        onView(withText(res.getString(R.string.send_notification))).perform(click())

        uiDevice.openNotification()
        uiDevice.wait(Until.hasObject(By.textStartsWith(expectedAppName)), 1000)
        val title: UiObject2 = uiDevice.findObject(By.text(expectedTitle))
        val text: UiObject2 = uiDevice.findObject(By.textStartsWith(expectedText))
        assertEquals(expectedTitle, title.text)
        assertTrue(text.text.startsWith(expectedText))
    }

    @Test
    fun testSendFeedBackPreferences_checkIfIntentIsCorrect() {
        val res = targetContext.resources
        onView(withText(res.getString(R.string.send_feedback))).perform(click())
        intended(hasExtra(`is`(Intent.EXTRA_INTENT),
                allOf( hasAction(Intent.ACTION_SEND),
                        hasType("message/rfc822"),
                        hasExtra(Intent.EXTRA_SUBJECT, "Query from android app") )))
        Intents.assertNoUnverifiedIntents()
    }

}