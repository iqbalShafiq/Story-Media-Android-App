package space.iqbalsyafiq.storymedia.ui.maps

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import space.iqbalsyafiq.storymedia.model.Story
import space.iqbalsyafiq.storymedia.repository.db.StoryDatabase

class MapsViewModel(application: Application) : AndroidViewModel(application) {
    // init dao
    private val storyDao = StoryDatabase(getApplication()).storyDao()

    // prepare live data
    private var _storyList = MutableLiveData<List<Story>>()
    val storyList: LiveData<List<Story>> = _storyList

    // get map story list
    suspend fun getMapStoryList(): LiveData<List<Story>> {
        val stories = viewModelScope.async {
            storyDao.getMapStories()
        }

        _storyList.value = stories.await()

        return storyList
    }
}