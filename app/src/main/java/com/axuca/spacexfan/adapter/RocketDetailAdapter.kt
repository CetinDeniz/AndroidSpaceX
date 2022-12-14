package com.axuca.spacexfan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.axuca.spacexfan.databinding.RocketDetailRecyclerListItemBinding

class RocketDetailAdapter :
    ListAdapter<String, RocketDetailAdapter.RocketDetailViewHolder>(RocketDetailDiffUtilCallback) {

    inner class RocketDetailViewHolder(
        private val binding: RocketDetailRecyclerListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            imageUrl: String
        ) {
            with(binding) {
                this.imageUrl = imageUrl
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketDetailViewHolder {
        return RocketDetailViewHolder(
            RocketDetailRecyclerListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RocketDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private object RocketDetailDiffUtilCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }
}