package com.app.weatherforecast.ui


import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import android.support.v4.app.FragmentManager

import android.support.v7.widget.RecyclerView
import com.app.weatherforecast.R
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule @JvmField
    var rule = IntentsTestRule(MainActivity::class.java)
    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentManager: FragmentManager

    @Before
    fun setActivity() {
        mainActivity = rule.activity
        mainActivity.supportFragmentManager.beginTransaction()
        fragmentManager = mainActivity.supportFragmentManager
    }

    /**
     * test if weather list presents
     */
    @Test
    @Throws(Exception::class)
    fun ensureWeatherListIsPresent() {
        val viewById = mainActivity.findViewById<RecyclerView>(R.id.listResults)
        assertThat(viewById, notNullValue())
    }

    /**
     * test items count, should be 5, not more
     */
    @Test
    @Throws(Exception::class)
    fun testWeatherListDisplayedCount() {
        val expectedCount = 5
        onView(withId(R.id.listResults)).check(CustomAssertions.hasItemCount(expectedCount))
    }

    /**
     * test weather list layout displayed correctly
     */
    @Test
    @Throws(Exception::class)
    fun testWeatherListItemLayoutDisplayedCorrectly() {
        onView(withId(R.id.listResults))
                .check(matches(hasDescendant(withId(R.id.forecast_item))))
        onView(withId(R.id.listResults))
                .check(matches(hasDescendant(withId(R.id.weather_icon))))
        onView(withId(R.id.listResults))
                .check(matches(hasDescendant(withId(R.id.weather_date))))
        onView(withId(R.id.listResults))
                .check(matches(hasDescendant(withId(R.id.description))))
        onView(withId(R.id.listResults))
                .check(matches(hasDescendant(withId(R.id.high))))
        onView(withId(R.id.listResults))
                .check(matches(hasDescendant(withId(R.id.low))))
    }

    /**
     * test click on the Today item, detail view displayed
     */
    @Test
    fun testClickOnTodayItem_checkIfDetailsIsDisplayed() {
        val itemNum = 0
        onView(withId(R.id.listResults)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(itemNum, click()))
        val fragmentManager = mainActivity.supportFragmentManager
        Assert.assertNotNull(fragmentManager.findFragmentByTag(ForecastDetailsFragment.TAG))
    }

    /**
     * test click on any item, detail view displayed
     */
    @Test
    fun testClickOnAnyItem_checkIfDetailsIsDisplayed() {
        val itemNum = 2
        onView(withId(R.id.listResults)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(itemNum, click()))
        Assert.assertNotNull(fragmentManager.findFragmentByTag(ForecastDetailsFragment.TAG))
    }

    /**
     * test click on settings menu item, settings view displayed
     */
    @Test
    fun testActionBarOverflow_checkIfSettingsIsDisplayed() {
        onView(withId(R.id.action_bar))
                .perform(click())
        // Click the settings item.
        onView(withId(R.id.action_settings))
                .perform(click())
        Assert.assertNotNull(fragmentManager.findFragmentByTag(SettingsFragment.TAG))
    }

    /**
     * test click on map menu item, chooser with apps to show map is displayed
     */
    @Test
    fun testActionBarOverflow_checkIfMapIsDisplayed() {
        onView(withId(R.id.action_bar)).perform(click())
        // Click the map menu item.
        onView(withId(R.id.action_map)).perform(click())
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_VIEW))
        Intents.assertNoUnverifiedIntents()
    }


}