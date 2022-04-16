package space.iqbalsyafiq.storymedia.ui.credential.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import space.iqbalsyafiq.storymedia.R
import space.iqbalsyafiq.storymedia.databinding.FragmentSubmitSignUpBinding
import space.iqbalsyafiq.storymedia.model.request.RegisterRequest
import space.iqbalsyafiq.storymedia.ui.credential.CredentialViewModel
import space.iqbalsyafiq.storymedia.ui.story.StoryActivity

class SubmitSignUpFragment : Fragment() {

    private val args: SubmitSignUpFragmentArgs by navArgs()
    private val viewModel: CredentialViewModel by activityViewModels()
    private var _binding: FragmentSubmitSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSubmitSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // set email text from navigation arguments
            etEmail.setText(args.email)

            // on sign up button clicked
            btnSignUp.setOnClickListener {
                // checking full name
                if (!etFullName.isNotEmpty) {
                    etFullName.onFormEmpty()
                    return@setOnClickListener
                }

                // checking password
                if (!etPassword.isPasswordValid) {
                    etPassword.onPasswordInvalid()
                    return@setOnClickListener
                }

                // checking email and full name
                if (etEmail.isNotEmpty) {
                    if (etEmail.isEmailValid) {
                        lifecycleScope.launch {
                            viewModel.registerUser(
                                RegisterRequest(
                                    etFullName.text.toString(),
                                    etEmail.text.toString(),
                                    etPassword.text.toString()
                                )
                            )
                        }
                    } else etEmail.onEmailInvalid()
                } else etEmail.onFormEmpty()
            }

            // on back pressed
            tvBack.setOnClickListener { requireActivity().onBackPressed() }
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                binding.progressLoading.visibility = if (it) View.VISIBLE else View.GONE
                binding.btnSignUp.visibility = if (it) View.GONE else View.VISIBLE
                binding.tvBack.visibility = if (it) View.GONE else View.VISIBLE
            }
        }

        viewModel.registerUserStatus.observe(viewLifecycleOwner) { status ->
            status?.let {
                if (it) {
                    // show toast
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.register_success),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.register_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
                        requireContext().getString(R.string.login_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.duplicateEmailStatus.observe(viewLifecycleOwner) { isDuplicate ->
            isDuplicate?.let {
                if (isDuplicate) binding.etEmail.onEmailDuplicate()
            }
        }
    }
}