package space.iqbalsyafiq.storymedia.ui.story

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import space.iqbalsyafiq.storymedia.model.Story
import space.iqbalsyafiq.storymedia.repository.StoryRepository
import space.iqbalsyafiq.storymedia.repository.TokenPreferences
import space.iqbalsyafiq.storymedia.repository.api.ApiConfig
import space.iqbalsyafiq.storymedia.repository.api.ApiService
import space.iqbalsyafiq.storymedia.utils.Event
import space.iqbalsyafiq.storymedia.utils.Helper
import java.io.File
import java.net.UnknownHostException

class StoryViewModel(
    private val storyRepository: StoryRepository,
    application: Application
) : AndroidViewModel(application) {
    // init data store
    private val loginTokenKey = stringPreferencesKey("login_token")
    private val nameKey = stringPreferencesKey("name")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "login_token_setting"
    )
    private val pref = TokenPreferences.getInstance(application.dataStore)

    // init api
    private val service = ApiConfig.getApiService()

    // live data
    private var _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private var _successState = MutableLiveData<Event<Boolean>>()
    val successState: LiveData<Event<Boolean>> = _successState

    private var _onCleared = MutableLiveData<Boolean>()
    val onCleared: LiveData<Boolean> = _onCleared

    // get token
    @JvmName("getToken1")
    fun getToken(): LiveData<String> {
        return pref.loadPreference(loginTokenKey).asLiveData()
    }

    // get name
    fun getName(): LiveData<String> {
        return pref.loadPreference(nameKey).asLiveData()
    }

    // get name
    fun clearToken() {
        viewModelScope.launch {
            pref.clearPreference(loginTokenKey)
            pref.clearPreference(nameKey)
            _onCleared.value = true
        }
    }

    // get list of story
    fun getListStory(token: String): LiveData<PagingData<Story>> = storyRepository.getStory(
        token
    ).cachedIn(viewModelScope)

    // upload story
    fun uploadStory(
        token: String,
        file: File,
        description: String,
        lat: String = "",
        lng: String = "",
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiService: ApiService = service,
        helper: Helper = Helper
    ) {
        _loadingState.value = true

        // have to use coroutine because the reduceFileImage can block UI Thread
        viewModelScope.launch(dispatcher) {
            // prepare the view model parameter
            val descriptionRequest = description.toRequestBody(
                "text/plain".toMediaType()
            )
            val latRequest = lat.toRequestBody(
                "text/plain".toMediaType()
            )
            val lngRequest = lng.toRequestBody(
                "text/plain".toMediaType()
            )
            val requestImageFile = helper.reduceFileImage(file).asRequestBody(
                "image/jpeg".toMediaTypeOrNull()
            )
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            try {
                val response = withContext(dispatcher) {
                    apiService.uploadStory(
                        "Bearer $token",
                        imageMultipart,
                        descriptionRequest,
                        latRequest,
                        lngRequest
                    )
                }

                when (response.error as Boolean) {
                    false -> {
                        _loadingState.postValue(false)
                        _successState.postValue(Event(true))
                    }

                    true -> {
                        _loadingState.postValue(false)
                        _successState.postValue(Event(false))
                    }
                }
            } catch (e: UnknownHostException) {
                _loadingState.postValue(false)
                _successState.postValue(Event(false))
            } catch (e: Exception) {
                _loadingState.postValue(false)
                _successState.postValue(Event(false))
            }
        }
    }
}