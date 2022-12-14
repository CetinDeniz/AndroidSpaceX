package com.axuca.spacexfan.util

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.axuca.spacexfan.R
import com.axuca.spacexfan.adapter.RocketAdapter
import com.axuca.spacexfan.adapter.RocketDetailAdapter
import com.axuca.spacexfan.adapter.UpcomingAdapter
import com.axuca.spacexfan.model.Links
import com.axuca.spacexfan.model.RocketInfo
import com.axuca.spacexfan.model.UpcomingLaunch

@BindingAdapter("submitUpcomingList")
fun RecyclerView.setUpcomingAdapterData(upcomingLaunches: List<UpcomingLaunch>?) {
    Log.e("submitList", upcomingLaunches.toString())
    upcomingLaunches?.let {
        (adapter as UpcomingAdapter).submitList(it)
    }
}

@BindingAdapter("submitRocketList")
fun RecyclerView.setRocketAdapterData(rockets: List<RocketInfo>?) {
    Log.e("submitList - Adapter", rockets.toString())
    rockets?.let {
        (adapter as RocketAdapter).submitList(it)
    }
}

@BindingAdapter("submitFavoritesList")
fun RecyclerView.setFavoritesAdapterData(rockets: List<RocketInfo>?) {
    Log.e("submitList - Adapter", rockets.toString())
    rockets?.let {
        Log.e("submitList - Adapter", rockets.toString())
        (adapter as RocketAdapter).submitList(it)
    }
}


@BindingAdapter("submitRocketDetailData")
fun RecyclerView.submitRocketDetailData(flickrImages: List<String>?) {
    flickrImages?.let {
        Log.e("RocketDetailData-Bind", flickrImages.toString())
        (adapter as RocketDetailAdapter).submitList(it)
    }
}


@BindingAdapter("loadUpcomingImage")
fun ImageView.loadImage(links: Links?) {
    links?.flickr?.let {
        if (it.original.isNotEmpty()) {
            // Load Original Image
            loadImage(it.original[0])
        } else if (it.small.isNotEmpty()) {
            // Load Small Image
            loadImage(it.small[0])
        } else {
            // Load Placeholder Image
            setImageResource(R.drawable.rocket_place_holder)
        }
    }
}

@BindingAdapter("loadRocketImage")
fun ImageView.loadRocketImage(flickrImages: List<String>?) {
    Log.e("submitList - loadImage", flickrImages.toString())

    flickrImages?.let {
        Log.e("submitList - loadImage", it.toString())
        load(it[0])
    }
}


@BindingAdapter("loadRocketDetailImage")
fun ImageView.loadRocketDetailImage(imageUrl: String) {
    loadImage(imageUrl)
}

@BindingAdapter("upcomingProgressBar")
fun ProgressBar.visible(upcomingLaunches: List<UpcomingLaunch>?) {
    upcomingLaunches?.let {
        visibility = View.INVISIBLE
    }
}

@BindingAdapter("rocketProgressBar")
fun ProgressBar.isVisible(rockets: List<RocketInfo>?) {
    rockets?.let {
        visibility = View.INVISIBLE
    }
}

@BindingAdapter("buttonState")
fun ImageView.setState(state: Boolean) {
    when (state) {
        true -> setImageResource(R.drawable.favorites_bar_true)
        else -> setImageResource(R.drawable.favorites_bar)
    }
}

@BindingAdapter("upcomingText")
fun TextView.setUpcomingText(details: String?) {
    text = if (!details.isNullOrEmpty()) {
        details
    } else {
        "Lorem ipsum dolor sit amet. Aut rerum rerum est delectus quia sit officia amet eos amet officia ea magnam nulla eum deleniti voluptas. Ut quisquam eveniet ut quia corrupti qui veniam repellat nam saepe illo."
    }
}

@BindingAdapter("launchDate")
fun TextView.setLaunchDateText(launchDate: String?) {
    text = if (!launchDate.isNullOrEmpty()) {
        launchDate.take(10)
    } else {
        "Unknown"
    }
}


@BindingAdapter("flightNumberText")
fun TextView.setFlightNumberText(flightNumber: Int?) {
    text = flightNumber?.toString() ?: "0"
}

private fun ImageView.loadImage(imageUrl: String) {
    load(imageUrl) {
        placeholder(R.drawable.loading_animation)
        /** Not necessary */
        error(R.drawable.broken_image)
    }
}