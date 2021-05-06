package com.onur.door2door.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 *   Created by farukalbayrak on 06.05.2021.
 */

data class LocationModel(
    @SerializedName("address") val address: String? = null,
    @SerializedName("lat")val latitude: Double = 0.0,
    @SerializedName("lng")val longitude: Double = 0.0
)