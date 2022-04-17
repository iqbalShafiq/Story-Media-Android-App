package space.iqbalsyafiq.storymedia.ui

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import space.iqbalsyafiq.storymedia.repository.TokenPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // init data store
    val loginTokenKey = stringPreferencesKey("login_token")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "login_token_setting"
    )
    private val pref = TokenPreferences.getInstance(application.dataStore)

    // get token
    @JvmName("getToken1")
    fun getToken(tokenPref: TokenPreferences = pref): LiveData<String> {
        return tokenPref.loadPreference(loginTokenKey).asLiveData()
    }
}