package com.onur.door2door.data.remote.model

/**
 *   Created by farukalbayrak on 06.05.2021.
 */

class BookingOpenedResponse(
    val status: String,
    val vehicleLocation: LocationModel,
    val pickupLocation: LocationModel,
    val dropoffLocation: LocationModel,
    val intermediateStopLocations: List<LocationModel>
)