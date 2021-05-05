package com.onur.door2door.ui.main

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.onur.door2door.R
import com.onur.door2door.databinding.ActivityMainBinding
import com.onur.door2door.ui.base.BaseActivity
import com.onur.door2door.ui.component.MapComponent
import com.onur.door2door.utility.constants.AppConstants
import com.onur.door2door.utility.constants.PermissionConstants
import com.onur.door2door.utility.providers.LocationProvider
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Onur Şeref on 5/3/21.
 * Loodos
 * onur.seref@loodos.com
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), MapComponent.MapReadyListener {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var provider: LocationProvider
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0

    private var dropLatLng: LatLng? = null
    private var pickUpLatLng: LatLng? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun bindScreen() {
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = mainViewModel
        }
        binding.map.setupMap(this)
        provider = LocationProvider(this,
            onSuccess = { latitude, longitude ->
                currentLatitude = latitude
                currentLongitude = longitude
                binding.map.moveCamera(currentLatitude, currentLongitude, 17f)
            },
            onFail = { showFailDialog() },
            onPermissionFail = { onBackPressed() },
            onRequestFail = { onBackPressed() },
            onShowLoading = { },
            onStopLoading = { })
        provider.checkLocation()
        binding.pickUpTextView.setOnClickListener {
            launchLocationAutoCompleteActivity(AppConstants.PICKUP_REQUEST_CODE)
        }
        binding.dropTextView.setOnClickListener {
            launchLocationAutoCompleteActivity(AppConstants.DROP_REQUEST_CODE)
        }
    }

    override fun onMapReady() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.PICKUP_REQUEST_CODE || requestCode == AppConstants.DROP_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    when (requestCode) {
                        AppConstants.PICKUP_REQUEST_CODE -> {
                            binding.pickUpTextView.text = place.name
                            pickUpLatLng = place.latLng
                            checkAndShowRequestButton()
                        }
                        AppConstants.DROP_REQUEST_CODE -> {
                            binding.dropTextView.text = place.name
                            dropLatLng = place.latLng
                            checkAndShowRequestButton()
                        }
                        PermissionConstants.DEFAULT_SETTINGS_REQ_CODE -> {
                            provider.checkLocation()
                        }
                    }
                }
            }
        }
    }

    private fun checkAndShowRequestButton() {
        binding.requestCabButton.visibility =
            if (dropLatLng != null && pickUpLatLng != null) View.VISIBLE else View.GONE
    }

    private fun launchLocationAutoCompleteActivity(requestCode: Int) {
        val fields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(this)
        startActivityForResult(intent, requestCode)
    }

    private fun showFailDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle(getString(R.string.permission_req_title))
        builder.setMessage(getString(R.string.permission_req_desc))
        builder.setPositiveButton(getString(R.string.try_again)) { dialog, _ ->
            provider.checkLocation()
        }
        builder.setNegativeButton(getString(R.string.give_up_capital)) { dialog, which ->
            onBackPressed()
        }
        builder.show()
    }
}
