package com.axuca.spacexfan.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axuca.spacexfan.model.Rocket
import com.axuca.spacexfan.model.RocketInfo
import com.axuca.spacexfan.retrofit.SpaceXApi
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RocketsVM @Inject constructor(
    private val mAuth: FirebaseAuth,
    private val googleAccount: GoogleSignInAccount?,
    private val databaseReference: DatabaseReference
) : ViewModel() {
    private var _rockets = MutableLiveData<List<RocketInfo>>()
    val rockets: LiveData<List<RocketInfo>>
        get() = _rockets

    private var firebaseResult: MutableMap<DataSnapshot?, Rocket> = mutableMapOf()


    private val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            /** Favorites in Firebase*/
            val rocketList: MutableList<Rocket> = ArrayList()
            for (ds in dataSnapshot.children) {
                val rocket: Rocket = ds.getValue(Rocket::class.java)!!
                rocketList.add(rocket)
            }
            Log.e("RocketsVM - Favorites", rocketList.size.toString())

            _rockets.value = _rockets.value?.map { apiRocket ->
                val rocket = apiRocket.copy(status = false)
                a@ for (favoriteRocket in rocketList) {
                    if (rocket.rocket.id == favoriteRocket.id) {
                        rocket.status = true
                        break@a
                    }
                }
                rocket
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("FavoritesVM", "loadPost:onCancelled", databaseError.toException())
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val apiResult = SpaceXApi.retrofitService.getAllRockets() // Rocket

            val favorites = databaseReference
                .child(getUserUID())
                .child("favorites")

            favorites.addValueEventListener(postListener)

            val firebaseResult = mutableListOf<String>()
            favorites.get().addOnSuccessListener {
                for (ds in it.children) {
                    val rocket: Rocket = ds.getValue(Rocket::class.java)!!
                    firebaseResult.add(rocket.id)

                    this@RocketsVM.firebaseResult[ds] = rocket
                }
            }.await()

            withContext(Dispatchers.Main) {
                _rockets.value = apiResult.map {
                    if (firebaseResult.contains(it.id)) RocketInfo(it, true)
                    else RocketInfo(it, false)
                }
            }
        }
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