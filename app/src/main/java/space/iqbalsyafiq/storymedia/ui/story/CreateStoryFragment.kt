package space.iqbalsyafiq.storymedia.ui.story

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.launch
import space.iqbalsyafiq.storymedia.databinding.FragmentCreateStoryBinding
import space.iqbalsyafiq.storymedia.ui.story.CameraActivity.Companion.CAMERA_X_RESULT
import space.iqbalsyafiq.storymedia.ui.story.CameraActivity.Companion.IS_USE_BACK
import space.iqbalsyafiq.storymedia.ui.story.CameraActivity.Companion.PICTURE
import java.io.File

class CreateStoryFragment : Fragment() {

    private val args: CreateStoryFragmentArgs by navArgs()
    private var _binding: FragmentCreateStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreateStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val story = args.storyDetail
        val user = args.userLogin

        Log.d(TAG, "onViewCreated: $story")
        Log.d(TAG, "onViewCreated: $user")

        with(binding) {
            btnBack.setOnClickListener { requireActivity().onBackPressed() }

            // if story not null
            story?.let {
                Glide.with(this@CreateStoryFragment)
                    .asBitmap()
                    .fitCenter()
                    .transform(RoundedCorners(16))
                    .load(story.photoUrl)
                    .into(ivStoryImage)
                etFullName.setText(story.name)
                etDescription.setText(story.description)
            }

            user?.let {
                etDescription.isEnabled = true

                // set visibility
                llAddPhoto.visibility = View.VISIBLE
                tvLabelFullName.visibility = View.GONE
                etFullName.visibility = View.GONE
                ivStoryImage.visibility = View.GONE

                // on llAddPhoto clicked
                llAddPhoto.setOnClickListener {
                    Intent(requireActivity(), CameraActivity::class.java).apply {
                        launcherIntentCameraX.launch(this)
                    }
                }
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra(PICTURE) as File
            val isBackCamera = it.data?.getBooleanExtra(IS_USE_BACK, true) as Boolean

            Log.d(TAG, "launcherIntent: $myFile")

            // set view
            binding.ivStoryImage.visibility = View.VISIBLE
            lifecycleScope.launch {
                val result = BitmapFactory.decodeFile(myFile.path)
                Glide.with(this@CreateStoryFragment)
                    .load(result)
                    .transform(FitCenter(), RoundedCorners(16))
                    .into(binding.ivStoryImage)
            }
            binding.llAddPhoto.visibility = View.GONE
        }
    }

    companion object {
        private val TAG = CreateStoryFragment::class.java.simpleName
    }
}