package com.onur.door2door.data.remote.model

/**
 *   Created by farukalbayrak on 06.05.2021.
 */

enum class EventType {
    bookingOpened,
    bookingClosed,
    vehicleLocationUpdated,
    statusUpdated,
    intermediateStopLocationsChanged,
    error
}