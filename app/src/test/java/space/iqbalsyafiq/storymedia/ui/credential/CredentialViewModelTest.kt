package space.iqbalsyafiq.storymedia.ui.credential

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
import space.iqbalsyafiq.storymedia.model.request.LoginRequest
import space.iqbalsyafiq.storymedia.model.request.RegisterRequest

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class CredentialViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CredentialViewModel
    private lateinit var apiFake: ApiServiceFake

    @Mock
    private lateinit var application: Application

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Before
    fun setup() {
        // set application context
        val context = Mockito.mock(Context::class.java)
        `when`(application.applicationContext).thenReturn(context)

        // init view model and api fake
        viewModel = CredentialViewModel(application)
        apiFake = ApiServiceFake()
    }

    @Test
    fun `when Register User Then registerUserStatus will Turn True`() =
        mainCoroutineRules.runBlockingTest {
            val requestBody = RegisterRequest("Iqbal", "iqbal@gmail.com", "test123")

            viewModel.registerUser(
                requestBody = requestBody,
                dispatcher = mainCoroutineRules.dispatcher,
                apiService = apiFake
            )

            println(viewModel.registerUserStatus.getOrAwaitValue())
            Assert.assertEquals(true, viewModel.registerUserStatus.getOrAwaitValue())
        }

    @Test
    fun `when Login User Then loginUserStatus will Turn True`() =
        mainCoroutineRules.runBlockingTest {
            val requestBody = LoginRequest("iqbal@gmail.com", "test123")

            viewModel.loginUser(
                requestBody = requestBody,
                dispatcher = mainCoroutineRules.dispatcher,
                apiService = apiFake
            )

            println(viewModel.loginUserStatus.getOrAwaitValue())
            Assert.assertEquals(true, viewModel.loginUserStatus.getOrAwaitValue())
        }
}