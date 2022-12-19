package com.axuca.spacexfan.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Class for UI representation
 */
@Parcelize
data class RocketInfo(
    var rocket: Rocket,
    var status: Boolean,
) : Parcelable