package com.axuca.spacexfan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.axuca.spacexfan.databinding.RocketListItemBinding
import com.axuca.spacexfan.model.RocketInfo

class RocketAdapter(
    private val clickListener: RocketClickListener,
    private val favoriteClickListener: FavoriteClickListener
) : ListAdapter<RocketInfo, RocketAdapter.RocketViewHolder>(RocketDiffUtilCallback) {

    inner class RocketViewHolder(
        private val binding: RocketListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            rocketInfo: RocketInfo,
            clickListener: RocketClickListener,
            favoriteClickListener: FavoriteClickListener
        ) {
            with(binding) {
                this.rocketInfo = rocketInfo
                this.clickListener = clickListener
                this.favoriteClickListener = favoriteClickListener
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RocketViewHolder {
        return RocketViewHolder(
            RocketListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RocketAdapter.RocketViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, favoriteClickListener)
    }
}

private object RocketDiffUtilCallback : DiffUtil.ItemCallback<RocketInfo>() {
    override fun areItemsTheSame(
        oldItem: RocketInfo,
        newItem: RocketInfo
    ): Boolean {
        return oldItem.rocket.id == newItem.rocket.id
    }

    override fun areContentsTheSame(
        oldItem: RocketInfo,
        newItem: RocketInfo
    ): Boolean {
        return oldItem == newItem
    }
}

class RocketClickListener(val clickListener: (rocketId: String) -> Unit) {
    fun onClick(rocketId: String) = clickListener(rocketId)
}

class FavoriteClickListener(val clickListener: (rocket: RocketInfo) -> Unit) {
    fun onClick(rocket: RocketInfo) = clickListener(rocket)
}