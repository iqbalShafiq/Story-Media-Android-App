package space.iqbalsyafiq.storymedia.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import space.iqbalsyafiq.storymedia.databinding.ActivityMainBinding
import space.iqbalsyafiq.storymedia.ui.credential.CredentialActivity
import space.iqbalsyafiq.storymedia.ui.story.StoryActivity

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        runBlocking {
            lifecycleScope.launch {
                delay(2000)
                observeLiveData()
            }
        }
    }

    private fun observeLiveData() {
        viewModel.getToken().observe(this) { token ->
            Log.d("TAG", "observeLiveData: $token")
            token?.let {
                if (token.isNotEmpty()) {
                    // go to dashboard
                    Intent(this@MainActivity, StoryActivity::class.java).apply {
                        startActivity(this)
                    }.also { finish() }
                } else {
                    // go to credential
                    Intent(this@MainActivity, CredentialActivity::class.java).apply {
                        startActivity(this)
                    }.also { finish() }
                }
            }
        }
    }
}