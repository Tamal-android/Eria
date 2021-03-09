package com.magicmind.eria.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OTPReqModel(
    @SerializedName("mobile")
    @Expose
    val mobile: String,
    @SerializedName("otp")
    @Expose
    var otp: String,
    @SerializedName("device_id")
    @Expose
    var device_id: String,
    @SerializedName("device_name")
    @Expose
    var device_name: String,
    @SerializedName("device_type")
    @Expose
    var device_type: String,
    @SerializedName("device_token")
    @Expose
    var device_token: String,
)