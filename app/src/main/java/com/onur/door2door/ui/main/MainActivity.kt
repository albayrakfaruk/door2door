package com.onur.door2door.ui.main

import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.onur.door2door.R
import com.onur.door2door.databinding.ActivityMainBinding
import com.onur.door2door.ui.base.BaseActivity
import com.onur.door2door.utility.providers.LocationProvider
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Onur Şeref on 5/3/21.
 * Loodos
 * onur.seref@loodos.com
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var provider: LocationProvider

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun bindScreen() {
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = mainViewModel
        }
        provider = LocationProvider(this,
            onSuccess = { latitude, longitude ->

            },
            onFail = { showFailDialog() },
            onPermissionFail = { onBackPressed() },
            onRequestFail = {},
            onShowLoading = { },
            onStopLoading = { })
        provider.checkLocation()
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
