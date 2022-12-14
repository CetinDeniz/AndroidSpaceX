package com.axuca.spacexfan.view_model

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val mAuth: FirebaseAuth,
    private val googleAccount: GoogleSignInAccount?
) : ViewModel() {

    fun isUserSignedIn(): Boolean {
        return mAuth.currentUser != null || googleAccount != null
    }
}