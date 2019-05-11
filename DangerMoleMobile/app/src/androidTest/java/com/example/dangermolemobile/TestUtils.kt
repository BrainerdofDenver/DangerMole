package com.example.dangermolemobile

import android.view.View
import android.view.ViewGroup
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * The main function of this class is to test the Utility.
 * @see Utility
 */
class TestUtils {
    /**
     * The main function of this function is to see whether the child is at position.
     */
    fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            /**
             * The main function of this block is to get the description of the child,
             * to that of the parent.
             */
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            /**
             * The main function of this block is to see if the child and the parents position,
             * matches each other.
             */
            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}