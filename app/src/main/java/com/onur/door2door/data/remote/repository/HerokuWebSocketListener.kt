package com.onur.door2door.data.remote.repository

import android.util.Log
import com.onur.door2door.utility.constants.AppConstants
import com.onur.door2door.utility.constants.AppConstants.NORMAL_CLOSURE_STATUS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class HerokuWebSocketListener : WebSocketListener() {

    val socketEventChannel: Channel<JSONObject> = Channel(Channel.UNLIMITED)

    override fun onOpen(webSocket: WebSocket, response: Response) = Unit

    override fun onMessage(webSocket: WebSocket, text: String) {
        GlobalScope.launch {
            val jsonObject = JSONObject(text)
            socketEventChannel.send(jsonObject)
            Log.d("onur2", jsonObject.toString())
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        GlobalScope.launch {
            val jsonObject = JSONObject()
            jsonObject.put("event", "error")
            jsonObject.put(AppConstants.RESPONSE_DATA, reason)
            socketEventChannel.send(jsonObject)
            Log.d("onur2", jsonObject.toString())
        }
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        socketEventChannel.close()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        GlobalScope.launch {
            val jsonObject = JSONObject()
            jsonObject.put("event", "error")
            jsonObject.put(AppConstants.RESPONSE_DATA, response?.message)
            socketEventChannel.send(jsonObject)
            Log.d("onur2", jsonObject.toString())
        }
    }

}