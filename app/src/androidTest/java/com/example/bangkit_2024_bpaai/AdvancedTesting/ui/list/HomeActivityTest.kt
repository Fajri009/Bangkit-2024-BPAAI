package com.example.bangkit_2024_bpaai.AdvancedTesting.ui.list

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.bangkit_2024_bpaai.AdvancedTesting.ui.detail.NewsDetailActivity
import com.example.bangkit_2024_bpaai.AdvancedTesting.util.EspressoIdlingResource
import com.example.bangkit_2024_bpaai.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeActivityTest{

    @get:Rule
    val activity = ActivityScenarioRule(HomeActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loadHeadlineNews_Success() {
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_news)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        // scrollToPosition : Menggulirkan list ke posisi tertentu.
    }

    @Test
    fun loadDetailNews_Success() {
        Intents.init()
        onView(withId(R.id.rv_news)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        intended(hasComponent(NewsDetailActivity::class.java.name)) // sama seperti startActivity
        onView(withId(R.id.webView)).check(matches(isDisplayed()))
    }

    @Test
    fun loadBookmarkedNews_Success() {
        onView(withId(R.id.rv_news)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        // actionOnItemAtPosition : Memberikan aksi pada item list tertentu.
        onView(withId(R.id.action_bookmark)).perform(click())
        pressBack()
        onView(withText("BOOKMARK")).perform(click())
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_news)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.webView)).check(matches(isDisplayed()))
        onView(withId(R.id.action_bookmark)).perform(click())
        pressBack()
    }
}