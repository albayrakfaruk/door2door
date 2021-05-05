package com.onur.door2door.data.remote.repository

import com.onur.door2door.utility.constants.AppConstants
import okhttp3.*
import okio.ByteString
import org.json.JSONObject
import javax.inject.Inject

/**
 *   Created by farukalbayrak on 05.05.2021.
 */

class Door2DoorService @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val request: Request
) {
    private lateinit var socket:WebSocket

    fun connect() {
        socket= okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
            }
        })
    }

}