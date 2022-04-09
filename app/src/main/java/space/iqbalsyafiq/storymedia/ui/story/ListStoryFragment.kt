package space.iqbalsyafiq.storymedia.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import space.iqbalsyafiq.storymedia.R
import space.iqbalsyafiq.storymedia.databinding.FragmentListStoryBinding
import space.iqbalsyafiq.storymedia.model.LoginResult
import space.iqbalsyafiq.storymedia.model.Story
import space.iqbalsyafiq.storymedia.ui.MainActivity

class ListStoryFragment : Fragment() {

    private val viewModel: StoryViewModel by activityViewModels()
    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListStoryAdapter
    private var userToken: String = ""
    private var userFullName: String = ""

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
            swipeRefresh.setOnRefreshListener {
                viewModel.getListStory(userToken)
                swipeRefresh.isRefreshing = false
            }

            btnLogout.setOnClickListener {
                viewModel.clearToken()
            }

            btnAdd.setOnClickListener {
                goToDetail(userLogin = LoginResult(userFullName, userToken, null))
            }
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                with(binding) {
                    progressLoading.visibility = if (it) View.VISIBLE else View.GONE
                    rvListStory.visibility = if (it) View.GONE else View.VISIBLE
                }
            }
        }

        viewModel.successState.observe(viewLifecycleOwner) { isLoading ->
            isLoading.getContentIfNotHandled()?.let {
                if (!it) {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.failed_fetching_api),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.onCleared.observe(viewLifecycleOwner) { isCleared ->
            isCleared?.let {
                Intent(requireActivity(), MainActivity::class.java).apply {
                    requireContext().startActivity(this)
                }.also { requireActivity().finish() }
            }
        }

        viewModel.getName().observe(viewLifecycleOwner) { userName ->
            userName?.let {
                userFullName = userName
                binding.tvUserName.text = requireContext().getString(
                    R.string.welcome_user,
                    userName
                )
            }
        }

        viewModel.getToken().observe(viewLifecycleOwner) { token ->
            token?.let {
                userToken = token
                viewModel.getListStory(userToken)
            }
        }

        viewModel.listStory.observe(viewLifecycleOwner) { stories ->
            stories?.let {
                if (stories.isNotEmpty()) {
                    adapter = ListStoryAdapter(this, it)
                    binding.rvListStory.adapter = adapter
                } else {
                    binding.tvEmptyList.visibility = View.VISIBLE
                    binding.rvListStory.visibility = View.GONE
                }
            }
        }
    }

    fun goToDetail(story: Story? = null, userLogin: LoginResult? = null) {
        val action = ListStoryFragmentDirections.navigateToCreateStory()
        action.storyDetail = story
        action.userLogin = userLogin
        Navigation.findNavController(binding.root).navigate(action)
    }
}