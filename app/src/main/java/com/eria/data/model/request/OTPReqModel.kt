package com.eria.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OTPReqModel (
    @SerializedName("user_id")
    @Expose
    val user_id :Int,
    @SerializedName("otp")
    @Expose
    var otp :String
)