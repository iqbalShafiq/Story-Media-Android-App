package space.iqbalsyafiq.storymedia.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import space.iqbalsyafiq.storymedia.databinding.ActivityMainBinding
import space.iqbalsyafiq.storymedia.ui.credential.CredentialActivity

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        runBlocking {
            lifecycleScope.launch {
                delay(3000)
                Intent(this@MainActivity, CredentialActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }
}