package space.iqbalsyafiq.storymedia.model

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import space.iqbalsyafiq.storymedia.repository.api.ApiService
import space.iqbalsyafiq.storymedia.repository.db.StoryDatabase

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val apiService: ApiService,
    private val database: StoryDatabase,
    private val tokenApi: String
) : RemoteMediator<Int, Story>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        val page = INITIAL_PAGE_INDEX
        return try {
            Log.d(TAG, "load: $tokenApi")
            val responseData = apiService.getAllStories(
                "Bearer $tokenApi",
                page,
                state.config.pageSize
            )
            val storyList = responseData.listStory as MutableList<Story>

            Log.d(TAG, "load: finish got api")

            val endOfPaginationReached = storyList.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteStories()
                }
                database.storyDao().insertStory(storyList)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private companion object {
        private val TAG = StoryRemoteMediator::class.java.simpleName
        const val INITIAL_PAGE_INDEX = 1
    }
}