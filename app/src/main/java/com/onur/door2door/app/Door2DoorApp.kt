package com.onur.door2door.app

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.onur.door2door.R


/**
 * Created by Onur Şeref on 5/3/21.
 * Loodos
 * onur.seref@loodos.com
 */
class Door2DoorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, getString(R.string.google_maps_key));
        /*Simulator.geoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_key))
            .build()*/
    }
}