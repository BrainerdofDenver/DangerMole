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
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CameraActivityTest {

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
    fun cameraActivityTest2() {
        val utils = TestUtils()
        val appCompatButton = onView(
                allOf(withId(R.id.take_pic_button), withText("Snap"),
                        utils.childAtPosition(
                                utils.childAtPosition(
                                        withClassName(`is`("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                0),
                        isDisplayed()))
        appCompatButton.perform(click())

        val view = onView(
                allOf(withId(com.android.camera2.R.id.progress_overlay),
                        utils.childAtPosition(
                                utils.childAtPosition(
                                        withId(com.android.camera2.R.id.module_layout),
                                        0),
                                0),
                        isDisplayed()))
        view.check(matches(isDisplayed()))

        val imageView = onView(
                allOf(withId(R.id.camView), withContentDescription("ImageView container that holds photos taken by Camera, and loaded from stored photos"),
                        utils.childAtPosition(
                                utils.childAtPosition(
                                        IsInstanceOf.instanceOf(ViewGroup::class.java),
                                        1),
                                0),
                        isDisplayed()))
        imageView.check(matches(isDisplayed()))
    }

}
