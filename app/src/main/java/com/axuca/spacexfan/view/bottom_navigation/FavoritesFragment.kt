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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FavoritesVM>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            viewModel.favoriteClick(rocketInfo)
        }

        binding.apply {
            viewModel = this@FavoritesFragment.viewModel
            lifecycleOwner = this@FavoritesFragment
            favoritesRecycler.adapter = RocketAdapter(clickListener, favoriteClickListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}