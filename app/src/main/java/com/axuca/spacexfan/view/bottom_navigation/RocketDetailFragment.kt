package com.axuca.spacexfan.view.bottom_navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.axuca.spacexfan.adapter.FavoriteClickListener
import com.axuca.spacexfan.adapter.RocketDetailAdapter
import com.axuca.spacexfan.databinding.FragmentRocketDetailBinding
import com.axuca.spacexfan.view_model.RocketDetailVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RocketDetailFragment : Fragment() {
    private var _binding: FragmentRocketDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RocketDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<RocketDetailVM>()
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
        _binding = FragmentRocketDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setRocketInfo(args.rocketInfo)

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

            viewModel.changeRocketInfoState()
        }

        binding.apply {
            viewModel = this@RocketDetailFragment.viewModel
            lifecycleOwner = this@RocketDetailFragment // or viewLifecycleOwner
            this.favoriteClickListener = favoriteClickListener
            imagesRecycler.adapter = RocketDetailAdapter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}