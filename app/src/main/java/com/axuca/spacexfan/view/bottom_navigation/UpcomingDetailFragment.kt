package com.axuca.spacexfan.view.bottom_navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.axuca.spacexfan.databinding.FragmentUpcomingDetailBinding

class UpcomingDetailFragment : Fragment() {
    private var _binding: FragmentUpcomingDetailBinding? = null
    private val binding get() = _binding!!

    private val args: UpcomingDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            launch = args.upcomingLaunch
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}