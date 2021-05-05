package com.onur.door2door.utility.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 *   Created by farukalbayrak on 29.11.2020.
 */

object ViewUtil {

    fun hideKeyboard(v: View, context: Context) {
        try {
            val inputMethodManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        } catch (e: Exception) {
            if (e.message != null) Log.d("KeyboardError", e.message!!)
        }
    }

}