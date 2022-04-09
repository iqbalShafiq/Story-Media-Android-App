package space.iqbalsyafiq.storymedia.ui.story

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import space.iqbalsyafiq.storymedia.databinding.FragmentCreateStoryBinding

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
                etFullName.setText(user.name)
                etDescription.isEnabled = true
            }
        }
    }

    companion object {
        private val TAG = CreateStoryFragment::class.java.simpleName
    }
}