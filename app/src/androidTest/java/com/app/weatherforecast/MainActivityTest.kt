package com.app.weatherforecast

import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.app.weatherforecast.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HelloWorldEspressoTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun listGoesOverTheFold() {
       // onView(allOf(withText("7"), hasSibling(withText("item: 0"))))
          //      .perform(click())
    //   onView(withText("Hello world!")).check(matches(isDisplayed()))
    }
}