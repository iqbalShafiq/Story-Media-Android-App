package space.iqbalsyafiq.storymedia.ui.credential

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import space.iqbalsyafiq.storymedia.model.request.LoginRequest
import space.iqbalsyafiq.storymedia.model.request.RegisterRequest
import space.iqbalsyafiq.storymedia.repository.TokenPreferences
import space.iqbalsyafiq.storymedia.repository.api.ApiConfig
import space.iqbalsyafiq.storymedia.repository.api.ApiService
import java.net.UnknownHostException

class CredentialViewModel(application: Application) : AndroidViewModel(application) {

    // init data store
    private val loginTokenKey = stringPreferencesKey("login_token")
    private val nameKey = stringPreferencesKey("name")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "login_token_setting"
    )
    private val pref = TokenPreferences.getInstance(application.dataStore)

    // init api
    private var service = ApiConfig.getApiService()

    // live data
    private var _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    private var _registerUserStatus = MutableLiveData<Boolean>()
    val registerUserStatus: LiveData<Boolean> = _registerUserStatus

    private var _duplicateEmailStatus = MutableLiveData<Boolean>()
    val duplicateEmailStatus: LiveData<Boolean> = _duplicateEmailStatus

    private var _loginUserStatus = MutableLiveData<Boolean>()
    val loginUserStatus: LiveData<Boolean> = _loginUserStatus

    suspend fun registerUser(
        requestBody: RegisterRequest,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiService: ApiService = service
    ) {
        _loadingState.postValue(true)
        println("Hai")

        try {
            val response = withContext(dispatcher) {
                apiService.registerUser(requestBody)
            }

            when (response.error as Boolean) {
                true -> {
                    _loadingState.postValue(false)
                    _registerUserStatus.postValue(false)

                }
                false -> {
                    _registerUserStatus.postValue(true)
                    loginUser(
                        LoginRequest(requestBody.email, requestBody.password),
                        dispatcher,
                        apiService
                    )
                }
            }
        } catch (e: UnknownHostException) {
            _loadingState.postValue(false)
            _registerUserStatus.postValue(false)
        } catch (e: Exception) {
            _loadingState.postValue(false)
            _duplicateEmailStatus.postValue(true)
        }

    }

    suspend fun loginUser(
        requestBody: LoginRequest,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiService: ApiService = service
    ) {
        _loadingState.value = true

        try {
            val response = withContext(dispatcher) {
                apiService.loginUser(requestBody)
            }

            when (response.error as Boolean) {
                true -> {
                    _loadingState.value = false
                    _loginUserStatus.value = false
                }
                false -> {
                    _loadingState.value = false
                    _loginUserStatus.value = true

                    viewModelScope.launch(dispatcher) {
                        pref.savePreference(
                            response.loginResult?.token ?: "",
                            loginTokenKey
                        )
                        pref.savePreference(
                            response.loginResult?.name ?: "",
                            nameKey
                        )
                    }
                }
            }
        } catch (e: UnknownHostException) {
            _loadingState.value = false
            _loginUserStatus.value = false
        } catch (e: java.lang.Exception) {
            _loadingState.value = false
            _loginUserStatus.value = false
        }
    }

    companion object {
        private val TAG = CredentialActivity::class.java.simpleName
    }
}