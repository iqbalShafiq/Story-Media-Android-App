package space.iqbalsyafiq.storymedia.ui.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import space.iqbalsyafiq.storymedia.databinding.ActivityStoryBinding

class StoryActivity : AppCompatActivity() {

    private var _binding: ActivityStoryBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}