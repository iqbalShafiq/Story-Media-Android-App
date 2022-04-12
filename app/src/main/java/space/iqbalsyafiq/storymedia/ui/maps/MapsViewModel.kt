package space.iqbalsyafiq.storymedia.ui.maps

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import space.iqbalsyafiq.storymedia.model.Story
import space.iqbalsyafiq.storymedia.repository.db.StoryDatabase

class MapsViewModel(application: Application) : AndroidViewModel(application) {
    // init dao
    private val storyDao = StoryDatabase(getApplication()).storyDao()

    // prepare live data
    private var _storyList = MutableLiveData<List<Story>>()
    val storyList: LiveData<List<Story>> = _storyList

    // get map story list
    fun getMapStoryList() {
        viewModelScope.launch {
            _storyList.value = storyDao.getMapStories()
        }
    }
}