package com.onur.door2door.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onur.door2door.data.remote.model.*
import com.onur.door2door.data.remote.repository.Door2DoorRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 *   Created by farukalbayrak on 29.11.2020.
 */

class MainViewModel @ViewModelInject constructor(
    private val doorRepository: Door2DoorRepository
) : ViewModel() {

    val errorLiveData = MutableLiveData<String?>()
    val stopLocationsLiveData = MutableLiveData<List<LocationModel>>()
    val vehicleLocationLiveData = MutableLiveData<LocationModel>()
    val pickupLocationLiveData = MutableLiveData<LocationModel>()
    val statusLiveData = MutableLiveData<String>()
    val bookClosedLiveData = MutableLiveData<Boolean>()
    val dropOffLocationLiveData = MutableLiveData<LocationModel>()

    init {
        viewModelScope.launch {
            doorRepository.startSocket().collect {
                if (it is State.Success<*>) {
                    when (it.data) {
                        is BookingOpenedResponse -> {
                            statusLiveData.value = it.data.status
                            pickupLocationLiveData.value = it.data.pickupLocation
                            dropOffLocationLiveData.value = it.data.dropoffLocation
                            vehicleLocationLiveData.value = it.data.vehicleLocation
                            stopLocationsLiveData.value = it.data.intermediateStopLocations
                        }
                        is VehicleLocationUpdatedResponse -> {
                            vehicleLocationLiveData.value = it.data.vehicleLocation
                        }
                        is IntermediateStopLocationsChangedResponse -> {
                            stopLocationsLiveData.value = it.data.list
                        }
                        is StatusUpdatedResponse -> {
                            statusLiveData.value = it.data.status
                        }
                        is Result -> {
                            if (it.data.isSuccess)
                                bookClosedLiveData.value = true
                            else
                                errorLiveData.value = it.data.resultDesc
                        }
                    }
                } else if (it is State.Error<*>) {
                    errorLiveData.value = (it.throwable as String)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        doorRepository.stopSocket()
    }
}