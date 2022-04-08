package space.iqbalsyafiq.storymedia.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import space.iqbalsyafiq.storymedia.R
import space.iqbalsyafiq.storymedia.databinding.FragmentListStoryBinding
import space.iqbalsyafiq.storymedia.ui.MainActivity

class ListStoryFragment : Fragment() {

    private val viewModel: StoryViewModel by activityViewModels()
    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnLogout.setOnClickListener {
                viewModel.clearToken()
            }
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.onCleared.observe(viewLifecycleOwner) { isCleared ->
            isCleared?.let {
                Intent(requireActivity(), MainActivity::class.java).apply {
                    requireContext().startActivity(this)
                }.also { requireActivity().finish() }
            }
        }

        viewModel.getName().observe(viewLifecycleOwner) { userName ->
            userName?.let {
                binding.tvUserName.text = requireContext().getString(
                    R.string.wellcome_user,
                    userName
                )
            }
        }
    }
}