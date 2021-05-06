package com.onur.door2door.ui.main

import androidx.activity.viewModels
import com.onur.door2door.R
import com.onur.door2door.databinding.ActivityMainBinding
import com.onur.door2door.ui.base.BaseActivity
import com.onur.door2door.ui.component.MapComponent
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Onur Şeref on 5/3/21.
 * Loodos
 * onur.seref@loodos.com
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), MapComponent.MapReadyListener {

    private val mainViewModel: MainViewModel by viewModels()

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun bindScreen() {
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = mainViewModel
        }
        binding.map.setupMap(this)
    }

    override fun onMapReady() = Unit

}
