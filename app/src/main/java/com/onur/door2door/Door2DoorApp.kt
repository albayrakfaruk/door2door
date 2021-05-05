package com.onur.door2door

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.onur.door2door.R
import dagger.hilt.android.HiltAndroidApp


/**
 * Created by Onur Şeref on 5/3/21.
 * Loodos
 * onur.seref@loodos.com
 */
@HiltAndroidApp
class Door2DoorApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, getString(R.string.google_maps_key))
    }
}