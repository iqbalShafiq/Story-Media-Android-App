package space.iqbalsyafiq.storymedia.ui.credential

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import space.iqbalsyafiq.storymedia.ApiServiceFake
import space.iqbalsyafiq.storymedia.MainCoroutineRule
import space.iqbalsyafiq.storymedia.getOrAwaitValue
import space.iqbalsyafiq.storymedia.model.request.RegisterRequest

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class CredentialViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CredentialViewModel

    @Mock
    private lateinit var application: Application

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Before
    fun setup() {
        val context = Mockito.mock(Context::class.java)
        `when`(application.applicationContext).thenReturn(context)
        viewModel = CredentialViewModel(application)
    }

    @Test
    fun `when Register User Then registerUserStatus will Turn True`() = runBlocking {
        val requestBody = RegisterRequest("Iqbal", "iqbal@gmail.com", "test123")
        val apiFake = ApiServiceFake()

        viewModel.registerUser(
            requestBody = requestBody,
            dispatcher = mainCoroutineRules.dispatcher,
            apiService = apiFake
        )

        println(viewModel.registerUserStatus.getOrAwaitValue())
        Assert.assertEquals(true, viewModel.registerUserStatus.getOrAwaitValue())
    }
}