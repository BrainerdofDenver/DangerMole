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
class NavBarTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(CameraActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.CAMERA",
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE")

    @Test
    fun navBarTest() {
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

        val recyclerView = onView(
                allOf(withId(R.id.design_navigation_view),
                        utils.childAtPosition(
                                allOf(withId(R.id.nav_view_camera),
                                        utils.childAtPosition(
                                                withId(R.id.drawer_layout),
                                                0)),
                                0),
                        isDisplayed()))
        recyclerView.check(matches(isDisplayed()))

        val checkedTextView = onView(
                allOf(withId(R.id.design_menu_item_text),
                        utils.childAtPosition(
                                utils.childAtPosition(
                                        withId(R.id.design_navigation_view),
                                        1),
                                0),
                        isDisplayed()))
        checkedTextView.check(matches(isDisplayed()))

        val checkedTextView2 = onView(
                allOf(withId(R.id.design_menu_item_text),
                        utils.childAtPosition(
                                utils.childAtPosition(
                                        withId(R.id.design_navigation_view),
                                        2),
                                0),
                        isDisplayed()))
        checkedTextView2.check(matches(isDisplayed()))

        val checkedTextView3 = onView(
                allOf(withId(R.id.design_menu_item_text),
                        utils.childAtPosition(
                                utils.childAtPosition(
                                        withId(R.id.design_navigation_view),
                                        3),
                                0),
                        isDisplayed()))
        checkedTextView3.check(matches(isDisplayed()))

        val checkedTextView4 = onView(
                allOf(withId(R.id.design_menu_item_text),
                        utils.childAtPosition(
                                utils.childAtPosition(
                                        withId(R.id.design_navigation_view),
                                        4),
                                0),
                        isDisplayed()))
        checkedTextView4.check(matches(isDisplayed()))

        val checkedTextView5 = onView(
                allOf(withId(R.id.design_menu_item_text),
                        utils.childAtPosition(
                                utils.childAtPosition(
                                        withId(R.id.design_navigation_view),
                                        7),
                                0),
                        isDisplayed()))
        checkedTextView5.check(matches(isDisplayed()))

        val checkedTextView6 = onView(
                allOf(withId(R.id.design_menu_item_text),
                        utils.childAtPosition(
                                utils.childAtPosition(
                                        withId(R.id.design_navigation_view),
                                        8),
                                0),
                        isDisplayed()))
        checkedTextView6.check(matches(isDisplayed()))
    }

}
