package space.iqbalsyafiq.storymedia.ui.credential.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import space.iqbalsyafiq.storymedia.databinding.FragmentInputEmailBinding

class InputEmailFragment : Fragment() {

    private var _binding: FragmentInputEmailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInputEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // Join button validation
            btnJoinNow.setOnClickListener {
                if (etEmail.isNotEmpty) {
                    if (etEmail.isEmailValid) {
                        val action = InputEmailFragmentDirections.navigateToSubmitSignUp(
                            etEmail.text.toString()
                        )
                        Navigation.findNavController(binding.root).navigate(action)
                    } else etEmail.onEmailInvalid()
                } else etEmail.onFormEmpty()
            }
        }
    }
}