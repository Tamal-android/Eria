package com.magicmind.eria.data.model.response

import com.magicmind.eria.data.network.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OTPVerifyResponse :BaseResponse<OtpDataModel>()

data class OtpDataModel (

    @SerializedName("token")
    @Expose
    val token: String?=null,
    @SerializedName("user")
    @Expose
    val user: OTPUserModel?=null
)