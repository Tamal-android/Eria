package com.magicmind.eria.utils

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter


class DBindingAdapter {

    companion object {

        @JvmStatic
        @BindingAdapter("loadImageDrawable")
        fun loadImageFromDrawable(imageView: AppCompatImageView, @IdRes imageRes: Int?) {
            imageRes?.let { imageView.setImageResource(it) }
        }

        @JvmStatic
        @BindingAdapter("isViewSelected")
        fun setViewSelected(view: View, isViewSelected: Boolean?) {
            if (isViewSelected != null) {
                view.isSelected = isViewSelected
            }
        }

        @JvmStatic
        @BindingAdapter("setImageDrawable")
        fun setImageDrawable(view: AppCompatImageView, drawable: Drawable?) {
            view.setImageDrawable(drawable)
        }
    }
}