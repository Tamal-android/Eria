package com.eria.data.model.response

import com.eria.data.network.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OTPVerifyResponse :BaseResponse<OtpDataModel>()

data class OtpDataModel (

    @SerializedName("user_id")
    @Expose
    val user_id: String?=null,
    @SerializedName("result")
    @Expose
    val result: Boolean?=false
)