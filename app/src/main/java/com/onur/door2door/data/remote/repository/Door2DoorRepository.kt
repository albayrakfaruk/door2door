package com.onur.door2door.data.remote.repository

import com.onur.door2door.data.remote.model.*
import com.onur.door2door.utility.constants.AppConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.flow

/**
 *   Created by farukalbayrak on 05.05.2021.
 */
@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
class Door2DoorRepository(private val doorService: Door2DoorService) {

    suspend fun startSocket() = flow<State> {
        try {
            doorService.connect().consumeEach {
                when {
                    it.getString(AppConstants.RESPONSE_TYPE) == EventType.error.name -> {
                        State.Error(Result(false, it.getString(AppConstants.RESPONSE_ERROR_TEXT)))
                    }
                    it.getString(AppConstants.RESPONSE_TYPE) == EventType.bookingOpened.name -> {
                        State.Success(it.get(AppConstants.RESPONSE_DATA) as BookingOpenedResponse)
                    }
                    it.getString(AppConstants.RESPONSE_TYPE) == EventType.vehicleLocationUpdated.name -> {
                        State.Success(it.get(AppConstants.RESPONSE_DATA) as VehicleLocationUpdatedResponse)
                    }
                    it.getString(AppConstants.RESPONSE_TYPE) == EventType.statusUpdated.name -> {
                        State.Success(StatusUpdatedResponse(it.getString(AppConstants.RESPONSE_STATUS)))
                    }
                    it.getString(AppConstants.RESPONSE_TYPE) == EventType.intermediateStopLocationsChanged.name -> {
                        State.Success(IntermediateStopLocationsChangedResponse(it.get(AppConstants.RESPONSE_DATA) as List<LocationModel>))
                    }
                    it.getString(AppConstants.RESPONSE_TYPE) == EventType.bookingClosed.name -> {
                        State.Success(Result(true))
                    }
                }
            }
        } catch (e: Throwable) {
            emit(State.Error(e.message ?: ""))
            e.printStackTrace()
        }
    }

    fun stopSocket() = doorService.disconnect()
}