package com.example.dangermolemobile

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.assertion.ViewAssertions.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UnitTests {

    @Test
    fun camButtonShouldReturnIntent(){
        val uri = Uri.parse("data")
        var resultData = Intent()
        resultData.data = uri
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    //Found from this site, which helped with espresso imports https://cate.blog/2016/04/28/testing-intents-on-android-like-stabbing-yourself-in-the-eye-with-a-blunt-implement/
    @Test
    fun testLaunchActivity() {
        onView(withId(R.id.take_pic_button)).check(matches(withText("SNAP")))
        onView(withId(R.id.probabilityView)).check(matches(withText("Probability:  ___%")))
        onView(withId(R.id.accuracyView)).check(matches(withText("Probability:  ___%")))
    }
}
