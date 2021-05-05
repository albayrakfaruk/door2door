package com.onur.door2door.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 *   Created by farukalbayrak on 05.05.2021.
 */

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity(){

    protected lateinit var binding: T

    abstract fun getLayoutId():Int
    abstract fun bindScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        bindScreen()
    }

}