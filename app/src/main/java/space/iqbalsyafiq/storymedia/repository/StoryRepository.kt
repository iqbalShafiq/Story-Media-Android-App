package space.iqbalsyafiq.storymedia.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import space.iqbalsyafiq.storymedia.model.Story
import space.iqbalsyafiq.storymedia.model.StoryRemoteMediator
import space.iqbalsyafiq.storymedia.repository.api.ApiService
import space.iqbalsyafiq.storymedia.repository.db.StoryDatabase

class StoryRepository(
    private val database: StoryDatabase,
    private val apiService: ApiService
) {
    fun getStory(apiKey: String): LiveData<PagingData<Story>> {
        Log.d(TAG, "getStory: $apiKey")
        Log.d(TAG, "getStory: ${database.storyDao().getStories()}")
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(apiService, database, apiKey),
            pagingSourceFactory = {
                database.storyDao().getStories()
            }
        ).liveData
    }

    companion object {
        private val TAG = StoryRepository::class.java.simpleName
    }
}