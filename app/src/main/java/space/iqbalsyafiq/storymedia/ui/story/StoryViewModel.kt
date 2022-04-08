package space.iqbalsyafiq.storymedia.ui.story

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import space.iqbalsyafiq.storymedia.model.DataResponse
import space.iqbalsyafiq.storymedia.model.Story
import space.iqbalsyafiq.storymedia.repository.TokenPreferences
import space.iqbalsyafiq.storymedia.repository.api.ApiConfig

class StoryViewModel(application: Application) : AndroidViewModel(application) {
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

    private var _onCleared = MutableLiveData<Boolean>()
    val onCleared: LiveData<Boolean> = _onCleared

    private var _listStory = MutableLiveData<List<Story>>()
    val listStory: LiveData<List<Story>> = _listStory

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

    // get list story
    fun getListStory(token: String) {
        _loadingState.value = true

        service.getAllStories(
            "Bearer $token"
        ).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                _loadingState.value = false

                if (response.isSuccessful) {
                    Log.d(TAG, "getListStory onResponse: ${response.body()?.listStory}")
                    response.body()?.listStory?.let {
                        _listStory.value = it
                    }
                } else {
                    Toast.makeText(getApplication(), "Please try again", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                Log.d(TAG, "getListStory onFailure: ${t.message}")
                _loadingState.value = false
                Toast.makeText(getApplication(), "Please try again", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private val TAG = StoryViewModel::class.java.simpleName
    }
}