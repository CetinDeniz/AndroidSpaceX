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
import com.axuca.spacexfan.databinding.FragmentSignUpBinding
import com.axuca.spacexfan.model.User
import com.axuca.spacexfan.util.showSnackBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mAuth: FirebaseAuth
    @Inject
    lateinit var mFirebaseDatabase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginDirectText.setOnClickListener {
                findNavController().navigate(R.id.action_global_signInFragment)
            }

            registerButton.setOnClickListener {
                registerUser()
            }
        }

    }

    private fun FragmentSignUpBinding.registerUser() {
        val emailView = emailEditText
        val passwordView = passwordEditText
        val passwordAgainView = passwordAgainEditText

        val nameSurName = nameSurname.text.toString()
        val email = emailView.text.toString()
        val password = passwordView.text.toString()
        val passwordAgain = passwordAgainView.text.toString()

        if (TextUtils.isEmpty(nameSurName)) {
            nameSurnameLayout.error = getString(R.string.empty_name_surname)
            nameSurnameLayout.requestFocus()
        }

        if (TextUtils.isEmpty(email)) {
            emailView.error = getString(R.string.empty_email)
            emailView.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailView.error = getString(R.string.email_bad_pattern)
            emailView.requestFocus()
        }

        if (TextUtils.isEmpty(password)) {
            passwordView.error = getString(R.string.empty_password)
            passwordView.requestFocus()
        } else if (TextUtils.isEmpty(passwordAgain)) {
            passwordView.error = getString(R.string.empty_password)
            passwordView.requestFocus()
        } else if (password != passwordAgain) {
            passwordAgainView.error = getString(R.string.password_not_match)
            passwordAgainView.requestFocus()
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = User(email, listOf())

                    mFirebaseDatabase.child(mAuth.currentUser!!.uid)
                        .setValue(user)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                showSnackBar(this.root, getString(R.string.register_successful))
                                mAuth.signOut()
                                findNavController().navigate(R.id.action_global_signInFragment)
                            }
                        }
                } else {
                    showSnackBar(this.root, getString(R.string.register_error))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}