package com.onur.door2door.ui.component

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var googleFragmentMap: SupportMapFragment? = null
    var googleMap: GoogleMap? = null

    private var gMarker: Marker? = null

    var onMapReadyListener: MapReadyListener? = null
    var markerClickListener: MapMarkerClickListener? = null

    fun setupMap(onMapReadyListener: MapReadyListener) {
        this.onMapReadyListener = onMapReadyListener
        val mTransaction: FragmentTransaction =
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        googleFragmentMap = SupportMapFragment.newInstance()
        mTransaction.add(id, googleFragmentMap!!)
        mTransaction.commit()
        googleFragmentMap?.getMapAsync(this)
        setSettings()
    }

    private fun setSettings() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap?.isMyLocationEnabled = true
        }
        googleMap?.uiSettings?.isRotateGesturesEnabled = false
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
    }

    fun clear() {
        googleMap?.clear()
    }

    fun addMarker(latitude: Double, longitude: Double, iconId: Int, isDraggable: Boolean = false) {
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(iconId))
        markerOptions.draggable(isDraggable)
        markerOptions.position(LatLng(latitude, longitude))
        gMarker = googleMap?.addMarker(markerOptions)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        googleMap?.setOnMarkerClickListener(this)
        onMapReadyListener?.onMapReady()
    }

    override fun onMarkerClick(gMarker: Marker): Boolean {
        markerClickListener?.onMarkerClick(
            gMarker.position.latitude,
            gMarker.position.longitude,
            gMarker.title,
            gMarker.snippet
        )
        return true
    }

    fun moveCamera(latitude: Double, longitude: Double, zoom: Float) {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), zoom))
    }

    fun animateCamera(latitude: Double, longitude: Double, zoom: Float, duration: Int) {
        val cameraPosition = CameraPosition.Builder()
        cameraPosition.zoom(zoom)
        cameraPosition.target(LatLng(latitude, longitude))
        googleMap?.animateCamera(
            CameraUpdateFactory.newCameraPosition(cameraPosition.build()),
            duration,
            null
        )
    }

    fun setMarker(lat: Double, long: Double) {
        gMarker?.position = LatLng(lat, long)
    }

    fun removeMarker() {
        gMarker?.remove()
    }

    interface MapReadyListener {
        fun onMapReady()
    }

    interface MapMarkerClickListener {
        fun onMarkerClick(
            latitude: Double,
            longitude: Double,
            name: String? = null,
            snippet: String? = null
        )
    }

}
