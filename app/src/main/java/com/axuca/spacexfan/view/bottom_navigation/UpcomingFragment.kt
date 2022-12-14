package com.axuca.spacexfan.view.bottom_navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.axuca.spacexfan.adapter.UpcomingAdapter
import com.axuca.spacexfan.adapter.UpcomingClickListener
import com.axuca.spacexfan.databinding.FragmentUpcomingBinding
import com.axuca.spacexfan.view_model.UpcomingVM

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<UpcomingVM>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clickListener = UpcomingClickListener {
            findNavController().navigate(
                UpcomingFragmentDirections.actionUpcomingFragmentToUpcomingDetailFragment(
                    viewModel.getUpcomingLaunch(it)
                )
            )
        }

        binding.apply {
            this.viewModel = this@UpcomingFragment.viewModel
            lifecycleOwner = this@UpcomingFragment // or viewLifecycleOwner
            upcomingRecycler.adapter = UpcomingAdapter(clickListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}