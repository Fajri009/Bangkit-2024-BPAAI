package com.example.bangkit_2024_bpaai.AdvancedTesting.ui.list

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bangkit_2024_bpaai.R
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.remote.retrofit.ApiConfig
import com.example.bangkit_2024_bpaai.AdvancedTesting.util.EspressoIdlingResource
import com.example.bangkit_2024_bpaai.AdvancedTesting.utils.JsonConverter
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class NewsFragmentTest {
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun getHeadlineNews_Success() {
        val bundle = Bundle()
        bundle.putString(NewsFragment.ARG_TAB, NewsFragment.TAB_NEWS)
        launchFragmentInContainer<NewsFragment>(bundle, R.style.Theme_Bangkit2024BPAAI)

        val mockResponse = MockResponse()
            .setResponseCode(200) // 200 untuk sukses
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse) // untuk mengeksekusi request dengan response yang sudah dibuat menggunakan MockResponse

        onView(withText("Inti Bumi Mendingin Lebih Cepat, Pertanda Apa? - detikInet"))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_news))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Perjalanan Luar Angkasa Sebabkan Anemia - CNN Indonesia"))
                )
            )

        //check data is match
    }

    //check if failed load data
    @Test
    fun getHeadlineNews_Error() {
        val bundle = Bundle()
        bundle.putString(NewsFragment.ARG_TAB, NewsFragment.TAB_NEWS)
        launchFragmentInContainer<NewsFragment>(bundle, R.style.Theme_Bangkit2024BPAAI)
        val mockResponse = MockResponse()
            .setResponseCode(500) // 500 untuk error
        mockWebServer.enqueue(mockResponse) // untuk mengeksekusi request dengan response yang sudah dibuat menggunakan MockResponse
        onView(withId(R.id.tv_error))
            .check(matches(isDisplayed()))
        onView(withText("Oops.. something went wrong."))
            .check(matches(isDisplayed()))
    }
}