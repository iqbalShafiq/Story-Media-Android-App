package space.iqbalsyafiq.storymedia.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import space.iqbalsyafiq.storymedia.getOrAwaitValue

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var viewModel: MainViewModel

    @Test
    fun `when Get Token Should Not Be Null`() {
        // set expected token
        val expectedToken = MutableLiveData<String>()
        expectedToken.value = "Bearer Token"
        `when`(viewModel.getToken()).thenReturn(expectedToken)

        // get actual token
        val actualToken = viewModel.getToken().getOrAwaitValue()

        // verify the method
        Mockito.verify(viewModel).getToken()

        // check actual token
        Assert.assertEquals("Bearer Token", actualToken)
    }

    @Test
    fun `when Get Token Should Be Empty`() {
        // set expected token
        val expectedToken = MutableLiveData<String>()
        expectedToken.value = ""
        `when`(viewModel.getToken()).thenReturn(expectedToken)

        // get actual token
        val actualToken = viewModel.getToken().getOrAwaitValue()

        // verify the method
        Mockito.verify(viewModel).getToken()

        // check actual token
        Assert.assertEquals("", actualToken)
    }
}