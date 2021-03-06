package space.iqbalsyafiq.storymedia.ui.story

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import space.iqbalsyafiq.storymedia.model.DataResponse
import space.iqbalsyafiq.storymedia.model.Story
import space.iqbalsyafiq.storymedia.repository.StoryRepository
import space.iqbalsyafiq.storymedia.repository.TokenPreferences
import space.iqbalsyafiq.storymedia.repository.api.ApiConfig
import space.iqbalsyafiq.storymedia.repository.db.StoryDatabase
import space.iqbalsyafiq.storymedia.utils.Event
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

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

    // init db
    private val db = StoryDatabase(getApplication())

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
        lng: String = ""
    ) {
        _loadingState.value = true

        // have to use coroutine because the reduceFileImage can block UI Thread
        viewModelScope.launch(Dispatchers.IO) {
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
            val requestImageFile = reduceFileImage(file).asRequestBody(
                "image/jpeg".toMediaTypeOrNull()
            )
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            service.uploadStory(
                "Bearer $token",
                imageMultipart,
                descriptionRequest,
                latRequest,
                lngRequest
            ).enqueue(object : Callback<DataResponse> {
                override fun onResponse(
                    call: Call<DataResponse>,
                    response: Response<DataResponse>
                ) {
                    _loadingState.value = false
                    _successState.value = Event(response.isSuccessful)
                }

                override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                    Log.d(TAG, "uploadStory onFailure: ${t.message}")
                    _loadingState.value = false
                    _successState.value = Event(false)
                }

            })
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    companion object {
        private val TAG = StoryViewModel::class.java.simpleName
    }
}