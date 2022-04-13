package space.iqbalsyafiq.storymedia.ui.story

import android.content.Intent
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
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.gms.maps.model.LatLng
import space.iqbalsyafiq.storymedia.R
import space.iqbalsyafiq.storymedia.databinding.FragmentCreateStoryBinding
import space.iqbalsyafiq.storymedia.ui.maps.MapsActivity
import space.iqbalsyafiq.storymedia.ui.story.CameraActivity.Companion.CAMERA_X_RESULT
import space.iqbalsyafiq.storymedia.ui.story.CameraActivity.Companion.PICTURE
import java.io.File

class CreateStoryFragment : Fragment() {

    private val viewModel: StoryViewModel by activityViewModels()
    private val args: CreateStoryFragmentArgs by navArgs()
    private var _binding: FragmentCreateStoryBinding? = null
    private val binding get() = _binding!!
    private var myFile: File? = null
    private var progressDrawable: CircularProgressDrawable? = null
    private var hasPhoto: Boolean = false
    private var storyLocation: LatLng? = null

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
                btnUpload.visibility = View.GONE

                // set location visibility
                if (it.lat != null && it.lon != null) {
                    etLocation.setText(
                        requireContext().getString(
                            R.string.lat_lon,
                            it.lat.toString(),
                            it.lon.toString()
                        )
                    )

                    ivSetLocation.setOnClickListener { _ ->
                        Intent(requireActivity(), MapsActivity::class.java).apply {
                            putExtra(
                                MapsActivity.LOCATION_DATA,
                                LatLng(it.lat, it.lon)
                            )
                            startActivity(this)
                        }
                    }
                } else {
                    tvLabelLocation.visibility = View.GONE
                    etLocation.visibility = View.GONE
                }
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

                // on set location clicked
                ivSetLocation.setOnClickListener {
                    Intent(requireActivity(), MapsActivity::class.java).apply {
                        putExtra(
                            MapsActivity.INTENT_DATA,
                            "get location"
                        )
                        launcherMapsActivity.launch(this)
                    }
                }

                ivStoryImage.setOnClickListener {
                    Intent(requireActivity(), CameraActivity::class.java).apply {
                        launcherIntentCameraX.launch(this)
                    }
                }

                btnUpload.setOnClickListener {
                    // check description and photo
                    if (etDescription.isNotEmpty && hasPhoto && etLocation.isNotEmpty) {
                        args.userLogin?.token?.let {
                            viewModel.uploadStory(
                                it,
                                myFile!!,
                                etDescription.text.toString(),
                                storyLocation?.latitude.toString(),
                                storyLocation?.longitude.toString()
                            )
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            requireContext().getString(R.string.empty_photo_and_desc),
                            Toast.LENGTH_SHORT
                        ).show()
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
            hasPhoto = true

            // set view
            binding.ivStoryImage.visibility = View.VISIBLE
            val result = BitmapFactory.decodeFile(myFile?.path)
            Glide.with(this@CreateStoryFragment)
                .load(result)
                .transform(FitCenter(), RoundedCorners(16))
                .into(binding.ivStoryImage)
            binding.llAddPhoto.visibility = View.GONE

        }
    }

    private val launcherMapsActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == MapsActivity.resultCode) {
            storyLocation = it.data?.getParcelableExtra(MapsActivity.SET_LOCATION_DATA)
            binding.etLocation.setText(
                requireContext().getString(
                    R.string.lat_lon,
                    storyLocation?.latitude.toString(),
                    storyLocation?.longitude.toString()
                )
            )
        }
    }

    companion object {
        private val TAG = CreateStoryFragment::class.java.simpleName
    }
}