package space.iqbalsyafiq.storymedia.ui.credential.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import space.iqbalsyafiq.storymedia.databinding.FragmentLoginBinding
import space.iqbalsyafiq.storymedia.model.request.LoginRequest
import space.iqbalsyafiq.storymedia.ui.credential.CredentialViewModel

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

    }
}