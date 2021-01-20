package com.eria.data.model.response

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopPicksResponse(

    @Json(name = "data")
    val topPicks: List<TopPicks?>? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "status")
    val status: Boolean? = null
) : Parcelable