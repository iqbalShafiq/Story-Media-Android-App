package space.iqbalsyafiq.storymedia.ui.story

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import space.iqbalsyafiq.storymedia.ApiServiceFake
import space.iqbalsyafiq.storymedia.DataDummy
import space.iqbalsyafiq.storymedia.MainCoroutineRule
import space.iqbalsyafiq.storymedia.adapter.ListStoryAdapter
import space.iqbalsyafiq.storymedia.getOrAwaitValue
import space.iqbalsyafiq.storymedia.model.Story
import space.iqbalsyafiq.storymedia.repository.StoryRepository
import space.iqbalsyafiq.storymedia.repository.TokenPreferences
import space.iqbalsyafiq.storymedia.utils.Event
import space.iqbalsyafiq.storymedia.utils.Helper
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var storyViewModel: StoryViewModel

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var helper: Helper

    @Mock
    private lateinit var tokenPreferences: TokenPreferences

    private lateinit var viewModel: StoryViewModel

    @Before
    fun setup() {
        // mocking application context
        val context = Mockito.mock(Context::class.java)
        `when`(application.applicationContext).thenReturn(context)

        // init viewModel
        viewModel = StoryViewModel(storyRepository, application)

        // mock the reduce file image helper
        `when`(helper.reduceFileImage(File("asd"))).thenReturn(File("asd"))

        // mock tokenPreference
        val tokenFlow = flowOf("Bearer Token")
        val nameFlow = flowOf("User Full Name")
        val tokenKeyString = stringPreferencesKey("login_token")
        val nameKeyString = stringPreferencesKey("name")

        `when`(tokenPreferences.loadPreference(tokenKeyString)).thenReturn(tokenFlow)
        `when`(tokenPreferences.loadPreference(nameKeyString)).thenReturn(nameFlow)
    }

    @Test
    fun `when Get Token then Should Not Empty`() = mainCoroutineRules.runBlockingTest {
        val token = viewModel.getToken(tokenPreferences).getOrAwaitValue()

        // assert token
        println(token)
        assertEquals("Bearer Token", token)
    }

    @Test
    fun `when Get Name then Should Not Empty`() = mainCoroutineRules.runBlockingTest {
        val name = viewModel.getName(tokenPreferences).getOrAwaitValue()

        // assert name
        println(name)
        assertEquals("User Full Name", name)
    }

    @Test
    fun `when Clear Token then Should Turn Empty Token and Name`() =
        mainCoroutineRules.runBlockingTest {
            // mocking clear preference
            var token = "Bearer Token"
            var name = "User Full Name"
            val tokenKeyString = stringPreferencesKey("login_token")
            val nameKeyString = stringPreferencesKey("name")

            `when`(tokenPreferences.clearPreference(tokenKeyString)).then {
                token = ""
                println("Token has cleared")
            }

            `when`(tokenPreferences.clearPreference(nameKeyString)).then {
                name = ""
                println("Name has cleared")
            }

            // call clear method
            viewModel.clearToken(tokenPreferences)

            // assert token
            assertEquals("", token)
            assertEquals("", name)
        }

    @Test
    fun `when Create Story Should Not Error`() = mainCoroutineRules.runBlockingTest {
        // set api fake
        val apiServiceFake = ApiServiceFake()

        // call upload story
        viewModel.uploadStory(
            "Bearer Token",
            File("asd"),
            "halo",
            "-7.123",
            "121.1111",
            mainCoroutineRules.dispatcher,
            apiServiceFake,
            helper
        )

        // assert success state
        val expectedValue = Event(true).getContentIfNotHandled()
        val actualValue = viewModel.successState.getOrAwaitValue().getContentIfNotHandled()
        println(actualValue)

        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `when Get Story Should Not Null`() = mainCoroutineRules.runBlockingTest {
        val dummyStory = DataDummy.generateStoryDataDummy()
        val data = PagedTestDataSources.snapshot(dummyStory)
        val story = MutableLiveData<PagingData<Story>>()
        story.value = data

        `when`(storyViewModel.getListStory("Bearer Token")).thenReturn(story)
        val actualStory = storyViewModel.getListStory("Bearer Token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )
        differ.submitData(actualStory)

        advanceUntilIdle()

        Mockito.verify(storyViewModel).getListStory("Bearer Token")
        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
    }

    class PagedTestDataSources private constructor(private val items: List<Story>) :
        PagingSource<Int, LiveData<List<Story>>>() {
        companion object {
            fun snapshot(items: List<Story>): PagingData<Story> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}