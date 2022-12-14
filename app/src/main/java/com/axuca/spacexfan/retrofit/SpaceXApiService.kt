package com.axuca.spacexfan.retrofit

import com.axuca.spacexfan.model.Rocket
import com.axuca.spacexfan.model.UpcomingLaunch
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.spacexdata.com/"

private const val ALL_ROCKETS = "v4/rockets"
private const val UPCOMING_LAUNCHES = "v5/launches/upcoming"

/**
 * Build the Moshi object that Retrofit will be using,
 * making sure to add the Kotlin adapter for full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface SpaceXApiService {

    @GET(ALL_ROCKETS)
    suspend fun getAllRockets(): List<Rocket>

    @GET(UPCOMING_LAUNCHES)
    suspend fun getUpcomingLaunches(): List<UpcomingLaunch>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
class SpaceXApi {
    companion object {
        val retrofitService: SpaceXApiService by lazy { retrofit.create(SpaceXApiService::class.java) }
    }
}