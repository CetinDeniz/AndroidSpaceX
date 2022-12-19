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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RocketDetailFragment : Fragment() {
    private var _binding: FragmentRocketDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RocketDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<RocketDetailVM>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRocketDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setRocketInfo(args.rocketInfo)

        val favoriteClickListener = FavoriteClickListener { rocketInfo ->
            viewModel.favoriteClick(rocketInfo)
        }

        binding.apply {
            viewModel = this@RocketDetailFragment.viewModel
            lifecycleOwner = this@RocketDetailFragment
            this.favoriteClickListener = favoriteClickListener
            imagesRecycler.adapter = RocketDetailAdapter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}