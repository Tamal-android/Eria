package com.magicmind.eria.utils

import android.content.ContentResolver
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setImageFromRaw(rawId: String) {
    val uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + rawId)
    Glide.with(context).load(rawId).into(this)
}