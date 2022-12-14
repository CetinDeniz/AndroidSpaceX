package com.axuca.spacexfan.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpcomingLaunch(
    @Json(name = "links") var links: Links,

    @Json(name = "details") var details: String? = null,

    @Json(name = "flight_number") var flightNumber: Int? = null,

    @Json(name = "name") var name: String? = null,

    @Json(name = "date_utc") var dateUtc: String? = null,
    @Json(name = "date_unix") var dateUnix: Int? = null,
    @Json(name = "date_local") var dateLocal: String? = null,
    @Json(name = "date_precision") var datePrecision: String? = null,

    @Json(name = "id") var id: String? = null
) : Parcelable

@Parcelize
data class Links(
    @Json(name = "flickr") var flickr: Flickr
) : Parcelable

@Parcelize
data class Flickr(
    @Json(name = "small") var small: List<String> = listOf(),
    @Json(name = "original") var original: List<String> = listOf()
) : Parcelable