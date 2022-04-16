package space.iqbalsyafiq.storymedia.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import space.iqbalsyafiq.storymedia.MainCoroutineRule
import space.iqbalsyafiq.storymedia.StoryDataDummy
import space.iqbalsyafiq.storymedia.getOrAwaitValue
import space.iqbalsyafiq.storymedia.model.Story

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var viewModel: MapsViewModel

    @Test
    fun `when Get Story Should be Not Empty`() = mainCoroutineRules.runBlockingTest {
        val expectedStory = MutableLiveData<List<Story>>()
        expectedStory.value = StoryDataDummy.generateStoryDataDummy()
        `when`(viewModel.getMapStoryList()).thenReturn(expectedStory)

        // actual stories
        val actualStories = viewModel.getMapStoryList().getOrAwaitValue()

        // verify mocking method
        Mockito.verify(viewModel).getMapStoryList()

        // check actual stories
        assertEquals(expectedStory.value, actualStories)
    }
}