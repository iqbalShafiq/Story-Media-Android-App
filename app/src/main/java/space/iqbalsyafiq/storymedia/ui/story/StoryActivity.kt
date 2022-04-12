package space.iqbalsyafiq.storymedia.ui.story

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import space.iqbalsyafiq.storymedia.databinding.ActivityStoryBinding
import space.iqbalsyafiq.storymedia.ui.ViewModelFactory

class StoryActivity : AppCompatActivity() {

    private val viewModel: StoryViewModel by viewModels {
        ViewModelFactory(this)
    }
    private var _binding: ActivityStoryBinding? = null
    private val binding get() = _binding
    private var tokenUser: String = ""
    private var nameUser: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.getToken().observe(this) { token ->
            token?.let {
                tokenUser = token
            }
        }

        viewModel.getName().observe(this) { name ->
            name?.let {
                nameUser = name
            }
        }
    }
}