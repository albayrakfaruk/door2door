package com.onur.door2door.data.remote.repository

import com.onur.door2door.utility.constants.AppConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import okhttp3.WebSocket
import org.json.JSONObject
import javax.inject.Inject

/**
 *   Created by farukalbayrak on 05.05.2021.
 */

class Door2DoorService @Inject constructor(
    private var socket: WebSocket,
    private var herokuWebSocketListener: HerokuWebSocketListener
) {

    fun connect(): Channel<JSONObject> =
        with(herokuWebSocketListener) {
            this.socketEventChannel
        }

    fun disconnect() {
        socket.close(AppConstants.NORMAL_CLOSURE_STATUS, null)
        herokuWebSocketListener.socketEventChannel.close()
    }

}