package com.app.weatherforecast.ui

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.app.weatherforecast.R
import org.hamcrest.CoreMatchers

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ForecastDetailsFragmentTest {

    @Rule
    @JvmField
    var rule = ActivityTestRule(MainActivity::class.java)
    private lateinit var mainActivity: MainActivity

    @Before
    fun setFragment() {
        mainActivity = rule.activity
        mainActivity.supportFragmentManager.beginTransaction()
        //open the detailed view, not today
        val itemNum = 1
        Espresso.onView(ViewMatchers.withId(R.id.listResults)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(itemNum, ViewActions.click()))
    }


    /**
     * test if forecast by time is displayed
     */
    @Test
    @Throws(Exception::class)
    fun ensureWeatherForecastByTimeListIsPresent() {
        val viewById = mainActivity.findViewById<RecyclerView>(R.id.detailsByTimeView)
        ViewMatchers.assertThat(viewById, CoreMatchers.notNullValue())
    }

    /**
     * test if forecast by time items count, should be 8 if the day is not today
     */
    @Test
    @Throws(Exception::class)
    fun testWeatherListDisplayedCount() {
        val expectedCount = 8
        onView(ViewMatchers.withId(R.id.detailsByTimeView)).check(CustomAssertions.hasItemCount(expectedCount))
    }

    /**
     * test forecast details layout is displayed correctly
     */
    @Test
    @Throws(Exception::class)
    fun testForecastDetails_checkForecastDetailsLayoutDisplayedCorrectly() {
        onView(ViewMatchers.withId(R.id.details)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.details))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.weather_icon))))
        onView(ViewMatchers.withId(R.id.details))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.weather_date))))
        onView(ViewMatchers.withId(R.id.details))
                .check(matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.high))))
        onView(ViewMatchers.withId(R.id.details))
                .check(matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.low))))
    }

    /**
     * test weather list item by time layout displayed correctly
     */
    @Test
    @Throws(Exception::class)
    fun testWeatherListByTime_checkIfItemLayoutDisplayedCorrectly() {
        onView(ViewMatchers.withId(R.id.detailsByTimeView))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.forecast_item))))
        onView(ViewMatchers.withId(R.id.detailsByTimeView))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.weather_icon))))
        onView(ViewMatchers.withId(R.id.detailsByTimeView))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.weather_time))))
        onView(ViewMatchers.withId(R.id.detailsByTimeView))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.description))))
        onView(ViewMatchers.withId(R.id.detailsByTimeView))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.temperature))))
    }
}