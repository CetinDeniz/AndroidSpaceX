package com.axuca.spacexfan.view.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.axuca.spacexfan.R
import com.axuca.spacexfan.databinding.FragmentForgotPasswordBinding
import com.axuca.spacexfan.util.showSnackBar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            reset.setOnClickListener {
                val email = resetPasswordEditText.text.toString().trim()

                if (TextUtils.isEmpty(email)) {
                    emailInputLayout.error = getString(R.string.empty_email)
                    emailInputLayout.requestFocus()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInputLayout.error = getString(R.string.email_bad_pattern)
                    emailInputLayout.requestFocus()
                } else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                        if (it.isSuccessful) {
                            findNavController().navigate(R.id.action_global_signInFragment)
                            showSnackBar(view, getString(R.string.check_email))
                        } else {
                            showSnackBar(view, getString(R.string.forgot_password_error))
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}