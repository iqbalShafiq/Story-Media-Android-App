package space.iqbalsyafiq.storymedia.ui.maps

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import space.iqbalsyafiq.storymedia.DataDummy
import space.iqbalsyafiq.storymedia.MainCoroutineRule
import space.iqbalsyafiq.storymedia.getOrAwaitValue
import space.iqbalsyafiq.storymedia.repository.db.StoryDao

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var dao: StoryDao

    @Mock
    private lateinit var application: Application

    private lateinit var viewModel: MapsViewModel

    @Before
    fun setup() {
        // prepare application context
        val context = mock(Context::class.java)
        `when`(application.applicationContext).thenReturn(context)

        // init view model
        viewModel = MapsViewModel(application)
    }

    @Test
    fun `when Get Story Should be Not Empty`() = mainCoroutineRules.runBlockingTest {
        // set expected list story
        val expectedStory = DataDummy.generateStoryDataDummy()

        // set dao mocking
        `when`(dao.getMapStories()).thenReturn(expectedStory)

        // call getMapStoryList
        viewModel.getMapStoryList(dao)

        // actual stories
        val actualStories = viewModel.storyList.getOrAwaitValue()

        // check actual stories
        println(actualStories)
        assertEquals(expectedStory, actualStories)
    }
}