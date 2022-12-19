package com.axuca.spacexfan.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.axuca.spacexfan.model.Rocket
import com.axuca.spacexfan.model.RocketInfo
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RocketDetailVM @Inject constructor(
    private val mAuth: FirebaseAuth,
    private val googleAccount: GoogleSignInAccount?,
    private val databaseReference: DatabaseReference
) : ViewModel() {

    private var _rocketInfo = MutableLiveData<RocketInfo>()
    val rocketInfo: LiveData<RocketInfo>
        get() = _rocketInfo

    fun favoriteClick(rocketInfo: RocketInfo){
        if (rocketInfo.status) {
            deleteFromFavorites(rocketInfo.rocket)
        } else {
            addToFavorites(rocketInfo.rocket)
        }
        changeRocketInfoState()
    }

    private fun deleteFromFavorites(rocket: Rocket) {
        databaseReference
            .child(getUserUID())
            .child("favorites")
            .get()
            .addOnSuccessListener {
                for (ds in it.children) { // Each favorite rocket
                    val eachRocket: Rocket = ds.getValue(Rocket::class.java)!!

                    if (eachRocket == rocket) {
                        ds.ref.removeValue()
                        break
                    }
                }
            }
    }

    private fun addToFavorites(rocket: Rocket) {
        databaseReference
            .child(mAuth.currentUser!!.uid)
            .child("favorites")
            .push()
            .setValue(rocket)
    }

    private fun getUserUID(): String {
        return if (mAuth.currentUser?.uid == null) googleAccount!!.id!! else mAuth.currentUser!!.uid
    }

    fun setRocketInfo(rocketInfo: RocketInfo) {
        _rocketInfo.value = rocketInfo
    }

    private fun changeRocketInfoState() {
        _rocketInfo.value!!.let {
            val r = it.copy(status = !it.status)
            _rocketInfo.value = r
        }
    }

}