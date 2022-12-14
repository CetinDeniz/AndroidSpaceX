package com.axuca.spacexfan.view.bottom_navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.axuca.spacexfan.adapter.FavoriteClickListener
import com.axuca.spacexfan.adapter.RocketAdapter
import com.axuca.spacexfan.adapter.RocketClickListener
import com.axuca.spacexfan.databinding.FragmentRocketsBinding
import com.axuca.spacexfan.view_model.RocketsVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RocketsFragment : Fragment() {
    private var _binding: FragmentRocketsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RocketsVM>()

    @Inject
    lateinit var mAuth: FirebaseAuth

    //    @Inject
    private lateinit var mFirebaseDatabase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.show()

        mFirebaseDatabase = FirebaseDatabase
            .getInstance("https://spacex-fan-c5350-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users")
        _binding = FragmentRocketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clickListener = RocketClickListener {
            findNavController().navigate(
                RocketsFragmentDirections.actionRocketsFragmentToRocketDetailFragment(
                    viewModel.getRocketInfo(it)
                )
            )
        }

        val favoriteClickListener = FavoriteClickListener { rocketInfo ->

            if (rocketInfo.status) {
                // Remove
                viewModel.deleteFromFavorites(rocketInfo.rocket)
            } else {
                mFirebaseDatabase
                    .child(mAuth.currentUser!!.uid)
                    .child("favorites")
                    .push()
                    .setValue(rocketInfo.rocket)
            }

            // find rocketInfo in VM and change state
            // viewModel.changeRocketInfoStatus(rocketInfo)
        }

        binding.apply {
            this.viewModel = this@RocketsFragment.viewModel
            lifecycleOwner = this@RocketsFragment // or viewLifecycleOwner
            rocketsRecycler.adapter = RocketAdapter(clickListener, favoriteClickListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}