package com.axuca.spacexfan.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.axuca.spacexfan.model.Rocket
import com.axuca.spacexfan.model.RocketInfo
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesVM @Inject constructor(
    private val mAuth: FirebaseAuth,
    private val googleAccount: GoogleSignInAccount?,
    private val databaseReference: DatabaseReference
) : ViewModel() {

    private var _rockets = MutableLiveData<List<RocketInfo>>()
    val rockets: LiveData<List<RocketInfo>>
        get() = _rockets

    private val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val rocketList: MutableList<Rocket> = ArrayList()
            for (ds in dataSnapshot.children) {
                val rocket: Rocket = ds.getValue(Rocket::class.java)!!
                rocketList.add(rocket)
            }

            _rockets.value = rocketList.map {
                RocketInfo(it, true)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("FavoritesVM", "loadPost:onCancelled", databaseError.toException())
        }
    }

    init {
        databaseReference
            .child(mAuth.currentUser!!.uid)
            .child("favorites")
            .addValueEventListener(postListener)
    }

    fun getRocketInfo(rocketId: String): RocketInfo {
        return _rockets.value?.find {
            it.rocket.id == rocketId
        }!!
    }

    fun deleteFromFavorites(rocket: Rocket) {
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

    private fun getUserUID(): String {
        return if (mAuth.currentUser?.uid == null) googleAccount!!.id!! else mAuth.currentUser!!.uid
    }

}