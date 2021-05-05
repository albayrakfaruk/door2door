package com.albayrakfaruk.noteapp.utility.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.albayrakfaruk.noteapp.utility.extensions.setOnDebouncedClickListener
import com.bumptech.glide.Glide

/**
 *   Created by farukalbayrak on 29.11.2020.
 */

object ViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("showToast", "toastText")
    fun bindToast(view: View, showToast: Boolean, text: String?) {
        if (showToast) {
            text?.let {
                Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setImageUrl")
    fun setImageUrl(imageView: ImageView, imageUrl: String?) {
        imageUrl?.let {
            Glide.with(imageView.context)
                .load(it)
                .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("visibleIf")
    fun changeVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visibleInVisibleIf")
    fun visibleInVisibleIf(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("enableIf")
    fun changeEnabled(view: View, isEnabled: Boolean) {
        view.isEnabled = isEnabled
    }

    @JvmStatic
    @BindingAdapter("onSafeClick")
    fun onSafeClick(view: View, listener: View.OnClickListener) {
        view.setOnDebouncedClickListener {
            listener.onClick(view)
        }
    }

    @JvmStatic
    @BindingAdapter("onLongClick")
    fun setOnLongClickListener(
        view: View,
        func: () -> Unit
    ) {
        view.setOnLongClickListener {
            func()
            true
        }
    }
}