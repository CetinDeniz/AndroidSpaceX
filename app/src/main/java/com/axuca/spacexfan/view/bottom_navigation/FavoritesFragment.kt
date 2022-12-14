package com.axuca.spacexfan.view.bottom_navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.axuca.spacexfan.adapter.FavoriteClickListener
import com.axuca.spacexfan.adapter.RocketAdapter
import com.axuca.spacexfan.adapter.RocketClickListener
import com.axuca.spacexfan.databinding.FragmentFavoritesBinding
import com.axuca.spacexfan.view_model.FavoritesVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FavoritesVM>()
    private lateinit var mFirebaseDatabase: DatabaseReference

    @Inject
    lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFirebaseDatabase = FirebaseDatabase
            .getInstance("https://spacex-fan-c5350-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users")
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clickListener = RocketClickListener {
            findNavController().navigate(
                FavoritesFragmentDirections.actionFavoritesFragmentToRocketDetailFragment(
                    viewModel.getRocketInfo(it)
                )
            )
        }

        val favoriteClickListener = FavoriteClickListener { rocketInfo ->
            // add this rocket to users firebase db
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
        }

        binding.apply {
            viewModel = this@FavoritesFragment.viewModel
            lifecycleOwner = this@FavoritesFragment // or viewLifecycleOwner
            favoritesRecycler.adapter = RocketAdapter(clickListener, favoriteClickListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}