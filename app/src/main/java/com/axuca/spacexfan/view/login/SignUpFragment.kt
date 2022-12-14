package com.axuca.spacexfan.view.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.axuca.spacexfan.R
import com.axuca.spacexfan.databinding.FragmentSignUpBinding
import com.axuca.spacexfan.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    @Inject
    lateinit var mAuth: FirebaseAuth

    //    @Inject
    private lateinit var mFirebaseDatabase: DatabaseReference

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFirebaseDatabase = FirebaseDatabase
            .getInstance("https://spacex-fan-c5350-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users")
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
                registerUser(it)
            }
        }

    }

    private fun FragmentSignUpBinding.registerUser(view: View) {
        val emailView = emailEditText
        val passwordView = passwordEditText
        val passwordAgainView = passwordAgainEditText

        val nameSurName = nameSurname.text.toString()
        val email = emailView.text.toString()
        val password = passwordView.text.toString()
        val passwordAgain = passwordAgainView.text.toString()

        if (TextUtils.isEmpty(nameSurName)) {
            nameSurnameLayout.error = "Name & Surname cannot be empty"
            nameSurnameLayout.requestFocus()
        }

        if (TextUtils.isEmpty(email)) {
            emailView.error = "Email cannot be empty"
            emailView.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailView.error = "Email does not match pattern."
            emailView.requestFocus()
        }

        if (TextUtils.isEmpty(password)) {
            passwordView.error = "Password cannot be empty"
            passwordView.requestFocus()
        } else if (TextUtils.isEmpty(passwordAgain)) {
            passwordView.error = "Password cannot be empty"
            passwordView.requestFocus()
        } else if (password != passwordAgain) {
            passwordAgainView.error = "Passwords are not the same"
            passwordAgainView.requestFocus()
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = User(email, listOf())

                    mFirebaseDatabase.child(mAuth.currentUser!!.uid)
                        .setValue(user)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Snackbar.make(
                                    view,
                                    "User registered successfully",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                mAuth.signOut()
                                findNavController().navigate(R.id.action_global_signInFragment)
                            } else {
                                Snackbar.make(
                                    view,
                                    "Registration error ${it.exception?.message ?: "Null exception"}",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Snackbar.make(
                        view,
                        "Registration error ${it.exception?.message ?: "Null exception"}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}