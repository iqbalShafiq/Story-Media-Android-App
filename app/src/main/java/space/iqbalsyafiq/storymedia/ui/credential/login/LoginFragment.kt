package space.iqbalsyafiq.storymedia.ui.credential.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import space.iqbalsyafiq.storymedia.R
import space.iqbalsyafiq.storymedia.databinding.FragmentLoginBinding
import space.iqbalsyafiq.storymedia.model.request.LoginRequest
import space.iqbalsyafiq.storymedia.ui.credential.CredentialViewModel
import space.iqbalsyafiq.storymedia.ui.story.StoryActivity

class LoginFragment : Fragment() {

    private val viewModel: CredentialViewModel by activityViewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // on back pressed
            tvBack.setOnClickListener { requireActivity().onBackPressed() }

            // on sign in button clicked
            btnSignIn.setOnClickListener {
                // checking password
                if (!etPassword.isPasswordValid) {
                    etPassword.onPasswordInvalid()
                    return@setOnClickListener
                }

                // checking email and full name
                if (etEmail.isNotEmpty) {
                    if (etEmail.isEmailValid) {
                        viewModel.loginUser(
                            LoginRequest(
                                etEmail.text.toString(),
                                etPassword.text.toString()
                            )
                        )
                    } else etEmail.onEmailInvalid()
                } else etEmail.onFormEmpty()
            }
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                Log.d(TAG, "observeLiveData: $it")
                binding.progressLoading.visibility = if (it) View.VISIBLE else View.GONE
                binding.btnSignIn.visibility = if (it) View.GONE else View.VISIBLE
                binding.tvBack.visibility = if (it) View.GONE else View.VISIBLE
            }
        }

        viewModel.loginUserStatus.observe(viewLifecycleOwner) { status ->
            status?.let {
                if (it) {
                    // show toast
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.login_success),
                        Toast.LENGTH_SHORT
                    ).show()

                    // intent to dashboard
                    Intent(requireActivity(), StoryActivity::class.java).apply {
                        requireContext().startActivity(this)
                    }.also { requireActivity().finish() }
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.invalid_credentials),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        private val TAG = LoginFragment::class.java.simpleName
    }
}