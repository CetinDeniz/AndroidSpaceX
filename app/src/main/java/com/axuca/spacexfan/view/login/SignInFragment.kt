package com.axuca.spacexfan.view.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.axuca.spacexfan.R
import com.axuca.spacexfan.databinding.FragmentSignInBinding
import com.axuca.spacexfan.model.User
import com.axuca.spacexfan.util.showSnackBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var startActivityForResult: ActivityResultLauncher<Intent>

    @Inject
    lateinit var mAuth: FirebaseAuth

    @Inject
    lateinit var googleSignInAccount: GoogleSignInAccount

    @Inject
    lateinit var mFirebaseDatabase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        startActivityForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                googleSignInActivityResult(it)
            }

        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginCard.setOnClickListener {
                signIn()
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

    /** For Sign-in with Email-Password */
    private fun signIn() {
        binding.apply {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (TextUtils.isEmpty(email)) {
                emailInputLayout.error = "Email cannot be empty"
            } else if (TextUtils.isEmpty(password)) {
                passwordInputLayout.error = "Password cannot be empty"
            } else {
                signInWithEmailAndPassword(email, password)
            }
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    showSnackBar(binding.root, getString(R.string.signin_accepted))
                    findNavController().navigate(R.id.action_global_rocketsFragment)
                } else {
                    showSnackBar(binding.root, getString(R.string.signin_denied))
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

    private fun googleSignInActivityResult(activityResult: ActivityResult) {
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val googleAccount = GoogleSignIn
                .getSignedInAccountFromIntent(activityResult.data)
                .getResult(ApiException::class.java)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}