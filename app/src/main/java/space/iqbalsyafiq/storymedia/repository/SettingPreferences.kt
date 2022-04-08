package space.iqbalsyafiq.storymedia.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val loginToken = stringPreferencesKey("login_token_setting")

    fun getLoginToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[loginToken] ?: ""
        }
    }

    suspend fun saveThemeSetting(token: String) {
        dataStore.edit { preferences ->
            preferences[loginToken] = token
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}