package space.iqbalsyafiq.storymedia.ui

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import space.iqbalsyafiq.storymedia.MainCoroutineRule
import space.iqbalsyafiq.storymedia.getOrAwaitValue
import space.iqbalsyafiq.storymedia.repository.TokenPreferences

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var pref: TokenPreferences

    @Mock
    private lateinit var application: Application

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        val context = mock(Context::class.java)
        `when`(application.applicationContext).thenReturn(context)
        viewModel = MainViewModel(application)

        // set expected token
        val tokenFlow = flowOf("Bearer Token")
        `when`(pref.loadPreference(stringPreferencesKey("login_token"))).thenReturn(tokenFlow)
    }

    @Test
    fun `when Get Token Should Not Be Empty`() = mainCoroutineRules.runBlockingTest {
        // get actual token
        val actualToken = viewModel.getToken(pref)

        // check actual token
        println(actualToken.getOrAwaitValue())
        Assert.assertEquals("Bearer Token", actualToken.getOrAwaitValue())
    }
}