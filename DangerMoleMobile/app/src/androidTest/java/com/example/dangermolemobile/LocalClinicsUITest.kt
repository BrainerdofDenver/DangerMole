package com.example.dangermolemobile


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LocalClinicsUITest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(CameraActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.CAMERA",
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE")

    @Test
            /**
             * The main function of this block is to test the Local Clinics Activity
             * @see LocalClinicsActivity
             */
    fun localClinicsUITest() {
        val utils = TestUtils()
        val appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        utils.childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        utils.childAtPosition(
                                                withClassName(`is`("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        val navigationMenuItemView = onView(
                allOf(utils.childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                utils.childAtPosition(
                                        withId(R.id.nav_view_camera),
                                        0)),
                        4),
                        isDisplayed()))
        navigationMenuItemView.perform(click())

        val frameLayout = onView(
                allOf(withId(R.id.map),
                        utils.childAtPosition(
                                allOf(withId(R.id.main_layout),
                                        utils.childAtPosition(
                                                withId(R.id.app_bar_layout),
                                                1)),
                                0),
                        isDisplayed()))
        frameLayout.check(matches(isDisplayed()))
    }

}
