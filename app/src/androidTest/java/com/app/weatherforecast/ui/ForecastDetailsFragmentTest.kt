package com.app.weatherforecast.ui

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
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
        Espresso.onView(ViewMatchers.withId(R.id.detailsByTimeView)).check(CustomAssertions.hasItemCount(expectedCount))
    }
}