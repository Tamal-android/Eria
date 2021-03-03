package com.magicmind.eria.data.model.response

import com.magicmind.eria.data.network.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse : BaseResponse<LoginDataModel>()
data class LoginDataModel (
    @SerializedName("user_id")
    @Expose
    val user_id: String?=null
)