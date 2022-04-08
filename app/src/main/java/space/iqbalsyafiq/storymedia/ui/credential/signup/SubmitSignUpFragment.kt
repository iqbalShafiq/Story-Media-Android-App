package space.iqbalsyafiq.storymedia.ui.credential.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import space.iqbalsyafiq.storymedia.databinding.FragmentSubmitSignUpBinding
import space.iqbalsyafiq.storymedia.model.request.RegisterRequest
import space.iqbalsyafiq.storymedia.ui.credential.CredentialViewModel

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
                        /** @TODO
                         * check to API for duplicate validation
                         * intent to dashboard after registration success
                         */
                        viewModel.registerUser(
                            RegisterRequest(
                                etFullName.text.toString(),
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
                binding.progressLoading.visibility = if (it) View.VISIBLE else View.GONE
                binding.btnSignUp.visibility = if (it) View.GONE else View.VISIBLE
                binding.tvBack.visibility = if (it) View.GONE else View.VISIBLE
            }
        }

        viewModel.registerUserStatus.observe(viewLifecycleOwner) { status ->
            status?.let {
                if (it) {
                    Toast.makeText(
                        requireContext(),
                        "Registration Success!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Registration Gagal!",
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