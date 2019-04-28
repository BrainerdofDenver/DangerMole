package com.example.dangermolemobile

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.robolectric.RobolectricTestRunner

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.example.dangermolemobile", appContext.packageName)
    }
}
