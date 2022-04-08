package space.iqbalsyafiq.storymedia.ui.credential

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import space.iqbalsyafiq.storymedia.databinding.ActivityCredentialBinding

class CredentialActivity : AppCompatActivity() {

    private var _binding: ActivityCredentialBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCredentialBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}