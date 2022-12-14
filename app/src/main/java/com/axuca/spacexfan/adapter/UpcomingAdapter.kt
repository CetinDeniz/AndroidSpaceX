package com.axuca.spacexfan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.axuca.spacexfan.databinding.UpcomingListItemBinding
import com.axuca.spacexfan.model.UpcomingLaunch

class UpcomingAdapter(
    private val clickListener: UpcomingClickListener
) : ListAdapter<UpcomingLaunch, UpcomingAdapter.UpcomingViewHolder>(DiffUtilCallback) {

    inner class UpcomingViewHolder(
        private val binding: UpcomingListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(launch: UpcomingLaunch, clickListener: UpcomingClickListener) {
            with(binding) {
                this.launch = launch
                this.clickListener = clickListener
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        return UpcomingViewHolder(
            UpcomingListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        holder.bind(getItem(position),clickListener)
    }
}

private object DiffUtilCallback : DiffUtil.ItemCallback<UpcomingLaunch>() {
    override fun areItemsTheSame(
        oldItem: UpcomingLaunch,
        newItem: UpcomingLaunch
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UpcomingLaunch,
        newItem: UpcomingLaunch
    ): Boolean {
        return oldItem == newItem
    }
}

class UpcomingClickListener(val clickListener: (upcomingId: String) -> Unit) {
    fun onClick(upcomingId: String) = clickListener(upcomingId)
}