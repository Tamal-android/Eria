package com.eria.data.model.response

import com.eria.data.network.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRegisterResponse : BaseResponse<RegisterDataModel1>()

data class RegisterDataModel1 (

    @SerializedName("user_id")
    @Expose
    val user_id: String?=null,
    @SerializedName("otp")
    @Expose
    val otp: String?=null,
    @SerializedName("mobile_no")
    @Expose
    val mobeli_no: String?=null,
    @SerializedName("email")
    @Expose
    val email: String?=null
)