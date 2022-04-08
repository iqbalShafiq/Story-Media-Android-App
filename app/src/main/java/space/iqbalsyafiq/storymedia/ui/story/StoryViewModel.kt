package space.iqbalsyafiq.storymedia.ui.story

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import space.iqbalsyafiq.storymedia.repository.TokenPreferences

class StoryViewModel(application: Application) : AndroidViewModel(application) {
    // init data store
    private val loginTokenKey = stringPreferencesKey("login_token")
    private val nameKey = stringPreferencesKey("name")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "login_token_setting"
    )
    private val pref = TokenPreferences.getInstance(application.dataStore)

    // live data
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
}