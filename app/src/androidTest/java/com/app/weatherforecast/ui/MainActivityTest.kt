package com.app.weatherforecast.ui


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.assertThat
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
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

    @Rule
    var rule = ActivityTestRule(MainActivity::class.java)
    private lateinit var mainActivity: MainActivity

    @Before
    fun setActivity() {
        mainActivity = rule.activity
        mainActivity.supportFragmentManager.beginTransaction()
    }

    @Test
    @Throws(Exception::class)
    fun ensureWeatherListIsPresent() {
        val viewById = mainActivity.findViewById<RecyclerView>(R.id.listResults)
        assertThat(viewById, notNullValue())
    }

    @Test
    @Throws(Exception::class)
    fun testWeatherListDisplayedCount() {
        val expectedCount = 5
        onView(withId(R.id.listResults)).check(CustomAssertions.hasItemCount(expectedCount))
    }


    @Test
    fun testClickOnItem_checkIfDetailsIsDisplayed() {
        val itemNum = 1
        onView(withId(R.id.listResults)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(itemNum, click()))
        val fragmentManager = mainActivity.supportFragmentManager
        Assert.assertNotNull(fragmentManager.findFragmentByTag(ForecastDetailsFragment.TAG))
    }


}