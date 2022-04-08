package space.iqbalsyafiq.storymedia.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun loadPreference(key: Preferences.Key<String>): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[key] ?: ""
        }
    }

    suspend fun savePreference(token: String, key: Preferences.Key<String>) {
        dataStore.edit { preferences ->
            preferences[key] = token
        }
    }

    suspend fun clearPreference(key: Preferences.Key<String>) {
        dataStore.edit { preferences ->
            preferences[key] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TokenPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): TokenPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = TokenPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}