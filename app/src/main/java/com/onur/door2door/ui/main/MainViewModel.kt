package com.onur.door2door.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.onur.door2door.data.remote.repository.Door2DoorRepository
import javax.inject.Inject

/**
 *   Created by farukalbayrak on 29.11.2020.
 */

class MainViewModel @Inject constructor(
    private val doorRepository: Door2DoorRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

}