package com.onur.door2door.utility.providers

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.onur.door2door.R
import com.onur.door2door.utility.constants.PermissionConstants


class LocationProvider(
    private val activity: AppCompatActivity,
    private val requestCode: Int = PermissionConstants.LOCATION_MAP_REQ_CODE,
    private val onSuccess: ((Double, Double) -> Unit?)? = null,
    private val onFail: (() -> Unit)? = null,
    private val onRequestFail: (() -> Unit)? = null,
    private val onPermissionFail: (() -> Unit)? = null,
    private val onShowLoading: (() -> Unit)? = null,
    private val onStopLoading: (() -> Unit)? = null
) {
    private var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)
    private var locationRequest = createLocationRequest()

    private var hasLocation: Boolean = false
    private var isPermissionDenied = false

    fun checkLocation(isFirstTime: Boolean = true) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!isFirstTime &&
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    ACCESS_FINE_LOCATION
                ) &&
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                showSettings()
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    requestCode
                )
            }
        } else {
            checkLocationProviderEnabled(true)
        }
    }

    private fun showSettings() {
        val builder = MaterialAlertDialogBuilder(activity)
        builder.setTitle(activity.getString(R.string.permission_req_title))
        builder.setMessage(activity.getString(R.string.permission_req_desc))
        builder.setPositiveButton(activity.getString(R.string.go_to_settings)) { dialog, _ ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivity(intent)
            dialog.dismiss()
            onFail?.invoke()
        }
        builder.setNegativeButton(activity.getString(R.string.give_up_capital)) { dialog, which ->
            dialog.dismiss()
            onFail?.invoke()
        }
        builder.show()
    }


    private fun checkLocationProviderEnabled(isFirstTime: Boolean) {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(activity)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener(activity) { checkLastKnownLocation() }
        task.addOnFailureListener(activity) {
            if (it is ResolvableApiException && isFirstTime) {
                try {
                    it.startResolutionForResult(
                        activity,
                        PermissionConstants.DEFAULT_SETTINGS_REQ_CODE
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    onFail?.invoke()
                }
            } else {
                onFail?.invoke()
            }
        }
    }

    private fun checkLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener(activity) { location: Location? ->
                if (location?.longitude ?: 0.0 > 0.0 && location?.latitude ?: 0.0 > 0.0) {
                    onSuccess?.invoke(location!!.latitude, location.longitude)
                } else {
                    requestLocationUpdates()
                }
            }
        } else {
            onFail?.invoke()
        }
    }

    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onShowLoading?.invoke()
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                mLocationCallback,
                null
            )
            Handler(Looper.getMainLooper()).postDelayed({
                if (!hasLocation) {
                    stopLocationUpdates()
                    onFail?.invoke()
                }
            }, 10000)
        }
    }

    fun checkRequestPermissions(requestCode: Int, grantResults: IntArray) {
        if (requestCode == this.requestCode && (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            checkLocationProviderEnabled(true)
        } else {
            onRequestFail?.invoke()
        }
    }

    fun checkActivityResult(requestCode: Int) {
        if (requestCode == PermissionConstants.DEFAULT_SETTINGS_REQ_CODE) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                checkLocationProviderEnabled(false)
            } else {
                isPermissionDenied = true
                onPermissionFail?.invoke()
            }
        }
    }

    private fun stopLocationUpdates() {
        onStopLoading?.invoke()
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

    private fun createLocationRequest(): LocationRequest {
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return mLocationRequest
    }

    private var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult == null || locationResult.lastLocation == null || locationResult.lastLocation.longitude == 0.0 && locationResult.lastLocation.latitude == 0.0) {
                onFail?.invoke()
            } else {
                onSuccess?.invoke(
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude
                )
            }
            stopLocationUpdates()
        }
    }

}