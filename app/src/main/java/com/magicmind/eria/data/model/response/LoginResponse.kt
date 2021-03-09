package com.magicmind.eria.data.model.response

import com.magicmind.eria.data.network.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse : BaseResponse<LoginDataModel>()
data class LoginDataModel(
    @SerializedName("body")
    @Expose
    val body: String? = null,
    @SerializedName("to")
    @Expose
    val mobile_no: String? = null
)