package com.eria.data.model.response

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartList (
    @Json(name = "image_path")
    val imagePath: String? = null,

    @Json(name = "image_title")
    val imageTitle: String? = null
) : Parcelable