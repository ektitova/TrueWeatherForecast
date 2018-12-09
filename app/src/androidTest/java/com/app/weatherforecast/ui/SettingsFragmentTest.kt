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
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.onView
import android.content.Intent
import android.R.id.edit
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.v7.preference.PreferenceManager
import android.content.SharedPreferences
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.PreferenceMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.preference.Preference

import org.junit.Test
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matchers.*
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.matcher.ViewMatchers.withParent
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.DataInteraction
import android.support.test.espresso.matcher.PreferenceMatchers.*
import android.text.TextUtils
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


@LargeTest
@RunWith(AndroidJUnit4::class)
class SettingsFragmentTest {

    @Rule
    @JvmField
    var rule = ActivityTestRule(MainActivity::class.java)
    private lateinit var mainActivity: MainActivity
    private lateinit var preferencesEditor: SharedPreferences.Editor

    @Before
    fun setFragment() {
        mainActivity = rule.activity
        mainActivity.supportFragmentManager.beginTransaction()
        Espresso.onView(ViewMatchers.withId(R.id.action_bar))
                .perform(ViewActions.click())
        // Click the settings item.
        Espresso.onView(ViewMatchers.withId(R.id.action_settings))
                .perform(ViewActions.click())
        val targetContext = getInstrumentation().targetContext
        preferencesEditor = PreferenceManager.getDefaultSharedPreferences(targetContext).edit()
    }

    /**
     * test that all settings are displayed correctly
     */
    @Test
    fun checkSettingsDisplayedCorrectlyTest() {
        onView(withText(R.string.pref_location_label)).check(matches(isDisplayed()))
        onView(withText(R.string.pref_units_label)).check(matches(isDisplayed()))
        onView(withText(R.string.send_feedback)).check(matches(isDisplayed()))
        onView(withText(R.string.show_notification)).check(matches(isDisplayed()))
        onView(withText(R.string.send_notification)).check(matches(isDisplayed()))
    }

    /**
     * test that all Location settings changed correctly
     */
    @Test
    fun populateLocationFromSharedPrefsTest() {
        preferencesEditor.putString("Location", "Moscow,ru")
        preferencesEditor.commit()
        val res = getInstrumentation().targetContext.resources

        onData(allOf(`is`(instanceOf(Preference::class.java)),
                withKey(res.getString(R.string.pref_location_key)),
                withTitle(R.string.pref_location_label)))
                .inAdapterView(withParent(not(withResName("headers"))))
                .check(matches(isDisplayed()))
    }


    private fun withResName(resName: String): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("with res-name: $resName")
            }

            override fun matchesSafely(view: View): Boolean {
                val identifier = view.getResources().getIdentifier(resName, "id", "android")
                return !TextUtils.isEmpty(resName) && view.getId() === identifier
            }
        }
    }

    private fun onPreferenceRow(datamatcher: Matcher<out Any>): DataInteraction {

        val interaction = onData(datamatcher)

        return interaction.inAdapterView(allOf(withId(android.R.id.list), not(withParent(withResName("headers")))))
    }
}