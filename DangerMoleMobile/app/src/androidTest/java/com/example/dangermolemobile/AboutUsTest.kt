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
class AboutUsTest {

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
    fun aboutUsTest() {
        val appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        val navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view_camera),
                                        0)),
                        7),
                        isDisplayed()))
        navigationMenuItemView.perform(click())

        val textView = onView(
                allOf(withId(R.id.aboutUs), withText("Danger Mole is an app conceived and developed by MSU Denver students as a solution to early detection of melanoma in nevi (moles). Danger Mole is designed to run on android and makes use of TensorFlow’s powerful machine learning libraries. The model itself is trained on data acquired from the International Skin Imaging Collaboration (ISIC) database linked below. Our team is composed of four undergraduates in pursuit of a BS in computer science. The project is currently in development as of spring 2019 and is intended as a prototype and not a definitive medical resource."),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                                        1),
                                0),
                        isDisplayed()))
        textView.check(matches(withText("Danger Mole is an app conceived and developed by MSU Denver students as a solution to early detection of melanoma in nevi (moles). Danger Mole is designed to run on android and makes use of TensorFlow’s powerful machine learning libraries. The model itself is trained on data acquired from the International Skin Imaging Collaboration (ISIC) database linked below. Our team is composed of four undergraduates in pursuit of a BS in computer science. The project is currently in development as of spring 2019 and is intended as a prototype and not a definitive medical resource.")))

        val textView2 = onView(
                allOf(withId(R.id.isicLink), withText(" Isic website"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                                        1),
                                1),
                        isDisplayed()))
        textView2.check(matches(withText(" Isic website")))

        val textView3 = onView(
                allOf(withId(R.id.gitLink), withText(" Our Github"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                                        1),
                                2),
                        isDisplayed()))
        textView3.check(matches(withText(" Our Github")))
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
