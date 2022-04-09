package space.iqbalsyafiq.storymedia.ui.story

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import space.iqbalsyafiq.storymedia.R
import space.iqbalsyafiq.storymedia.databinding.FragmentCreateStoryBinding
import space.iqbalsyafiq.storymedia.ui.story.CameraActivity.Companion.CAMERA_X_RESULT
import space.iqbalsyafiq.storymedia.ui.story.CameraActivity.Companion.PICTURE
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class CreateStoryFragment : Fragment() {

    private val viewModel: StoryViewModel by activityViewModels()
    private val args: CreateStoryFragmentArgs by navArgs()
    private var _binding: FragmentCreateStoryBinding? = null
    private val binding get() = _binding!!
    private var myFile: File? = null
    private var progressDrawable: CircularProgressDrawable? = null

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

            // detail read only mode
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

            // create mode
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

                ivStoryImage.setOnClickListener {
                    Intent(requireActivity(), CameraActivity::class.java).apply {
                        launcherIntentCameraX.launch(this)
                    }
                }

                btnUpload.setOnClickListener {
                    val description =
                        etDescription.text.toString().toRequestBody("text/plain".toMediaType())

                    val requestImageFile =
                        reduceFileImage(myFile!!).asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        myFile?.name,
                        requestImageFile
                    )

                    args.userLogin?.token?.let {
                        viewModel.uploadStory(it, imageMultipart, description)
                    }
                }
            }
        }

        progressDrawable = CircularProgressDrawable(requireContext()).apply {
            strokeWidth = 10f
            centerRadius = 50f
            start()
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                if (isLoading) {
                    binding.btnBack.visibility = View.GONE

                    binding.btnUpload.setImageDrawable(progressDrawable)
                    Log.d(TAG, "observeLiveData: Loading ...")

                } else {
                    binding.btnBack.visibility = View.VISIBLE
                    binding.btnUpload.setImageResource(R.drawable.ic_confirm)
                }
            }
        }

        viewModel.successState.observe(viewLifecycleOwner) { isSuccess ->
            isSuccess.getContentIfNotHandled()?.let {
                if (it) {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.success_upload_an_image),
                        Toast.LENGTH_SHORT
                    ).show()

                    requireActivity().onBackPressed()
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.error_when_uploading_image),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            myFile = it.data?.getSerializableExtra(PICTURE) as File

            Log.d(TAG, "launcherIntent: $myFile")

            // set view
            binding.ivStoryImage.visibility = View.VISIBLE
            lifecycleScope.launch {
                val result = BitmapFactory.decodeFile(myFile?.path)
                Glide.with(this@CreateStoryFragment)
                    .load(result)
                    .transform(FitCenter(), RoundedCorners(16))
                    .into(binding.ivStoryImage)
            }
            binding.llAddPhoto.visibility = View.GONE
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    companion object {
        private val TAG = CreateStoryFragment::class.java.simpleName
    }
}