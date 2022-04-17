package space.iqbalsyafiq.storymedia.ui.story

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import space.iqbalsyafiq.storymedia.JsonConverter
import space.iqbalsyafiq.storymedia.R
import space.iqbalsyafiq.storymedia.repository.api.ApiConstant
import space.iqbalsyafiq.storymedia.utils.EspressoIdlingResource

@RunWith(AndroidJUnit4::class)
@MediumTest
class ListStoryFragmentTest {
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConstant.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun getHeadlineNews_Success() {
        launchFragmentInContainer<ListStoryFragment>(null, R.style.Theme_StoryMedia)
        onView(withId(R.id.rvListStory))
            .check(matches(isDisplayed()))

        //check data is match
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rvListStory))
            .check(matches(isDisplayed()))
        onView(withText("Ardana"))
            .check(matches(isDisplayed()))
    }
}