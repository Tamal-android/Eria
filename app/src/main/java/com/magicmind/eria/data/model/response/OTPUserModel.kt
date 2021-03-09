package com.magicmind.eria.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OTPUserModel(

    @SerializedName("id")
    @Expose
    val userId: Int? = null,
    @SerializedName("mobile")
    @Expose
    val mobile: String? = null,
    @SerializedName("role_id")
    @Expose
    val role_id: String? = null,
    @SerializedName("status")
    @Expose
    val status: String? = null
)