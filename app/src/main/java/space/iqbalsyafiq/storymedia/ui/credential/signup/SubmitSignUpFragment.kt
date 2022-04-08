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

                        Toast.makeText(
                            requireContext(),
                            "Registration Success!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else etEmail.onEmailInvalid()
                } else etEmail.onFormEmpty()
            }
        }
    }
}