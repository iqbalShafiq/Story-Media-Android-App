package space.iqbalsyafiq.storymedia.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import space.iqbalsyafiq.storymedia.di.Injection
import space.iqbalsyafiq.storymedia.ui.story.StoryViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(
                Injection.provideRepository(context),
                context.applicationContext as Application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}