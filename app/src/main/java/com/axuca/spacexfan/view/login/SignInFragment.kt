package com.axuca.spacexfan.view.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.axuca.spacexfan.R
import com.axuca.spacexfan.databinding.FragmentSignInBinding
import com.axuca.spacexfan.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var startActivityForResult: ActivityResultLauncher<Intent>

    @Inject
    lateinit var mAuth: FirebaseAuth

    //    @Inject
    lateinit var mFirebaseDatabase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()

        mFirebaseDatabase = FirebaseDatabase
            .getInstance("https://spacex-fan-c5350-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users")

        startActivityForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    val googleAccount = task.getResult(ApiException::class.java)

                    mFirebaseDatabase.orderByKey().equalTo(googleAccount.id!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (!snapshot.exists()) {
                                    googleAccount?.let { acc ->
                                        val user = User(acc.email ?: "Null email", listOf())
                                        mFirebaseDatabase.child(acc.id!!)
                                            .setValue(user)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    findNavController().navigate(R.id.action_global_rocketsFragment)
                                                }
                                            }
                                    }
                                } else {
                                    findNavController().navigate(R.id.action_global_rocketsFragment)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
            }

        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginCard.setOnClickListener {
                login()
            }

            googleSignCard.setOnClickListener {
                signInWithGoogle()
            }

            forgotPassword.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment())
            }

            signUp.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }
        }
    }


    private fun login() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (TextUtils.isEmpty(email)) {
            binding.email.error = "Email cannot be empty"
        } else if (TextUtils.isEmpty(password)) {
            binding.password.error = "Password cannot be empty"
        } else {
            signInWithEmailAndPassword(email, password)
        }
    }

    /** For Sign-in with Email-Password */
    private fun signInWithEmailAndPassword(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    Snackbar.make(
                        binding.root,
                        "User logged in successfully",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_global_rocketsFragment)
                } else {
                    Snackbar.make(
                        binding.root,
                        "Your password or e-mail address is wrong.", // Log in error ${it.exception?.message ?: "Null exception"}
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
    }

    /** For Google Sign-in */
    private fun signInWithGoogle() {
        startActivityForResult.launch(getSignInIntent())
    }

    private fun getSignInIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        return mGoogleSignInClient.signInIntent
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}