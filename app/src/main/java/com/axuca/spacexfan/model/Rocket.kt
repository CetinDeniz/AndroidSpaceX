package com.axuca.spacexfan.model

import android.os.Parcelable
import com.google.firebase.database.PropertyName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 *
 */
@Parcelize
data class Rocket(
    @PropertyName("id") @Json(name = "id") var id: String = "",
    @PropertyName("name") @Json(name = "name") var name: String = "",
    @PropertyName("description") @Json(name = "description") var description: String = "",
    @PropertyName("flickr_images") @Json(name = "flickr_images") var flickrImages: List<String> = listOf(),

    @PropertyName("height") @Json(name = "height") var height: Height? = Height(),
    @PropertyName("diameter") @Json(name = "diameter") var diameter: Diameter? = Diameter(),
    @PropertyName("mass") @Json(name = "mass") var mass: Mass? = Mass(0, 0),

    @PropertyName("first_flight") @Json(name = "first_flight") var firstFlightDate: String = "",
    @PropertyName("country") @Json(name = "country") var country: String = "", // not used
    @PropertyName("active") @Json(name = "active") var active: Boolean = false, // not used

    @PropertyName("cost_per_launch") @Json(name = "cost_per_launch") var costPerLaunch: Int = 0,
    @PropertyName("success_rate_pct") @Json(name = "success_rate_pct") var successRatePct: Int = 0 // not used
) : Parcelable


@Parcelize
data class Height(
    @PropertyName("meters") @Json(name = "meters") var meters: Double? = 0.0,
    @PropertyName("feet") @Json(name = "feet") var feet: Double? = 0.0
) : Parcelable {
    override fun toString(): String {
        return "${meters}m / ${feet}ft"
    }
}

@Parcelize
data class Diameter(
    @PropertyName("meters") @Json(name = "meters") var meters: Double? = 0.0,
    @PropertyName("feet") @Json(name = "feet") var feet: Double? = 0.0
) : Parcelable{
    override fun toString(): String {
        return "$meters m / $feet ft"
    }
}

@Parcelize
data class Mass(
    @PropertyName("kg") @Json(name = "kg") var kg: Int? = 0,
    @PropertyName("lb") @Json(name = "lb") var lb: Int? = 0
) : Parcelable{
    override fun toString(): String {
        return "$kg kg / $lb lb"
    }
}