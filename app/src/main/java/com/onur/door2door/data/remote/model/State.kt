package com.onur.door2door.data.remote.model

/**
 *   Created by farukalbayrak on 06.05.2021.
 */

sealed class State {
    data class Success<T>(val data: T) : State()
    data class Error<T>(val throwable: T) : State()
}