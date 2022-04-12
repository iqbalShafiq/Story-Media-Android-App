package space.iqbalsyafiq.storymedia.di

import android.content.Context
import space.iqbalsyafiq.storymedia.repository.StoryRepository
import space.iqbalsyafiq.storymedia.repository.api.ApiConfig
import space.iqbalsyafiq.storymedia.repository.db.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}